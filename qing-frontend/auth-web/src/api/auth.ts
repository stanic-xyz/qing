import request from './request';
import type {LoginResult} from "./types.ts";

export const login = (data: any) => {
    return request.post<LoginResult>('/auth/login', data);
};

export const register = (data: any) => {
    return request.post<void>('/auth/register', data);
};

export const activate = (token: string) => {
    return request.post<void>('/auth/active', {token});
};
