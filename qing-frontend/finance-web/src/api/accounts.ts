import api from './base';

export const accountApi = {
  list: () => api.get('/accounts'),

  create: (data: any) => api.post('/accounts', data),

  update: (id: number, data: any) => api.put(`/accounts/${id}`, data),

  delete: (id: number) => api.delete(`/accounts/${id}`),

  getBalance: (id: number) => api.get(`/accounts/${id}/balance`),
};
