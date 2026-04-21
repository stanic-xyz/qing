import api from './base';

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

  // 确认导入
  confirmImport: (uploadId: string, confirmedTempIds?: string[], modifications?: any[]) =>
    api.post(`/uploads/${uploadId}/import`, { uploadId, confirmedTempIds, modifications }),

  // 删除上传
  deleteUpload: (id: number, softDelete?: boolean) =>
    api.delete(`/uploads/${id}`, { params: { softDelete } }),
};
