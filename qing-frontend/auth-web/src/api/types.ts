export interface ApiResponse<T> {
  code: number;
  message: string;
  result: T;
  devMessage: string | null;
  success: boolean;
}

export interface LoginResult {
  token: string;
  userId: number;
  username: string;
  avatar: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  nickname?: string;
  email?: string;
  phone?: string;
}

export interface User {
  id?: string | number;
  uid?: string | number;
  username: string;
  nickname?: string;
  email?: string;
  phone?: string;
  avatar?: string;
  description?: string;
  status?: 'ACTIVE' | 'INACTIVE' | 'LOCKED' | string;
  active?: boolean;
  locked?: boolean;
  roles?: Role[];
  createdAt?: string;
  updatedAt?: string;
  registeredAt?: string;
  lastLoginAt?: string;
}

export interface Role {
  id: string;
  code: string;
  name: string;
  description?: string;
  type?: string;
  status?: string;
  permissions?: Permission[];
}

export interface Permission {
  id: string;
  code: string;
  name: string;
  type?: 'MENU' | 'BUTTON' | 'API' | string;
  resource?: string;
  action?: string;
  parentId?: string | null;
  sortOrder?: number;
  description?: string;
  children?: Permission[];
}

export interface PageResult<T> {
  items: T[];
  total: number;
  page: number;
  size: number;
  totalPages: number;
}
