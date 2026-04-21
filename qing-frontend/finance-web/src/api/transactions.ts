import api from './base';

export const transactionApi = {
  list: (params: { page?: number; size?: number; accountId?: number; startDate?: string; endDate?: string }) =>
    api.get('/transactions', { params }),

  getById: (id: number) => api.get(`/transactions/${id}`),

  update: (id: number, data: any) => api.put(`/transactions/${id}`, data),

  delete: (id: number) => api.delete(`/transactions/${id}`),

  // 批量操作
  batchUpdate: (ids: number[], data: any) => api.post('/transactions/batch-update', { ids, ...data }),
};
