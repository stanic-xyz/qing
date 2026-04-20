// hooks/useImportFlow.ts
// 管理导入流程的完整状态机:
// Step 1: 选择文件上传
// Step 2: 上传并解析
// Step 3: 预览确认
// Step 4: 执行匹配
// Step 5: 确认入库

import { useState, useCallback } from 'react';
import { importApi } from '@/api/import';
import type { PreviewRecord, UploadBatchOverviewResponse } from '@/pages/Import/types';

export type ImportStep = 'idle' | 'uploading' | 'preview' | 'matching' | 'confirming' | 'done';

export interface ImportFlowState {
  // 当前批次概览
  overview: UploadBatchOverviewResponse | null;
  // 预览记录
  previewRecords: PreviewRecord[];
  // 分页
  totalCount: number;
  hasMore: boolean;
  // 用户编辑过的记录 (tempId -> 修改内容)
  modifiedRecords: Record<string, any>;
  // 用户锁定的记录ID（不参与匹配）
  lockedTempIds: Set<string>;
  // 用户选择确认的记录ID
  selectedTempIds: Set<string>;
  // 当前步骤
  step: ImportStep;
  // 匹配状态
  matchStatus: 'idle' | 'processing' | 'completed' | 'failed';
  matchError: string | null;
  // 导入结果
  importResult: number | null;
}

const initialState: ImportFlowState = {
  overview: null,
  previewRecords: [],
  totalCount: 0,
  hasMore: false,
  modifiedRecords: {},
  lockedTempIds: new Set(),
  selectedTempIds: new Set(),
  step: 'idle',
  matchStatus: 'idle',
  matchError: null,
  importResult: null,
};

export function useImportFlow() {
  const [state, setState] = useState<ImportFlowState>(initialState);

  // --- Actions ---

  /** 上传并解析文件 */
  const uploadFiles = useCallback(async (
    files: File[],
    parserId: number,
    accountId: number
  ) => {
    setState(s => ({ ...s, step: 'uploading', matchStatus: 'idle', matchError: null }));

    try {
      const res = await importApi.uploadFiles(files, parserId, accountId);
      const uploadId = res.data?.data?.[0]?.uploadId;

      if (!uploadId) throw new Error('上传成功但未返回 uploadId');

      // 立即获取概览
      const overviewRes = await importApi.getBatchOverview(uploadId);
      const overview = overviewRes.data?.data;

      // 获取第一页预览
      const previewRes = await importApi.getPreview(uploadId, 0, 50);
      const previewData = previewRes.data?.data;

      setState(s => ({
        ...s,
        overview,
        previewRecords: previewData?.previewRecords || [],
        totalCount: previewData?.totalCount || 0,
        hasMore: previewData?.hasMore || false,
        step: 'preview',
        modifiedRecords: {},
        lockedTempIds: new Set(),
        selectedTempIds: new Set(),
      }));

      return uploadId;
    } catch (e: any) {
      setState(s => ({ ...s, step: 'idle' }));
      throw e;
    }
  }, []);

  /** 加载更多预览记录 */
  const loadMorePreview = useCallback(async (page: number, size = 50) => {
    if (!state.overview?.uploadId) return;
    try {
      const res = await importApi.getPreview(state.overview.uploadId, page, size);
      const data = res.data?.data;
      setState(s => ({
        ...s,
        previewRecords: page === 0 ? (data?.previewRecords || []) : [...s.previewRecords, ...(data?.previewRecords || [])],
        totalCount: data?.totalCount || s.totalCount,
        hasMore: data?.hasMore || false,
      }));
    } catch (e) {
      console.error('加载更多失败', e);
    }
  }, [state.overview?.uploadId]);

  /** 修改单条记录 */
  const modifyRecord = useCallback((tempId: string, modifications: any) => {
    setState(s => ({
      ...s,
      modifiedRecords: { ...s.modifiedRecords, [tempId]: modifications },
    }));
  }, []);

  /** 锁定/解锁记录（跳过匹配） */
  const toggleLock = useCallback((tempId: string) => {
    setState(s => {
      const next = new Set(s.lockedTempIds);
      if (next.has(tempId)) next.delete(tempId);
      else next.add(tempId);
      return { ...s, lockedTempIds: next };
    });
  }, []);

  /** 全选/取消全选 */
  const toggleSelectAll = useCallback((select: boolean) => {
    setState(s => ({
      ...s,
      selectedTempIds: select ? new Set(s.previewRecords.map(r => r.tempId)) : new Set(),
    }));
  }, [state.previewRecords]);

  /** 切换单条选择 */
  const toggleSelect = useCallback((tempId: string) => {
    setState(s => {
      const next = new Set(s.selectedTempIds);
      if (next.has(tempId)) next.delete(tempId);
      else next.add(tempId);
      return { ...s, selectedTempIds: next };
    });
  }, []);

  /** 开始匹配 */
  const startMatching = useCallback(async () => {
    if (!state.overview?.uploadId) return;
    setState(s => ({ ...s, step: 'matching', matchStatus: 'processing' }));

    try {
      const res = await importApi.startMatching(
        state.overview.uploadId,
        Array.from(state.lockedTempIds)
      );

      // 轮询匹配状态
      await pollMatchStatus(state.overview.uploadId);

    } catch (e: any) {
      setState(s => ({
        ...s,
        matchStatus: 'failed',
        matchError: e.message || '匹配失败',
      }));
    }
  }, [state.overview?.uploadId, state.lockedTempIds]);

  /** 轮询匹配状态直到完成 */
  const pollMatchStatus = useCallback(async (uploadId: string) => {
    const maxAttempts = 30;
    let attempts = 0;

    while (attempts < maxAttempts) {
      await new Promise(r => setTimeout(r, 2000));
      attempts++;

      try {
        const res = await importApi.getMatchStatus(uploadId);
        const status = res.data?.data?.status;

        if (status === 'COMPLETED') {
          // 匹配完成，重新加载预览
          const previewRes = await importApi.getPreview(uploadId, 0, 200);
          const previewData = previewRes.data?.data;

          setState(s => ({
            ...s,
            step: 'matching',
            matchStatus: 'completed',
            previewRecords: previewData?.previewRecords || s.previewRecords,
            totalCount: previewData?.totalCount || s.totalCount,
          }));
          return;
        } else if (status === 'FAILED') {
          throw new Error(res.data?.data?.errorMsg || '匹配失败');
        }
      } catch (e: any) {
        setState(s => ({
          ...s,
          matchStatus: 'failed',
          matchError: e.message,
        }));
        return;
      }
    }

    setState(s => ({
      ...s,
      matchStatus: 'failed',
      matchError: '匹配超时，请稍后重试',
    }));
  }, []);

  /** 确认导入 */
  const confirmImport = useCallback(async () => {
    if (!state.overview?.uploadId) return;
    setState(s => ({ ...s, step: 'confirming' }));

    try {
      const modifications = Object.entries(state.modifiedRecords).map(([tempId, mods]) => ({
        tempId,
        ...mods,
      }));

      const res = await importApi.confirmImport(
        state.overview.uploadId,
        Array.from(state.selectedTempIds),
        modifications
      );

      const importedCount = res.data?.data || 0;
      setState(s => ({ ...s, step: 'done', importResult: importedCount }));
      return importedCount;
    } catch (e: any) {
      setState(s => ({ ...s, step: 'matching', matchError: e.message || '导入失败' }));
      throw e;
    }
  }, [state.overview?.uploadId, state.selectedTempIds, state.modifiedRecords]);

  /** 重置流程 */
  const reset = useCallback(() => {
    setState(initialState);
  }, []);

  /** 返回预览阶段 */
  const backToPreview = useCallback(() => {
    if (state.step === 'matching' || state.step === 'done') {
      setState(s => ({ ...s, step: 'preview', matchStatus: 'idle', matchError: null }));
    }
  }, [state.step]);

  return {
    ...state,
    uploadFiles,
    loadMorePreview,
    modifyRecord,
    toggleLock,
    toggleSelect,
    toggleSelectAll,
    startMatching,
    confirmImport,
    reset,
    backToPreview,
  };
}
