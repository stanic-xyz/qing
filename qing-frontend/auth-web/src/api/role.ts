import request from './request';
import type {PageResult, Role} from "./types.ts";

export const getRoles = (params: any) => {
  return request.get<PageResult<Role>>('/roles', { params });
};

export const createRole = (data: any) => {
  return request.post<void>('/roles', data);
};

export const updateRole = (id: string, data: any) => {
  return request.put<void>(`/roles/${id}`, data);
};

export const deleteRole = (id: string) => {
  return request.delete<void>(`/roles/${id}`);
};

export const getRolePermissions = (id: string) => {
  return request.get<any[]>(`/roles/${id}/permissions`); // TODO: Define Permission type strictly if needed
};

export const assignPermissions = (roleId: string, permissionIds: string[]) => {
  return request.post<void>(`/roles/${roleId}/permissions`, { permissionIds });
};
