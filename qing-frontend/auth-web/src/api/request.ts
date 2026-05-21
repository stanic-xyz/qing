import axios, {type AxiosResponse, type AxiosError} from 'axios';
import {message} from 'antd';
import type {ApiResponse} from "./types.ts";

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
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

request.interceptors.response.use(
    (response: AxiosResponse<ApiResponse<any>>) => {
        return response.data as any;
    },
    async (error: AxiosError<ApiResponse<any>>) => {
        const originalRequest = error.config;

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
            } catch (refreshError) {
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
            const errorMsg = error.response.data?.message || '请求失败';
            message.error(errorMsg);
        } else {
            message.error('网络错误');
        }

        return Promise.reject(error);
    }
);

export const get = <T>(url: string, config?: any): Promise<ApiResponse<T>> => {
    return request.get(url, config);
};

export const post = <T>(url: string, data?: any, config?: any): Promise<ApiResponse<T>> => {
    return request.post(url, data, config);
};

export const put = <T>(url: string, data?: any, config?: any): Promise<ApiResponse<T>> => {
    return request.put(url, data, config);
};

export const patch = <T>(url: string, data?: any, config?: any): Promise<ApiResponse<T>> => {
    return request.patch(url, data, config);
};

export const del = <T>(url: string, config?: any): Promise<ApiResponse<T>> => {
    return request.delete(url, config);
};

export default {
    get,
    post,
    put,
    patch,
    delete: del
};
