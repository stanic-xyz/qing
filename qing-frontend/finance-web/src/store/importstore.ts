// store/importStore.ts
// Zustand store 管理导入流程的全局状态
import { create } from 'zustand';
import type { PreviewRecord, UploadBatchOverview } from '@/pages/Import/types';

interface ImportFlowState {
  // 当前展开的 uploadId
  expandedUploadId: string | null;

  // 各展开行的预览数据 (uploadId -> data)
  previewMap: Record<string, {
    overview: UploadBatchOverview | null;
    previewRecords: PreviewRecord[];
    totalCount: number;
    hasMore: boolean;
  }>;

  // 各展开行的处理状态 (uploadId -> status)
  rowStates: Record<string, {
    isLoadingPreview: boolean;
    processStep: number;
    modifiedRecords: Record<string, any>;
    lockedTempIds: string[];
    isMatching: boolean;
    isImporting: boolean;
  }>;

  // Actions
  setExpanded: (uploadId: string | null) => void;
  setPreview: (uploadId: string, data: {
    overview: UploadBatchOverview | null;
    previewRecords: PreviewRecord[];
    totalCount: number;
    hasMore: boolean;
  }) => void;
  setRowState: (uploadId: string, state: Partial<{
    isLoadingPreview: boolean;
    processStep: number;
    modifiedRecords: Record<string, any>;
    lockedTempIds: string[];
    isMatching: boolean;
    isImporting: boolean;
  }>) => void;
  resetRow: (uploadId: string) => void;
}

export const useImportStore = create<ImportFlowState>((set) => ({
  expandedUploadId: null,
  previewMap: {},
  rowStates: {},

  setExpanded: (uploadId) => set({ expandedUploadId: uploadId }),

  setPreview: (uploadId, data) =>
    set(state => ({
      previewMap: { ...state.previewMap, [uploadId]: data },
    })),

  setRowState: (uploadId, partial) =>
    set(state => ({
      rowStates: {
        ...state.rowStates,
        [uploadId]: { ...(state.rowStates[uploadId] || {}), ...partial },
      },
    })),

  resetRow: (uploadId) =>
    set(state => {
      const { [uploadId]: _, ...rest } = state.rowStates;
      const { [uploadId]: __, ...restPreview } = state.previewMap;
      return {
        rowStates: rest,
        previewMap: restPreview,
      };
    }),
}));
