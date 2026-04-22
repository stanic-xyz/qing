import {useEffect, useMemo, useState} from 'react';
import axios from 'axios';
import {Loader2, UploadCloud, X} from 'lucide-react';
import type {Account, ParserItem, UploadBatchPreviewResponse, UploadBatchOverview} from '../types';

interface UploadViewProps {
    accounts: Account[];
    onClose: () => void;
}

export default function UploadView({accounts, onClose}: UploadViewProps) {
    const [files, setFiles] = useState<FileList | null>(null);
    const [parsers, setParsers] = useState<ParserItem[]>([]);
    const [filteredParsers, setFilteredParsers] = useState<ParserItem[]>([]);
    const [parserId, setParserId] = useState('');

    const [accountId, setAccountId] = useState<number | undefined>(undefined);

    const [isUploading, setIsUploading] = useState(false);
    const [batchPreviews, setBatchPreviews] = useState<UploadBatchPreviewResponse[]>([]);
    const [batchOverviews, setBatchOverviews] = useState<Map<string, UploadBatchOverview>>(new Map());
    const [activePreviewTab, setActivePreviewTab] = useState(0);

    // Memoized current preview to avoid re-computation on each render
    const currentPreview = useMemo(() => {
        return batchPreviews.length > 0 && batchPreviews[activePreviewTab]
            ? batchPreviews[activePreviewTab]
            : null;
    }, [batchPreviews, activePreviewTab]);

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
        const selectedAccount = accounts.find(a => a.id === accountId);
        const activeChannelId = selectedAccount?.channelDto?.id;

        let next = activeChannelId ? parsers.filter(p => p.channel?.id === activeChannelId) : [];

        if (selectedFileTypes.size === 1) {
            const onlyType = Array.from(selectedFileTypes)[0];
            const typed = next.filter(p => {
                const pt = (p.fileType || '').toUpperCase();
                if (onlyType === 'EXCEL') return pt === 'EXCEL' || pt === 'XLS' || pt === 'XLSX';
                return pt === onlyType;
            });
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
    }, [parsers, accountId, selectedFileTypes, accounts]);

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
        if (!accountId) return alert('请先选择关联账户');
        if (!parserId) return alert('请先选择解析器');

        setIsUploading(true);
        const formData = new FormData();
        for (const f of Array.from(files)) formData.append('files', f);
        formData.append('parserId', parserId);
        formData.append('accountId', String(accountId));

        try {
            const res = await axios.post('/api/bills/upload-batch', formData, {
                headers: {'Content-Type': 'multipart/form-data'}
            });
            const previews = res.data.data || [];
            setBatchPreviews(previews);
            setActivePreviewTab(0);

            // Fetch overview data for each uploaded file
            const overviews = new Map<string, UploadBatchOverview>();
            for (const preview of previews) {
                try {
                    const overviewRes = await axios.get(`/api/bills/batch/overview/${preview.uploadId}`);
                    if (overviewRes.data?.data) {
                        overviews.set(preview.uploadId, overviewRes.data.data);
                    }
                } catch (e) {
                    console.error('Failed to fetch overview for', preview.uploadId, e);
                }
            }
            setBatchOverviews(overviews);
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
            // Also remove from overviews map
            const newOverviews = new Map(batchOverviews);
            newOverviews.delete(uploadId);
            setBatchOverviews(newOverviews);
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
                    <X size={16} className="mr-1"/> 返回导入记录
                </button>
            </div>

            {batchPreviews.length === 0 ? (
                <div className="space-y-6 max-w-2xl">
                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">关联本方账户</label>
                            <select
                                value={accountId || ''}
                                onChange={e => setAccountId(Number(e.target.value))}
                                className="w-full border-gray-300 rounded-md shadow-sm p-2.5 border focus:ring-blue-500 focus:border-blue-500"
                            >
                                <option value="">请选择导入目标账户</option>
                                {accounts.map(acc => (
                                    <option key={acc.id} value={acc.id}>
                                        {acc.accountName} [{acc.channelDto?.name || '未知渠道'}]
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div>
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
                        <div
                            className="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md hover:border-blue-500 transition-colors bg-gray-50">
                            <div className="space-y-1 text-center">
                                <UploadCloud className="mx-auto h-12 w-12 text-gray-400"/>
                                <div className="flex text-sm text-gray-600 justify-center">
                                    <label
                                        className="relative cursor-pointer bg-transparent rounded-md font-medium text-blue-600 hover:text-blue-500 focus-within:outline-none">
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
                                {files &&
                                    <p className="text-sm font-medium text-green-600 mt-2">已选择 {files.length} 个文件</p>}
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
                                    <Loader2 className="animate-spin mr-2" size={18}/>
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

                    {currentPreview && (
                        <div className="border rounded-lg p-6 bg-gray-50">
                            <div className="flex items-start mb-4">
                                <div className="flex items-center justify-center w-12 h-12 rounded-full bg-green-100 mr-4">
                                    <span className="text-green-600 text-xl">✓</span>
                                </div>
                                <div>
                                    <h3 className="text-lg font-medium text-gray-900">解析完成</h3>
                                    <p className="text-sm text-gray-500 mt-1">
                                        文件：{batchOverviews.get(currentPreview.uploadId)?.fileName || currentPreview.fileName}
                                        {batchOverviews.get(currentPreview.uploadId)?.fileSize && (
                                            <span> | 大小：{(batchOverviews.get(currentPreview.uploadId)!.fileSize / 1024 / 1024).toFixed(2)} MB</span>
                                        )}
                                    </p>
                                </div>
                            </div>

                            <div className="border-t border-gray-200 my-4 pt-4">
                                <h4 className="text-sm font-medium text-gray-700 mb-3">解析统计</h4>
                                <div className="grid grid-cols-2 gap-4">
                                    <div className="bg-white rounded-lg p-4 border">
                                        <p className="text-xs text-gray-500 mb-1">总收入</p>
                                        <p className="text-lg font-semibold text-green-600">
                                            +{batchOverviews.get(currentPreview.uploadId)?.totalIncome?.toLocaleString() || '0'}
                                        </p>
                                        <p className="text-xs text-gray-400 mt-1">
                                            {batchOverviews.get(currentPreview.uploadId)?.incomeCount || 0} 笔
                                        </p>
                                    </div>
                                    <div className="bg-white rounded-lg p-4 border">
                                        <p className="text-xs text-gray-500 mb-1">总支出</p>
                                        <p className="text-lg font-semibold text-red-600">
                                            -{batchOverviews.get(currentPreview.uploadId)?.totalExpense?.toLocaleString() || '0'}
                                        </p>
                                        <p className="text-xs text-gray-400 mt-1">
                                            {batchOverviews.get(currentPreview.uploadId)?.expenseCount || 0} 笔
                                        </p>
                                    </div>
                                </div>
                                <div className="mt-3 flex items-center text-sm text-gray-500">
                                    <span className="mr-4">总交易笔数：{currentPreview.parsedCount} 条</span>
                                    <span className="mr-4">转账：{batchOverviews.get(currentPreview.uploadId)?.transferCount || 0} 笔</span>
                                    <span>账户：{batchOverviews.get(currentPreview.uploadId)?.accountName || '-'}</span>
                                </div>
                            </div>

                            <div className="border-t border-gray-200 my-4 pt-4">
                                <div className="flex items-center text-sm text-gray-500">
                                    <span className="mr-6">批次信息：{batchOverviews.get(currentPreview.uploadId)?.batchCount || 0} 个批次</span>
                                    {batchOverviews.get(currentPreview.uploadId)?.transactionStartTime && (
                                        <span>时间范围：{new Date(batchOverviews.get(currentPreview.uploadId)!.transactionStartTime).toLocaleDateString()} ~ {new Date(batchOverviews.get(currentPreview.uploadId)!.transactionEndTime!).toLocaleDateString()}</span>
                                    )}
                                </div>
                            </div>

                            <div className="mt-6 flex justify-end space-x-3">
                                {currentPreview && (
                                    <button
                                        onClick={() => handleDiscardFile(currentPreview.uploadId, activePreviewTab)}
                                        className="px-4 py-2 border border-red-300 text-red-600 rounded-md hover:bg-red-50 text-sm"
                                    >
                                        舍弃此文件（解析错误）
                                    </button>
                                )}
                                <button onClick={onClose}
                                        className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 text-sm">
                                    前往处理
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

