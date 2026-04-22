import { useState, useEffect, useMemo } from 'react';
import axios from 'axios';
import { ArrowLeft, Search, Loader2, Play, Check, Pin, FlaskConical, X, Filter, Edit2 } from 'lucide-react';
import type { PreviewRecord, UploadBatchOverview, Account } from './types';

interface ImportDetailProps {
    uploadId: string;
    initialStatus?: 'UPLOADED' | 'MATCHING' | 'IMPORTED';
    accounts: Account[];
    selectedRuleIds: Set<number>;
    onClose: () => void;
    onImportSuccess: () => void;
}

type FilterStatus = 'ALL' | 'MATCHED' | 'UNMATCHED' | 'SUSPICIOUS';

export default function ImportDetail({
    uploadId,
    initialStatus = 'UPLOADED',
    accounts,
    selectedRuleIds,
    onClose,
    onImportSuccess
}: ImportDetailProps) {
    const [processStep, setProcessStep] = useState<number>(initialStatus === 'MATCHING' ? 2 : 1);
    const [processPreview, setProcessPreview] = useState<{ uploadId: string; previewRecords: PreviewRecord[] } | null>(null);
    const [isMatching, setIsMatching] = useState(false);
    const [isImporting, setIsImporting] = useState(false);
    const [modifiedRecords, setModifiedRecords] = useState<Record<string, any>>({});
    const [lockedTempIds, setLockedTempIds] = useState<Set<string>>(new Set());
    const [loading, setLoading] = useState(true);
    const [batchOverview, setBatchOverview] = useState<UploadBatchOverview | null>(null);

    // 筛选状态
    const [filterStatus, setFilterStatus] = useState<FilterStatus>('ALL');
    const [searchKeyword, setSearchKeyword] = useState('');

    useEffect(() => {
        fetchPreview();
    }, [uploadId]);

    const fetchPreview = async () => {
        setLoading(true);
        try {
            const res = await axios.get(`/api/bills/preview/${uploadId}`);
            setProcessPreview(res.data.data);
            // 如果状态是 MATCHING，直接跳到第二步
            if (res.data.data.status === 'MATCHING') {
                setProcessStep(2);
            }
            // Fetch batch overview
            try {
                const overviewRes = await axios.get(`/api/bills/batch/overview/${uploadId}`);
                if (overviewRes.data?.data) {
                    setBatchOverview(overviewRes.data.data);
                }
            } catch (e) {
                console.error('Failed to fetch batch overview', e);
            }
        } catch (e) {
            alert('获取预览数据失败');
        } finally {
            setLoading(false);
        }
    };

    const handleStartMatch = async () => {
        setIsMatching(true);
        try {
            await axios.post(`/api/bills/match/${uploadId}`, Array.from(lockedTempIds));
            const pollInterval = setInterval(async () => {
                try {
                    const res = await axios.get(`/api/bills/match/status/${uploadId}`);
                    if (res.data.code === 200) {
                        const statusData = res.data.data;
                        if (statusData.status === 'COMPLETED') {
                            clearInterval(pollInterval);
                            setProcessPreview(statusData.preview);
                            setIsMatching(false);
                            setProcessStep(2);
                        } else if (statusData.status === 'FAILED') {
                            clearInterval(pollInterval);
                            setIsMatching(false);
                            alert('匹配失败: ' + statusData.errorMsg);
                        }
                    }
                } catch (e) {
                    console.error(e);
                }
            }, 1500);
        } catch (e: any) {
            setIsMatching(false);
            alert('启动匹配失败: ' + (e.response?.data?.message || e.message));
        }
    };

    const handleReMatch = async () => {
        setProcessStep(1);
        setModifiedRecords({});
        setLockedTempIds(new Set());
        await handleStartMatch();
    };

    const handleTestSingleMatch = async (tempId: string) => {
        try {
            const ruleIdsArray = Array.from(selectedRuleIds);
            const res = await axios.post(`/api/bills/match-single/${tempId}`, ruleIdsArray);
            if (res.data.code === 200) {
                const newRecord = res.data.data;
                setProcessPreview(prev => {
                    if (!prev) return prev;
                    const newRecords = prev.previewRecords.map(r => r.tempId === tempId ? newRecord : r);
                    return { ...prev, previewRecords: newRecords };
                });
                setModifiedRecords(prev => {
                    const next = { ...prev };
                    delete next[tempId];
                    return next;
                });

                if (newRecord.matchRuleName) {
                    alert(`单条测试完成：成功命中规则 [${newRecord.matchRuleName}]`);
                } else {
                    alert('单条测试完成：未命中任何规则，保持原始状态。');
                }
            }
        } catch (e: any) {
            alert('测试失败: ' + (e.response?.data?.message || e.message));
        }
    };

    const handleConfirmImport = async () => {
        if (!processPreview) return;
        setIsImporting(true);
        try {
            const tempIds = processPreview.previewRecords.map((r: any) => r.tempId);
            const modifications = Object.keys(modifiedRecords).map(tempId => {
                const mod = modifiedRecords[tempId];
                return {
                    tempId,
                    type: mod.type || processPreview.previewRecords.find(r => r.tempId === tempId)?.type,
                    merchant: mod.merchant || processPreview.previewRecords.find(r => r.tempId === tempId)?.merchant,
                    targetAccountId: mod.targetAccountId || processPreview.previewRecords.find(r => r.tempId === tempId)?.targetAccountId,
                };
            }).filter(m => Object.keys(modifiedRecords[m.tempId]).length > 0);

            await axios.post('/api/bills/import', {
                uploadId: uploadId,
                confirmedTempIds: tempIds,
                modifications: modifications.length > 0 ? modifications : undefined
            });

            setProcessStep(3);  // Mark as import completed
            alert(`成功导入！`);
            onImportSuccess();
            onClose();
        } catch (e: any) {
            console.error(e);
            alert('导入失败: ' + (e.response?.data?.message || e.message));
        } finally {
            setIsImporting(false);
        }
    };

    const toggleLock = (tempId: string) => {
        setLockedTempIds(prev => {
            const next = new Set(prev);
            if (next.has(tempId)) {
                next.delete(tempId);
            } else {
                next.add(tempId);
            }
            return next;
        });
    };

    const handleRecordChange = (tempId: string, field: string, value: any) => {
        setModifiedRecords(prev => {
            const current = prev[tempId] || {};
            const updated = { ...current, [field]: value };
            if (field === 'type' && value !== 'TRANSFER') {
                updated.targetAccountId = undefined;
            }
            return { ...prev, [tempId]: updated };
        });
    };

    const channel = processPreview?.previewRecords[0]?.channel;
    const getDynamicColumns = (ch?: string) => {
        switch (ch) {
            case 'ALIPAY':
            case 'JINGDONG':
                return [
                    { key: 'paymentMethod', label: '付款方式', width: 'w-24' },
                    { key: 'merchantOrderNo', label: '商家订单号', width: 'w-32' },
                ];
            case 'CITIC_CREDIT':
                return [
                    { key: 'cardLast4', label: '卡号', width: 'w-20' },
                    { key: 'billingAmount', label: '结算金额', width: 'w-24' },
                ];
            default:
                return [];
        }
    };
    const dynamicColumns = getDynamicColumns(channel);

    const renderMatchStatus = (status: string, ruleName?: string) => {
        switch (status) {
            case 'AUTO_MATCHED':
                return <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-green-100 text-green-800" title={ruleName}>🟢 自动匹配 {ruleName && `(${ruleName})`}</span>;
            case 'INTERNAL_TRANSFER':
                return <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-blue-100 text-blue-800" title={ruleName}>🔵 内部转账 {ruleName && `(${ruleName})`}</span>;
            case 'MANUAL_EDITED':
                return <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-purple-100 text-purple-800">🟣 手动修改</span>;
            case 'SUSPICIOUS':
                return <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-yellow-100 text-yellow-800" title={ruleName}>🟡 存疑</span>;
            case 'ORIGINAL':
            default:
                return <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-gray-100 text-gray-800">⚪ 原始数据</span>;
        }
    };

    // 筛选后的记录
    const filteredRecords = useMemo(() => {
        if (!processPreview?.previewRecords) return [];

        let result = processPreview.previewRecords;

        // 按状态筛选
        if (filterStatus === 'MATCHED') {
            result = result.filter(r => ['AUTO_MATCHED', 'INTERNAL_TRANSFER'].includes(r.matchStatus));
        } else if (filterStatus === 'UNMATCHED') {
            result = result.filter(r => r.matchStatus === 'ORIGINAL');
        } else if (filterStatus === 'SUSPICIOUS') {
            result = result.filter(r => r.matchStatus === 'SUSPICIOUS');
        }

        // 按关键字搜索
        if (searchKeyword) {
            const kw = searchKeyword.toLowerCase();
            result = result.filter(r =>
                r.counterparty?.toLowerCase().includes(kw) ||
                r.merchant?.toLowerCase().includes(kw) ||
                r.matchRuleName?.toLowerCase().includes(kw)
            );
        }

        return result;
    }, [processPreview?.previewRecords, filterStatus, searchKeyword]);

    // 统计各状态数量
    const statusCounts = useMemo(() => {
        if (!processPreview?.previewRecords) return { all: 0, matched: 0, unmatched: 0, suspicious: 0 };
        const records = processPreview.previewRecords;
        return {
            all: records.length,
            matched: records.filter(r => ['AUTO_MATCHED', 'INTERNAL_TRANSFER'].includes(r.matchStatus)).length,
            unmatched: records.filter(r => r.matchStatus === 'ORIGINAL').length,
            suspicious: records.filter(r => r.matchStatus === 'SUSPICIOUS').length,
        };
    }, [processPreview?.previewRecords]);

    if (loading) {
        return (
            <div className="fixed inset-0 bg-white z-50 flex items-center justify-center">
                <Loader2 className="animate-spin text-blue-600" size={40} />
            </div>
        );
    }

    return (
        <div className="fixed inset-0 bg-gray-100 z-50 flex flex-col">
            {/* 顶部栏 */}
            <div className="bg-white shadow-sm border-b border-gray-200 px-6 py-4 flex items-center justify-between">
                <div className="flex items-center space-x-4">
                    <button onClick={onClose} className="flex items-center text-gray-600 hover:text-gray-800">
                        <ArrowLeft size={20} className="mr-2" />
                        返回列表
                    </button>
                    <div>
                        <h1 className="text-xl font-bold text-gray-900">导入详情</h1>
                        <p className="text-sm text-gray-500">共 {processPreview?.previewRecords?.length || 0} 条记录</p>
                    </div>
                </div>
            </div>

            {/* 步骤指引 */}
            <div className="bg-white border-b border-gray-200 px-6 py-3">
                <div className="flex items-center space-x-2">
                    <span className={`flex items-center text-sm font-medium ${processStep >= 1 ? 'text-green-600' : 'text-gray-400'}`}>
                        <span className={`w-6 h-6 rounded-full flex items-center justify-center text-xs mr-2 ${processStep >= 1 ? 'bg-green-100' : 'bg-gray-200'}`}>①</span>
                        解析完成 ✓
                    </span>
                    <span className="text-gray-300 mx-2">→</span>
                    <span className={`flex items-center text-sm font-medium ${processStep >= 2 ? (processStep === 2 && isMatching ? 'text-blue-600' : 'text-green-600') : 'text-gray-400'}`}>
                        <span className={`w-6 h-6 rounded-full flex items-center justify-center text-xs mr-2 ${processStep >= 2 ? (processStep === 2 && isMatching ? 'bg-blue-100' : 'bg-green-100') : 'bg-gray-200'}`}>②</span>
                        匹配中 {processStep === 2 && isMatching ? '●' : processStep > 2 ? '✓' : '○'}
                    </span>
                    <span className="text-gray-300 mx-2">→</span>
                    <span className={`flex items-center text-sm font-medium ${processStep >= 3 ? 'text-green-600' : 'text-gray-400'}`}>
                        <span className={`w-6 h-6 rounded-full flex items-center justify-center text-xs mr-2 ${processStep >= 3 ? 'bg-green-100' : 'bg-gray-200'}`}>③</span>
                        导入完成 {processStep >= 3 ? '✓' : '○'}
                    </span>
                    {batchOverview && (
                        <span className="ml-6 text-sm text-gray-500">
                            批次概览：{batchOverview.batchCount} 个批次 | 账户：{batchOverview.accountName}
                        </span>
                    )}
                </div>
            </div>

            {/* 内容区 */}
            <div className="flex-1 overflow-auto p-6">
                <div className="bg-gray-50 p-6 border-l-4 border-blue-500 shadow-inner relative">
                    {isMatching && (
                        <div className="absolute inset-0 bg-white/70 backdrop-blur-sm z-20 flex flex-col items-center justify-center">
                            <Loader2 className="animate-spin text-blue-600 mb-4" size={40}/>
                            <span className="font-medium text-blue-800">正在应用匹配规则...</span>
                        </div>
                    )}

                    <div className="flex justify-between items-center mb-4">
                        <div>
                            <h4 className="font-bold text-gray-800">
                                {processStep === 1 ? '第一步：原始数据' : '第二步：匹配结果确认'}
                            </h4>
                            <p className="text-sm text-gray-500">
                                {processStep === 1
                                    ? '数据已解析完成，您可以进入第二步进行智能匹配。'
                                    : '筛选并确认匹配结果后可导入。'}
                            </p>
                        </div>
                    </div>

                    {/* 筛选栏 - 仅第二步显示 */}
                    {processStep === 2 && (
                        <div className="bg-white border rounded-lg p-4 mb-4 flex items-center space-x-4">
                            <div className="flex items-center">
                                <Filter size={16} className="text-gray-400 mr-2" />
                                <span className="text-sm text-gray-600 mr-2">筛选:</span>
                            </div>
                            <select
                                value={filterStatus}
                                onChange={(e) => setFilterStatus(e.target.value as FilterStatus)}
                                className="border-gray-300 rounded-md shadow-sm p-2 text-sm border focus:ring-blue-500 focus:border-blue-500"
                            >
                                <option value="ALL">全部 ({statusCounts.all})</option>
                                <option value="MATCHED">已匹配 ({statusCounts.matched})</option>
                                <option value="UNMATCHED">未匹配 ({statusCounts.unmatched})</option>
                                <option value="SUSPICIOUS">存疑 ({statusCounts.suspicious})</option>
                            </select>
                            <div className="flex-1 relative">
                                <Search size={16} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
                                <input
                                    type="text"
                                    value={searchKeyword}
                                    onChange={(e) => setSearchKeyword(e.target.value)}
                                    placeholder="搜索对方/商户/规则名..."
                                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md text-sm focus:ring-blue-500 focus:border-blue-500"
                                />
                                {searchKeyword && (
                                    <button
                                        onClick={() => setSearchKeyword('')}
                                        className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600"
                                    >
                                        <X size={14} />
                                    </button>
                                )}
                            </div>
                            <span className="text-sm text-gray-500">
                                显示 {filteredRecords.length} 条记录
                            </span>
                            <div className="flex items-center space-x-3 ml-4">
                                <button
                                    onClick={handleReMatch}
                                    disabled={isMatching || isImporting}
                                    className="flex items-center px-4 py-2 border border-blue-600 text-blue-600 rounded-md hover:bg-blue-50 text-sm disabled:opacity-50"
                                >
                                    <Play size={16} className="mr-1" />重新匹配
                                </button>
                                <button
                                    onClick={handleConfirmImport}
                                    disabled={isImporting || isMatching}
                                    className="flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 text-sm disabled:opacity-50"
                                >
                                    {isImporting ? <><Loader2 className="animate-spin mr-1" size={16}/>导入中</> : <><Check size={16} className="mr-1" />确认导入</>}
                                </button>
                            </div>
                        </div>
                    )}

                    {/* 进入第二步按钮 - 仅第一步显示 */}
                    {processStep === 1 && (
                        <div className="bg-white border rounded-lg p-4 mb-4 flex justify-center">
                            <button
                                onClick={() => {
                                    setProcessStep(2);
                                }}
                                className="flex items-center px-6 py-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 text-base font-medium shadow-md"
                            >
                                <Play size={20} className="mr-2" fill="currentColor" />
                                开始匹配
                            </button>
                        </div>
                    )}

                    <div className="bg-white border rounded-lg overflow-hidden max-h-[600px] overflow-x-auto overflow-y-auto">
                        <table className="min-w-full divide-y divide-gray-200 table-fixed">
                            <thead className="bg-gray-100 sticky top-0 z-10">
                                <tr>
                                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-28">状态</th>
                                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">时间</th>
                                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-48">说明/对方</th>
                                    {dynamicColumns.map(col => (
                                        <th key={col.key} className={`px-4 py-2 text-left text-xs font-medium text-gray-500 ${col.width}`}>{col.label}</th>
                                    ))}
                                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">
                                        提取商户 {processStep === 2 && <Edit2 size={12} className="inline text-blue-500"/>}
                                    </th>
                                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-24">
                                        收支 {processStep === 2 && <Edit2 size={12} className="inline text-blue-500"/>}
                                    </th>
                                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">
                                        转账目标 {processStep === 2 && <Edit2 size={12} className="inline text-blue-500"/>}
                                    </th>
                                    <th className="px-4 py-2 text-right text-xs font-medium text-gray-500 w-32 sticky right-0 bg-gray-100 z-10">操作</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100 bg-white">
                                {filteredRecords.map((record) => {
                                    const mod = modifiedRecords[record.tempId] || {};
                                    const currentType = mod.type || record.type;
                                    const currentMerchant = mod.merchant !== undefined ? mod.merchant : record.merchant;
                                    const currentTargetAccount = mod.targetAccountId !== undefined ? mod.targetAccountId : record.targetAccountId;
                                    const isModified = Object.keys(mod).length > 0;
                                    const isLocked = lockedTempIds.has(record.tempId);

                                    return (
                                        <tr key={record.tempId} className={`hover:bg-blue-50/20 ${isModified ? 'bg-yellow-50/20' : ''} ${isLocked ? 'bg-gray-50 opacity-80' : ''}`}>
                                            <td className="px-4 py-2 whitespace-nowrap truncate" title={record.matchRuleName || record.matchStatus}>
                                                {renderMatchStatus(processStep === 1 ? (record.matchStatus || 'ORIGINAL') : (isModified ? 'MANUAL_EDITED' : record.matchStatus), record.matchRuleName)}
                                            </td>
                                            <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-600 truncate" title={record.transactionTime}>{record.transactionTime}</td>
                                            <td className="px-4 py-2 text-sm text-gray-900 truncate" title={record.counterparty}>{record.counterparty}</td>

                                            {dynamicColumns.map(col => (
                                                <td key={col.key} className="px-4 py-2 whitespace-nowrap text-sm text-gray-500 truncate" title={record.extData?.[col.key] || '-'}>
                                                    {record.extData?.[col.key] || '-'}
                                                </td>
                                            ))}

                                            <td className="px-4 py-2 whitespace-nowrap truncate">
                                                {processStep === 2 ? (
                                                    <input
                                                        type="text"
                                                        value={currentMerchant || ''}
                                                        onChange={e => handleRecordChange(record.tempId, 'merchant', e.target.value)}
                                                        className="text-sm border-gray-300 rounded px-2 py-1 w-full border focus:ring-blue-500"
                                                        placeholder="未提取"
                                                    />
                                                ) : (
                                                    <span className="text-sm text-gray-500">{record.merchant || '-'}</span>
                                                )}
                                            </td>

                                            <td className="px-4 py-2 whitespace-nowrap">
                                                {processStep === 2 ? (
                                                    <select
                                                        value={currentType}
                                                        onChange={e => handleRecordChange(record.tempId, 'type', e.target.value)}
                                                        className={`text-sm border-gray-300 rounded px-2 py-1 border focus:ring-blue-500 w-full ${currentType === 'INCOME' ? 'text-green-600' : currentType === 'EXPENSE' ? 'text-red-600' : 'text-blue-600'}`}
                                                    >
                                                        <option value="EXPENSE">支出</option>
                                                        <option value="INCOME">收入</option>
                                                        <option value="TRANSFER">转账</option>
                                                    </select>
                                                ) : (
                                                    <span className={`text-sm ${record.type === 'INCOME' ? 'text-green-600' : record.type === 'EXPENSE' ? 'text-red-600' : 'text-gray-600'}`}>{record.type}</span>
                                                )}
                                            </td>

                                            <td className="px-4 py-2 whitespace-nowrap truncate">
                                                {processStep === 2 && currentType === 'TRANSFER' ? (
                                                    <select
                                                        value={currentTargetAccount || ''}
                                                        onChange={e => handleRecordChange(record.tempId, 'targetAccountId', e.target.value ? Number(e.target.value) : undefined)}
                                                        className="text-sm border-gray-300 rounded px-2 py-1 w-full border focus:ring-blue-500"
                                                    >
                                                        <option value="">选择目标</option>
                                                        {accounts.map(a => <option key={a.id} value={a.id} title={a.accountName}>{a.accountName}</option>)}
                                                    </select>
                                                ) : (
                                                    <span className="text-gray-400 text-sm">-</span>
                                                )}
                                            </td>

                                            <td className="px-4 py-2 whitespace-nowrap text-right space-x-2 sticky right-0 bg-inherit shadow-[-4px_0_6px_-4px_rgba(0,0,0,0.1)]">
                                                {processStep === 1 && (
                                                    <>
                                                        <button onClick={() => handleTestSingleMatch(record.tempId)} className="text-blue-600 hover:text-blue-800 text-xs flex items-center inline-flex" title="单条测试">
                                                            <FlaskConical size={14} className="mr-0.5"/>测试
                                                        </button>
                                                        <button onClick={() => toggleLock(record.tempId)} className={`${isLocked ? 'text-orange-500' : 'text-gray-400 hover:text-gray-600'} text-xs flex items-center inline-flex`} title="固定后全局重匹将忽略此条">
                                                            <Pin size={14} className="mr-0.5" fill={isLocked ? 'currentColor' : 'none'}/> {isLocked ? '已固定' : '固定'}
                                                        </button>
                                                    </>
                                                )}
                                            </td>
                                        </tr>
                                    );
                                })}
                                {filteredRecords.length === 0 && (
                                    <tr>
                                        <td colSpan={8} className="px-4 py-8 text-center text-gray-500">
                                            {searchKeyword || filterStatus !== 'ALL' ? '没有符合筛选条件的记录' : '暂无数据'}
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}
