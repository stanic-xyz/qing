import api from './base';

// ParserConfig 类型定义
export interface ParserConfigRequest {
    name: string;
    channelId?: number;
    encoding?: string;
    skipRows?: number;
    fieldMappingRules?: string;
    metadataRules?: string;
    script?: string;
    status?: 'DRAFT' | 'PUBLISHED';
}

export interface ParserConfigResponse {
    id: number;
    name: string;
    channelId?: number;
    encoding?: string;
    skipRows?: number;
    fieldMappingRules?: string;
    metadataRules?: string;
    script?: string;
    status?: 'DRAFT' | 'PUBLISHED';
    createdAt?: string;
    updatedAt?: string;
}

export const configApi = {
    list: () => api.get<ParserConfigResponse[]>('/parsers/configs'),

    create: (data: ParserConfigRequest) => api.post<ParserConfigResponse>('/parsers/configs', data),

    update: (id: number, data: Partial<ParserConfigRequest>) =>
        api.put<ParserConfigResponse>(`/parsers/configs/${id}`, data),

    delete: (id: number, cascade: boolean) =>
        api.delete<void>(`/parsers/configs/${id}?cascade=${cascade}`),

    getBalance: (id: number) =>
        api.get<{ balance: number }>(`/parsers/configs/${id}/balance`),

    countTransactions: (id: number) =>
        api.get<{ count: number }>(`/accounts/${id}/transaction-count`),

    publish: (id: number) => api.post<void>(`/parsers/configs/${id}/publish`),

    unpublish: (id: number) => api.post<void>(`/parsers/configs/${id}/unpublish`)
};
