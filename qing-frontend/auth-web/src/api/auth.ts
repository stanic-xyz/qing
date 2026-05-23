import request from './request';

export const login = (data: { username: string; password: string }) => {
    return request.post('/auth/login', data);
};

export const register = (data: any) => {
    return request.post('/auth/register', data);
};

export const activate = (token: string) => {
    return request.post('/auth/active', {token});
};

export const logout = () => {
    return request.post('/auth/logout');
};

export const getCurrentUser = () => {
    return request.get('/auth/me');
};

export const refreshToken = (refreshToken: string) => {
    return request.post('/auth/refresh', {refreshToken});
};
