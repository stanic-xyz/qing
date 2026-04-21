import api from './base';

export const matcherApi = {
  list: () => api.get('/matchers'),

  getActive: () => api.get('/matchers/active'),

  create: (data: any) => api.post('/matchers', data),

  update: (id: number, data: any) => api.put(`/matchers/${id}`, data),

  delete: (id: number) => api.delete(`/matchers/${id}`),

  // 单条匹配预览
  matchSingle: (recordId: string, ruleIds?: number[]) =>
    api.post('/matchers/match-single', { recordId, ruleIds }),
};
