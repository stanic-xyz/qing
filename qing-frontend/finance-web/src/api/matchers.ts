import api from './base';
import type { MatcherRule } from '../types/common';

// Matcher 类型定义
export interface MatcherRequest {
    name: string;
    priority: number;
    sourceChannel?: string;
    matchRegex?: string;
    ruleType?: string;
    counterpartyId?: number;
    setType?: string;
    targetMerchant?: string;
    targetAccountKeyword?: string;
}

export interface MatcherResponse extends MatcherRequest {
    id: number;
    createdAt?: string;
    updatedAt?: string;
}

export const matcherApi = {
    list: () => api.get<MatcherRule[]>('/matchers'),

    getActive: () => api.get<MatcherRule[]>('/matchers/active'),

    create: (data: MatcherRequest) =>
        api.post<MatcherResponse>('/matchers', data),

    update: (id: number, data: Partial<MatcherRequest>) =>
        api.put<MatcherResponse>(`/matchers/${id}`, data),

    delete: (id: number) =>
        api.delete<void>(`/matchers/${id}`),

    // 单条匹配预览
    matchSingle: (recordId: string, ruleIds?: number[]) =>
        api.post<MatcherRule[]>('/matchers/match-single', { recordId, ruleIds }),
};
