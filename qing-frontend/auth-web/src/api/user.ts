import request from './request';
import {getCurrentUser as getUser} from "./auth.ts";

export const getUserInfo = (id: string) => {
    return request.get(`/users/${id}`);
};

export const getCurrentUser = () => {
    return getUser();
};

export const updateUser = (id: string, data: any) => {
    return request.put(`/users/${id}`, data);
};

export const getUsers = (params: any) => {
    return request.get('/users', {params});
};

export const createUser = (data: any) => {
    return request.post('/users', data);
};

export const deleteUser = (id: string) => {
    return request.delete(`/users/${id}`);
};

export const activateUser = (id: string) => {
    return request.patch(`/users/${id}/activate`);
};

export const deactivateUser = (id: string) => {
    return request.patch(`/users/${id}/deActivate`);
};

export const sendEmailCode = (data: { email: string; userId: string }) => {
    return request.post('/users/email/code', data);
}

export const changeEmail = (data: { userId: string; email: string; code: string }) => {
    return request.post('/users/email/change', data);
}

export const assignRolesToUser = (userId: string, roleIds: string[]) => {
    return request.post('/users/assignRolesToUser', {
        userId,
        roleIds
    });
};
