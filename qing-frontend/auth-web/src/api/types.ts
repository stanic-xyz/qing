export interface ApiResponse<T> {
  code: number;
  message: string;
  result: T;
  devMessage: string | null;
  success: boolean;
}

export interface LoginResult {
  scope: string;
  accessToken: string;
  idToken: string;
  refreshToken: string;
  tokenType: string;
  expireIn: string;
  code: string;
}

export interface User {
  id: string;
  username: { value: string };
  nickname: string;
  email: { value: string };
  phone: { value: string };
  avatar: string;
  description: string;
  status: 'ACTIVE' | 'INACTIVE' | 'LOCKED'; // Assuming status values
  roles: Role[];
  createdAt: string;
  updatedAt: string;
}

export interface Role {
  id: string;
  code: string;
  name: string;
  description: string;
  permissions?: Permission[];
}

export interface Permission {
  id: string;
  code: string;
  name: string;
  type: 'MENU' | 'BUTTON' | 'API';
  resource: string;
  action: string;
  parentId: string | null;
  sortOrder: number;
  description: string;
  children?: Permission[];
}

export interface PageResult<T> {
  items: T[];
  total: number;
  page: number;
  size: number;
  totalPages: number;
}
