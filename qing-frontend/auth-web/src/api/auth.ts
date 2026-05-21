import request from './request';
import type {LoginResult, RegisterRequest, User} from "./types.ts";

export const login = (data: { username: string; password: string }) => {
    return request.post<LoginResult>('/auth/login', data);
};

export const register = (data: RegisterRequest) => {
    return request.post<User>('/auth/register', data);
};

export const activate = (token: string) => {
    return request.post<void>('/auth/active', {token});
};

export const logout = () => {
    return request.post<void>('/auth/logout');
};

export const getCurrentUser = () => {
    return request.get<User>('/auth/me');
};

export const refreshToken = (refreshToken: string) => {
    return request.post<string>('/auth/refresh', {refreshToken});
};
