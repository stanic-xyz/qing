import api from './base';
import type { RecordModification } from '../types/common';

// Import 类型定义
export interface ImportModification {
    tempId: string;
    type?: string;
    merchant?: string;
    targetAccountId?: number;
    categoryId?: number;
    counterpartyId?: number;
}

export interface QianjiExecuteRequest {
    confirmTempIds?: string[];
    modifications?: RecordModification[];
}

export interface QianjiRecord {
    tempId: string;
    amount: number;
    transactionTime: string;
    merchant?: string;
    type?: string;
}

export const importApi = {
    // 上传文件列表
    listUploads: () => api.get('/uploads'),

    // 上传并解析文件
    uploadFiles: (files: File[], parserId: number, accountId: number) => {
        const formData = new FormData();
        files.forEach(f => formData.append('files', f));
        formData.append('parserId', String(parserId));
        formData.append('accountId', String(accountId));
        return api.post('/uploads/batch', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
    },

    // 获取预览数据
    getPreview: (uploadId: string, page?: number, size?: number) =>
        api.get(`/uploads/${uploadId}/preview`, { params: { page, size } }),

    // 获取批次概览
    getBatchOverview: (uploadId: string) => api.get(`/uploads/${uploadId}/overview`),

    // 开始匹配
    startMatching: (uploadId: string, lockedTempIds?: string[]) =>
        api.post(`/uploads/${uploadId}/match`, { lockedTempIds }),

    // 获取匹配状态
    getMatchStatus: (uploadId: string) => api.get(`/uploads/${uploadId}/match/status`),

    // 确认导入
    confirmImport: (uploadId: string, confirmedTempIds?: string[], modifications?: RecordModification[]) =>
        api.post(`/uploads/${uploadId}/import`, { uploadId, confirmedTempIds, modifications }),

    // 删除上传
    deleteUpload: (id: number, softDelete?: boolean) =>
        api.delete(`/uploads/${id}`, { params: { softDelete } }),

    // ===== 钱迹导入新流程 =====
    // 上传文件
    qianjiUpload: (file: File, accountId: number) => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('accountId', String(accountId));
        return api.post('/import/qianji/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
    },

    // 文件预览
    qianjiPreview: (fileId: number) =>
        api.get(`/import/qianji/preview?fileId=${fileId}`),

    // 解析预览
    qianjiPreviewAndParse: (fileId: number) =>
        api.post(`/import/qianji/previewAndParse?fileId=${fileId}`),

    // 执行导入
    qianjiExecute: (fileId: number, request: QianjiExecuteRequest) =>
        api.post(`/import/qianji/execute?fileId=${fileId}`, request),

    // 执行导入（records 模式）
    qianjiExecuteRecords: (records: QianjiRecord[]) =>
        api.post('/import/qianji/executeRecords', records),

    // 文件列表
    qianjiListFiles: (accountId: number, page = 0, size = 20) =>
        api.get(`/import/qianji/files?accountId=${accountId}&page=${page}&size=${size}`),

    // 删除文件
    qianjiDeleteFile: (fileId: number) =>
        api.delete(`/import/qianji/files/${fileId}`),

    // 文件去重检测
    qianjiCheckDuplicate: (fileHash: string, accountId: number) =>
        api.post('/import/qianji/checkDuplicate', null, { params: { fileHash, accountId } }),
};
