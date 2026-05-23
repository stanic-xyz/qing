import request from './request';

export const getRoles = (params: any) => {
    return request.get('/roles', {params});
};

export const createRole = (data: any) => {
    return request.post('/roles', data);
};

export const updateRole = (id: string, data: any) => {
    return request.put(`/roles/${id}`, data);
};

export const deleteRole = (id: string) => {
    return request.delete(`/roles/${id}`);
};

export const getRolePermissions = (id: string) => {
    return request.get(`/roles/${id}/permissions`);
};

export const assignPermissions = (roleId: string, permissionIds: string[]) => {
    return request.post(`/roles/${roleId}/permissions`, {permissionIds});
};
