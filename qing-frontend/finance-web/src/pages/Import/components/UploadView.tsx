import { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import { Loader2, UploadCloud, X } from 'lucide-react';
import type { Account, ParserItem, UploadBatchPreviewResponse } from '../types';

interface UploadViewProps {
  accounts: Account[];
  onClose: () => void;
}

export default function UploadView({ accounts, onClose }: UploadViewProps) {
  const [files, setFiles] = useState<FileList | null>(null);
  const [selectedChannel, setSelectedChannel] = useState('');
  const [parsers, setParsers] = useState<ParserItem[]>([]);
  const [filteredParsers, setFilteredParsers] = useState<ParserItem[]>([]);
  const [parserId, setParserId] = useState('');

  const [accountId, setAccountId] = useState<number | undefined>(undefined);
  const [filteredAccounts, setFilteredAccounts] = useState<Account[]>([]);

  const [isUploading, setIsUploading] = useState(false);
  const [batchPreviews, setBatchPreviews] = useState<UploadBatchPreviewResponse[]>([]);
  const [activePreviewTab, setActivePreviewTab] = useState(0);

  useEffect(() => {
    (async () => {
      try {
        const res = await axios.get('/api/finance/parsers');
        setParsers(res.data.data || []);
      } catch (e) {
        console.error(e);
        alert('获取解析器失败');
      }
    })();
  }, []);

  const channelOptions = useMemo(() => {
    const fromAccounts = accounts.map(a => a.channel).filter(Boolean) as string[];
    const fromParsers = parsers.map(p => p.channel).filter(Boolean) as string[];
    return Array.from(new Set([...fromAccounts, ...fromParsers]));
  }, [accounts, parsers]);

  const selectedFileTypes = useMemo(() => {
    if (!files || files.length === 0) return new Set<string>();
    const types = new Set<string>();
    for (const f of Array.from(files)) {
      const name = f.name.toLowerCase();
      if (name.endsWith('.csv')) types.add('CSV');
      else if (name.endsWith('.xls') || name.endsWith('.xlsx')) types.add('EXCEL');
      else if (name.endsWith('.pdf')) types.add('PDF');
    }
    return types;
  }, [files]);

  useEffect(() => {
    if (selectedChannel) return;
    if (channelOptions.length > 0) setSelectedChannel(channelOptions[0]);
  }, [channelOptions, selectedChannel]);

  useEffect(() => {
    const next = selectedChannel ? accounts.filter(a => a.channel === selectedChannel) : accounts;
    setFilteredAccounts(next);
    if (next.length === 1) {
      setAccountId(next[0].id);
    } else if (accountId && !next.find(a => a.id === accountId)) {
      setAccountId(undefined);
    }
  }, [accounts, selectedChannel]);

  useEffect(() => {
    let next = selectedChannel ? parsers.filter(p => p.channel === selectedChannel) : parsers;
    if (selectedFileTypes.size === 1) {
      const onlyType = Array.from(selectedFileTypes)[0];
      const typed = next.filter(p => (p.fileType || '').toUpperCase() === onlyType);
      if (typed.length > 0) next = typed;
    }

    setFilteredParsers(next);
    if (next.length === 0) {
      setParserId('');
      return;
    }
    if (!next.find(p => p.id === parserId)) {
      setParserId(next[0].id);
    }
  }, [parsers, selectedChannel, selectedFileTypes]);

  const currentParser = useMemo(() => filteredParsers.find(p => p.id === parserId), [filteredParsers, parserId]);

  const fileAccept = useMemo(() => {
    const t = (currentParser?.fileType || '').toUpperCase();
    if (t === 'CSV') return '.csv';
    if (t === 'EXCEL') return '.xls,.xlsx';
    if (t === 'PDF') return '.pdf';
    return '.csv,.xls,.xlsx,.pdf';
  }, [currentParser]);

  const handleUploadBatch = async () => {
    if (!files || files.length === 0) return alert('请先选择文件');
    if (!selectedChannel) return alert('请先选择渠道类型');
    if (!accountId) return alert('请先选择关联账户');
    if (!parserId) return alert('请先选择解析器');

    setIsUploading(true);
    const formData = new FormData();
    for (const f of Array.from(files)) formData.append('files', f);
    formData.append('parserId', parserId);
    formData.append('accountId', String(accountId));

    try {
      const res = await axios.post('/api/bills/upload-batch', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      setBatchPreviews(res.data.data || []);
      setActivePreviewTab(0);
    } catch (e: any) {
      console.error(e);
      alert('上传解析失败: ' + (e.response?.data?.message || e.message));
    } finally {
      setIsUploading(false);
    }
  };

  const handleDiscardFile = async (uploadId: string, index: number) => {
    if (!window.confirm('确认舍弃该文件的解析结果吗？')) return;
    try {
      await axios.delete(`/api/finance/uploads/${uploadId}?softDelete=false`);
      const next = [...batchPreviews];
      next.splice(index, 1);
      setBatchPreviews(next);
      if (activePreviewTab >= next.length) setActivePreviewTab(Math.max(0, next.length - 1));
    } catch (e) {
      alert('舍弃失败');
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
      <div className="flex justify-between items-center mb-6 border-b pb-4">
        <h3 className="text-lg font-medium text-gray-900">上传账单文件</h3>
        <button onClick={onClose} className="text-gray-500 hover:text-gray-700 flex items-center text-sm">
          <X size={16} className="mr-1" /> 返回导入记录
        </button>
      </div>

      {batchPreviews.length === 0 ? (
        <div className="space-y-6 max-w-2xl">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">渠道类型</label>
              <select
                value={selectedChannel}
                onChange={e => {
                  setSelectedChannel(e.target.value);
                  setAccountId(undefined);
                  setFiles(null);
                }}
                className="w-full border-gray-300 rounded-md shadow-sm p-2.5 border focus:ring-blue-500 focus:border-blue-500"
              >
                {channelOptions.map(c => (
                  <option key={c} value={c}>
                    {c}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">关联本方账户</label>
              <select
                value={accountId || ''}
                onChange={e => setAccountId(Number(e.target.value))}
                className="w-full border-gray-300 rounded-md shadow-sm p-2.5 border focus:ring-blue-500 focus:border-blue-500"
              >
                <option value="">请选择导入目标账户</option>
                {filteredAccounts.map(acc => (
                  <option key={acc.id} value={acc.id}>
                    {acc.accountName} ({acc.accountType})
                  </option>
                ))}
              </select>
            </div>
            <div className="col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-2">解析器</label>
              <select
                value={parserId}
                onChange={e => setParserId(e.target.value)}
                className="w-full border-gray-300 rounded-md shadow-sm p-2.5 border focus:ring-blue-500 focus:border-blue-500"
                disabled={!accountId || filteredParsers.length === 0}
              >
                {filteredParsers.map(p => (
                  <option key={p.id} value={p.id}>
                    {p.name} ({(p.fileType || '').toUpperCase()}) {p.isBuiltIn ? '(内置)' : '(自定义)'}
                  </option>
                ))}
              </select>
              {!accountId && <p className="text-xs text-gray-500 mt-1">请先选择账户，再选择解析器。</p>}
              {accountId && filteredParsers.length === 0 && (
                <p className="text-xs text-gray-500 mt-1">当前渠道下暂无可用解析器，请先在“解析器配置”中创建并发布。</p>
              )}
              {selectedFileTypes.size > 1 && (
                <p className="text-xs text-amber-600 mt-1">已选择多种文件类型，未按文件类型过滤解析器。</p>
              )}
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">选择账单文件（可多选）</label>
            <div className="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md hover:border-blue-500 transition-colors bg-gray-50">
              <div className="space-y-1 text-center">
                <UploadCloud className="mx-auto h-12 w-12 text-gray-400" />
                <div className="flex text-sm text-gray-600 justify-center">
                  <label className="relative cursor-pointer bg-transparent rounded-md font-medium text-blue-600 hover:text-blue-500 focus-within:outline-none">
                    <span>选择多个文件</span>
                    <input
                      type="file"
                      multiple
                      className="sr-only"
                      onChange={e => setFiles(e.target.files)}
                      accept={fileAccept}
                      disabled={!accountId || !parserId}
                    />
                  </label>
                </div>
                {files && <p className="text-sm font-medium text-green-600 mt-2">已选择 {files.length} 个文件</p>}
                {!accountId || !parserId ? (
                  <p className="text-xs text-gray-500">选择账户与解析器后再上传文件。</p>
                ) : (
                  <p className="text-xs text-gray-500">支持 {fileAccept}。</p>
                )}
              </div>
            </div>
          </div>

          <div className="pt-4 flex justify-end">
            <button
              onClick={handleUploadBatch}
              disabled={isUploading || !files || !accountId || !parserId}
              className="flex items-center px-6 py-2.5 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
            >
              {isUploading ? (
                <>
                  <Loader2 className="animate-spin mr-2" size={18} />
                  解析中...
                </>
              ) : (
                '上传并解析'
              )}
            </button>
          </div>
        </div>
      ) : (
        <div className="flex flex-col">
          <div className="mb-4">
            <h4 className="text-md font-medium text-gray-900 mb-2">解析结果预览（共 {batchPreviews.length} 个文件）</h4>
            <div className="flex space-x-2 border-b border-gray-200">
              {batchPreviews.map((preview, idx) => (
                <button
                  key={preview.uploadId}
                  onClick={() => setActivePreviewTab(idx)}
                  className={`px-4 py-2 text-sm font-medium border-b-2 ${
                    activePreviewTab === idx
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  {preview.fileName} ({preview.parsedCount}条)
                </button>
              ))}
            </div>
          </div>

          {batchPreviews.length > 0 && batchPreviews[activePreviewTab] && (
            <div className="overflow-auto border rounded-lg max-h-[500px]">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50 sticky top-0 z-10">
                  <tr>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">时间</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">说明/对方</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">收支</th>
                    <th className="px-4 py-2 text-right text-xs font-medium text-gray-500">金额</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-100">
                  {batchPreviews[activePreviewTab].previewRecords.slice(0, 100).map(r => (
                    <tr key={r.tempId}>
                      <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-600">{r.transactionTime}</td>
                      <td className="px-4 py-2 text-sm text-gray-900 truncate max-w-[200px]">{r.counterparty}</td>
                      <td className="px-4 py-2 whitespace-nowrap text-sm">{r.type}</td>
                      <td className="px-4 py-2 whitespace-nowrap text-right text-sm font-medium">{r.amount}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          <div className="mt-4 flex justify-between items-center bg-gray-50 p-4 rounded-lg">
            <span className="text-sm text-gray-500">仅展示前 100 条预览。如解析结果混乱，可舍弃该文件。</span>
            <div className="space-x-3">
              {batchPreviews.length > 0 && batchPreviews[activePreviewTab] && (
                <button
                  onClick={() => handleDiscardFile(batchPreviews[activePreviewTab].uploadId, activePreviewTab)}
                  className="px-4 py-2 border border-red-300 text-red-600 rounded-md hover:bg-red-50 text-sm"
                >
                  舍弃此文件（解析错误）
                </button>
              )}
              <button onClick={onClose} className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 text-sm">
                保留结果并完成
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

