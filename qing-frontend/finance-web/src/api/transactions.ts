import api from './base';

// Transaction 类型定义
export interface TransactionRequest {
    categoryId?: number;
    counterpartyId?: number;
    tags?: number[];
    remark?: string;
}

export interface TransactionResponse {
    id: number;
    amount: number;
    transactionTime: string;
    directionType: string;
    type?: string;
    merchant?: string;
    categoryId?: number;
    counterpartyId?: number;
    accountId: number;
    createdAt: string;
    updatedAt: string;
}

export interface TransactionListParams {
    page?: number;
    size?: number;
    accountId?: number;
    startDate?: string;
    endDate?: string;
}

export const transactionApi = {
    list: (params: TransactionListParams) =>
        api.get<TransactionResponse[]>('/transactions', { params }),

    getById: (id: number) =>
        api.get<TransactionResponse>(`/transactions/${id}`),

    update: (id: number, data: Partial<TransactionRequest>) =>
        api.put<TransactionResponse>(`/transactions/${id}`, data),

    delete: (id: number) =>
        api.delete<void>(`/transactions/${id}`),

    // 批量操作
    batchUpdate: (ids: number[], data: Partial<TransactionRequest>) =>
        api.post<void>('/transactions/batch-update', { ids, ...data }),
};
