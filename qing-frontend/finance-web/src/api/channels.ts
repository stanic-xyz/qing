import api from './base';

// Channel 类型定义
export interface ChannelRequest {
    name: string;
    code: string;
    icon?: string;
    type?: string;
}

export interface ChannelResponse {
    id: number;
    name: string;
    code: string;
    icon?: string;
    type?: string;
    createdAt?: string;
    updatedAt?: string;
}

export const channelApi = {
    list: () => api.get<ChannelResponse[]>('/channels'),

    create: (data: ChannelRequest) =>
        api.post<ChannelResponse>('/channels', data),

    update: (id: number, data: Partial<ChannelRequest>) =>
        api.put<ChannelResponse>(`/channels/${id}`, data),

    delete: (id: number, cascade: boolean) =>
        api.delete<void>(`/channels/${id}?cascade=${cascade}`),

    getBalance: (id: number) =>
        api.get<{ balance: number }>(`/channels/${id}/balance`),

    countTransactions: (id: number) =>
        api.get<{ count: number }>(`/accounts/${id}/transaction-count`)
};
