import request from './request';
import type {PageResult, User} from "./types.ts";

export const getUserInfo = (id: string) => {
    return request.get<User>(`/users/${id}`);
};

export const updateUser = (id: string, data: any) => {
    return request.put<void>(`/users/${id}`, data);
};

// Admin
export const getUsers = (params: any) => {
    return request.get<PageResult<User>>('/users', {params});
};

export const createUser = (data: any) => {
    return request.post<void>('/users', data);
};

export const deleteUser = (id: string) => {
    return request.delete<void>(`/users/${id}`);
};

export const activateUser = (id: string) => {
    return request.patch<void>(`/users/${id}/activate`);
};

export const deactivateUser = (id: string) => {
    return request.patch<void>(`/users/${id}/deActivate`);
};

export const sendEmailCode = (data: { email: string; userId: string }) => {
    return request.post<void>('/users/email/code', data);
}

export const changeEmail = (data: { userId: string; email: string; code: string }) => {
    return request.post<void>('/users/email/change', data);
}
