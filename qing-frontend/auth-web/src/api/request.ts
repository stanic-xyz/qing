import axios, {type AxiosResponse} from 'axios';
import {message} from 'antd';
import type {ApiResponse} from "./types.ts";

const request = axios.create({
    baseURL: '/api/v1',
    timeout: 10000,
});

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
        console.info("请求结束：",response);
        const res = response.data;
        if (!res.success) {
            message.error(res.message || 'Error');
            return Promise.reject(new Error(res.message || 'Error'));
        }
        return res as any; // We cast to any here to satisfy axios types but the caller will specify T
    },
    (error) => {
        if (error.response) {
            if (error.response.status === 401) {
                localStorage.removeItem('token');
                window.location.href = '/login';
            } else {
                message.error(error.response.data?.message || 'Request Failed');
            }
        } else {
            message.error('Network Error');
        }
        return Promise.reject(error);
    }
);

// Wrapper methods to type the response
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
