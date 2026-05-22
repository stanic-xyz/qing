import axios, {type AxiosResponse, type AxiosError} from 'axios';
import {message} from 'antd';

const request = axios.create({
    baseURL: '/api/v1',
    timeout: 10000,
});

let isRefreshing = false;
let requestQueue: Array<(token: string) => void> = [];

const processQueue = (token: string | null) => {
    requestQueue.forEach((cb) => cb(token as string));
    requestQueue = [];
};

request.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        console.log('请求拦截:', config.url, 'Token:', token ? '存在' : '不存在');
        return config;
    },
    (error) => {
        console.error('请求拦截器错误:', error);
        return Promise.reject(error);
    }
);

request.interceptors.response.use(
    (response: AxiosResponse) => {
        console.log('响应拦截 - 原始响应:', response);
        console.log('响应数据:', response.data);

        const data = response.data;

        if (data && typeof data === 'object') {
            if ('success' in data && !data.success) {
                const errorMsg = data.message || '请求失败';
                console.error('API 返回失败:', errorMsg);
                message.error(errorMsg);
                return Promise.reject(new Error(errorMsg));
            }
        }

        return data;
    },
    async (error: AxiosError) => {
        const originalRequest = error.config;

        console.error('响应错误:', error.response?.status, error.message);

        if (error.response?.status === 401 && originalRequest && !originalRequest.headers['X-Retry']) {
            if (isRefreshing) {
                return new Promise((resolve) => {
                    requestQueue.push((token: string) => {
                        originalRequest.headers['Authorization'] = `Bearer ${token}`;
                        resolve(request(originalRequest));
                    });
                });
            }

            isRefreshing = true;

            try {
                const refreshToken = localStorage.getItem('refreshToken');
                if (refreshToken) {
                    const res = await axios.post('/api/v1/auth/refresh', {refreshToken});
                    if (res.data.success) {
                        const newToken = res.data.result;
                        localStorage.setItem('token', newToken);
                        processQueue(newToken);
                        originalRequest.headers['Authorization'] = `Bearer ${newToken}`;
                        originalRequest.headers['X-Retry'] = 'true';
                        return request(originalRequest);
                    }
                }

                localStorage.removeItem('token');
                localStorage.removeItem('userInfo');
                localStorage.removeItem('userId');
                localStorage.removeItem('refreshToken');
                window.location.href = '/login';
                message.error('登录已过期，请重新登录');
            } catch {
                processQueue(null);
                localStorage.removeItem('token');
                localStorage.removeItem('userInfo');
                localStorage.removeItem('userId');
                localStorage.removeItem('refreshToken');
                window.location.href = '/login';
                message.error('登录已过期，请重新登录');
            } finally {
                isRefreshing = false;
            }
        } else if (error.response) {
            const errorData = error.response.data as any;
            const errorMsg = errorData?.message || error.message || '请求失败';
            console.error('请求失败:', errorMsg);
            message.error(errorMsg);
        } else if (error.request) {
            console.error('网络错误:', error.message);
            message.error('网络错误，请检查网络连接');
        } else {
            console.error('请求错误:', error.message);
            message.error(error.message || '请求失败');
        }

        return Promise.reject(error);
    }
);

export const get = (url: string, config?: any): Promise<any> => {
    return request.get(url, config);
};

export const post = (url: string, data?: any, config?: any): Promise<any> => {
    return request.post(url, data, config);
};

export const put = (url: string, data?: any, config?: any): Promise<any> => {
    return request.put(url, data, config);
};

export const patch = (url: string, data?: any, config?: any): Promise<any> => {
    return request.patch(url, data, config);
};

export const del = (url: string, config?: any): Promise<any> => {
    return request.delete(url, config);
};

export default {
    get,
    post,
    put,
    patch,
    delete: del
};
