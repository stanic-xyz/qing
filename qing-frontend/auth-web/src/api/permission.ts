import request from './request';
import type {Permission} from "./types.ts";

export const getPermissions = () => {
    return request.get<Permission[]>('/permissions');
};

export const createPermission = (data: any) => {
    return request.post<void>('/permissions', data);
};

export const updatePermission = (id: string, data: any) => {
    return request.put<void>(`/permissions/${id}`, data);
};

export const deletePermission = (id: string) => {
    return request.delete<void>(`/permissions/${id}`);
};
