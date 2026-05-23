import request from './request';

export const getPermissions = () => {
    return request.get('/permissions');
};

export const createPermission = (data: any) => {
    return request.post('/permissions', data);
};

export const updatePermission = (id: string, data: any) => {
    return request.put(`/permissions/${id}`, data);
};

export const deletePermission = (id: string) => {
    return request.delete(`/permissions/${id}`);
};
