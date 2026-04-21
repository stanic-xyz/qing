import api from './base';

export const dashboardApi = {
  getMonthly: (year: number, month: number) =>
    api.get('/dashboard/monthly', { params: { year, month } }),

  getYearlyTrend: (year: number) =>
    api.get('/dashboard/yearly-trend', { params: { year } }),

  getCategorySpending: (year: number, month: number) =>
    api.get('/dashboard/category-spending', { params: { year, month } }),

  getPaymentMethods: (year: number, month: number) =>
    api.get('/dashboard/payment-methods', { params: { year, month } }),

  getTrend: (months?: number) =>
    api.get('/dashboard/trend', { params: { months } }),

  getAccountBalances: () =>
    api.get('/dashboard/account-balances'),

  getSuspicious: (year: number, month: number) =>
    api.get('/dashboard/suspicious', { params: { year, month } }),
};
