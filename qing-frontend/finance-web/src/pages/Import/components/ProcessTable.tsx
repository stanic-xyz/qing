import React from 'react';
import axios from 'axios';
import { Edit2, Loader2, Play, Check, Pin, FlaskConical } from 'lucide-react';
import type { Account, PreviewRecord } from '../types';

interface ProcessTableProps {
  uploadId: string;
  processPreview: { uploadId: string; previewRecords: PreviewRecord[] };
  setProcessPreview: React.Dispatch<React.SetStateAction<{ uploadId: string; previewRecords: PreviewRecord[] } | null>>;
  accounts: Account[];
  selectedRuleIds: Set<number>;
  processStep: number;
  setProcessStep: React.Dispatch<React.SetStateAction<number>>;
  modifiedRecords: Record<string, any>;
  setModifiedRecords: React.Dispatch<React.SetStateAction<Record<string, any>>>;
  lockedTempIds: Set<string>;
  setLockedTempIds: React.Dispatch<React.SetStateAction<Set<string>>>;
  isMatching: boolean;
  setIsMatching: React.Dispatch<React.SetStateAction<boolean>>;
  isImporting: boolean;
  setIsImporting: React.Dispatch<React.SetStateAction<boolean>>;
  onImportSuccess: () => void;
}

export default function ProcessTable({
  uploadId, processPreview, setProcessPreview, accounts, selectedRuleIds,
  processStep, setProcessStep, modifiedRecords, setModifiedRecords,
  lockedTempIds, setLockedTempIds, isMatching, setIsMatching,
  isImporting, setIsImporting, onImportSuccess
}: ProcessTableProps) {

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

      alert(`成功导入！`);
      onImportSuccess();
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

  return (
    <div className="bg-gray-50 p-6 border-l-4 border-blue-500 shadow-inner relative">
      {isMatching && (
        <div className="absolute inset-0 bg-white/70 backdrop-blur-sm z-20 flex flex-col items-center justify-center">
          <Loader2 className="animate-spin text-blue-600 mb-4" size={40}/>
          <span className="font-medium text-blue-800">正在应用匹配规则...</span>
        </div>
      )}
      <div className="flex justify-between items-center mb-4">
        <div>
          <h4 className="font-bold text-gray-800">{processStep === 1 ? '第一步：原始数据与测试' : '第二步：匹配结果确认'}</h4>
          <p className="text-sm text-gray-500">
            {processStep === 1
              ? '点击“单条测试”可验证单条记录，或点击“开始匹配(处理数据)”全局处理（已锁定的记录将被忽略）。'
              : '您可以直接修改商户、收支或转账目标。确认无误后点击导入。'}
          </p>
        </div>
        <div className="flex space-x-3">
          {processStep === 1 ? (
            <button onClick={handleStartMatch} disabled={isMatching} className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 text-sm">
              <Play size={16} className="mr-1" fill="currentColor"/>开始匹配(处理数据)
            </button>
          ) : (
            <>
              <button onClick={() => setProcessStep(1)} className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-100 text-sm">
                返回修改规则
              </button>
              <button onClick={handleConfirmImport} disabled={isImporting} className="flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 text-sm">
                {isImporting ? <><Loader2 className="animate-spin mr-1" size={16}/>导入中</> : <><Check size={16} className="mr-1" />确认导入</>}
              </button>
            </>
          )}
        </div>
      </div>

    <div className="bg-white border rounded-lg overflow-hidden max-h-[600px] overflow-x-auto overflow-y-auto">
        <table className="min-w-full divide-y divide-gray-200 table-fixed">
          <thead className="bg-gray-100 sticky top-0 z-10">
            <tr>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-28">状态</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">时间</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-48">说明/对方</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">提取商户 {processStep===2 && <Edit2 size={12} className="inline text-blue-500"/>}</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-24">收支 {processStep===2 && <Edit2 size={12} className="inline text-blue-500"/>}</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">转账目标 {processStep===2 && <Edit2 size={12} className="inline text-blue-500"/>}</th>
              <th className="px-4 py-2 text-right text-xs font-medium text-gray-500 w-32 sticky right-0 bg-gray-100 z-10">操作</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100 bg-white">
            {processPreview.previewRecords.map((record) => {
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
                  
                  <td className="px-4 py-2 whitespace-nowrap truncate" title={currentMerchant || record.merchant || '-'}>
                    {processStep === 2 ? (
                      <input type="text" value={currentMerchant || ''} onChange={e => handleRecordChange(record.tempId, 'merchant', e.target.value)} className="text-sm border-gray-300 rounded px-2 py-1 w-full border focus:ring-blue-500" placeholder="未提取" />
                    ) : <span className="text-sm text-gray-500">{record.merchant || '-'}</span>}
                  </td>

                  <td className="px-4 py-2 whitespace-nowrap">
                    {processStep === 2 ? (
                      <select value={currentType} onChange={e => handleRecordChange(record.tempId, 'type', e.target.value)} className={`text-sm border-gray-300 rounded px-2 py-1 border focus:ring-blue-500 w-full ${currentType === 'INCOME' ? 'text-green-600' : currentType === 'EXPENSE' ? 'text-red-600' : 'text-blue-600'}`}>
                        <option value="EXPENSE">支出</option>
                        <option value="INCOME">收入</option>
                        <option value="TRANSFER">转账</option>
                      </select>
                    ) : <span className={`text-sm ${record.type === 'INCOME' ? 'text-green-600' : record.type === 'EXPENSE' ? 'text-red-600' : 'text-gray-600'}`}>{record.type}</span>}
                  </td>

                  <td className="px-4 py-2 whitespace-nowrap truncate">
                    {processStep === 2 && currentType === 'TRANSFER' ? (
                      <select value={currentTargetAccount || ''} onChange={e => handleRecordChange(record.tempId, 'targetAccountId', e.target.value ? Number(e.target.value) : undefined)} className="text-sm border-gray-300 rounded px-2 py-1 w-full border focus:ring-blue-500">
                        <option value="">选择目标</option>
                        {accounts.map(a => <option key={a.id} value={a.id} title={a.accountName}>{a.accountName}</option>)}
                      </select>
                    ) : <span className="text-gray-400 text-sm">-</span>}
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
          </tbody>
        </table>
      </div>
    </div>
  );
}