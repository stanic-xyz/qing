import api from './base';

export const channelApi = {
    list: () => api.get('/channels'),

    create: (data: any) => api.post('/channels', data),

    update: (id: number, data: any) => api.put(`/channels/${id}`, data),

    delete: (id: number, cascade: boolean) => api.delete(`/channels/${id}?cascade=${cascade}`),

    getBalance: (id: number) => api.get(`/channels/${id}/balance`),

    countTransactions: (id: number) => api.get(`/accounts/${id}/transaction-count`)
};
