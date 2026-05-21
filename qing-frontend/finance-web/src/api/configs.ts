import api from './base';

export const configApi = {
    list: () => api.get('/parsers/configs'),

    create: (data: any) => api.post('/parsers/configs', data),

    update: (id: number, data: any) => api.put(`/parsers/configs/${id}`, data),

    delete: (id: number, cascade: boolean) => api.delete(`/parsers/configs/${id}?cascade=${cascade}`),

    getBalance: (id: number) => api.get(`/parsers/configs/${id}/balance`),

    countTransactions: (id: number) => api.get(`/accounts/${id}/transaction-count`),

    publish: (id: number) => api.post(`/parsers/configs/${id}/publish`),

    unpublish: (id: number) => api.post(`/parsers/configs/${id}/unpublish`)
};
