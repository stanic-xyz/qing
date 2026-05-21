import client from './base';

export interface Tag {
  id: number;
  name: string;
  color: string;
  type: 'SYSTEM' | 'USER';
  isDeleted: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTagRequest {
  name: string;
  color?: string;
}

export interface UpdateTagRequest {
  name: string;
  color?: string;
}

export interface TagStatistics {
  [key: string]: number;
}

export const tagApi = {
  list: () => client.get('/tags'),

  get: (id: number) => client.get(`/tags/${id}`),

  create: (data: CreateTagRequest) =>
    client.post('/tags', data),

  update: (id: number, data: UpdateTagRequest) =>
    client.put(`/tags/${id}`, data),

  delete: (id: number) => client.delete(`/tags/${id}`),

  statistics: () =>
    client.get('/tags/statistics'),

  getByTransaction: (transactionId: number) =>
    client.get(`/tags/transactions/${transactionId}`),

  addToTransaction: (transactionId: number, tagId: number) =>
    client.post(`/tags/transactions/${transactionId}?tagId=${tagId}`),

  removeFromTransaction: (transactionId: number, tagId: number) =>
    client.delete(`/tags/transactions/${transactionId}/${tagId}`),
};

export default tagApi;
