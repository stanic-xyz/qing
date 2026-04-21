// hooks/useUploadRecords.ts
// 统一管理导入记录列表的获取和操作
import { useState, useCallback } from 'react';
import { importApi } from '@/api/import';

export interface UploadRecord {
  id: number;
  fileName: string;
  status: string;
  parsedCount?: number;
  importedCount?: number;
  transactionStartTime?: string;
  transactionEndTime?: string;
  [key: string]: any;
}

export function useUploadRecords() {
  const [records, setRecords] = useState<UploadRecord[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchRecords = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await importApi.listUploads();
      setRecords(res.data?.data?.content || []);
    } catch (e: any) {
      setError(e.message || '获取上传记录失败');
    } finally {
      setIsLoading(false);
    }
  }, []);

  const deleteRecord = useCallback(async (id: number, softDelete = false) => {
    await importApi.deleteUpload(id, softDelete);
    setRecords(prev => prev.filter(r => r.id !== id));
  }, []);

  return { records, isLoading, error, fetchRecords, deleteRecord };
}
