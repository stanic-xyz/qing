// 重写版 ProcessTable：使用 Zustand store，prop 接口大幅简化
import React, { useCallback, useEffect } from 'react';
import axios from 'axios';
import { Edit2, Loader2, Play, Check, Pin, FlaskConical } from 'lucide-react';
import { useImportStore } from '../../../store/importStore';
import type { PreviewRecord } from '../types';

interface ProcessTableProps {
  uploadId: string;
  preview: { uploadId: string; previewRecords: PreviewRecord[] };
  accounts: any[];
  selectedRuleIds: Set<number>;
  onImportSuccess: () => void;
}

export default function ProcessTable({
  uploadId, preview, accounts, selectedRuleIds, onImportSuccess,
}: ProcessTableProps) {
  const {
    rowStates, setRowState,
    previewMap, setPreview,
  } = useImportStore();

  const state = rowStates[uploadId];
  const processStep = state?.processStep ?? 1;
  const modifiedRecords = state?.modifiedRecords ?? {};
  const lockedTempIds = state?.lockedTempIds ?? [];
  const isMatching = state?.isMatching ?? false;
  const isImporting = state?.isImporting ?? false;

  // 更新本地记录
  const updatePreviewRecord = useCallback((tempId: string, updated: Partial<PreviewRecord>) => {
    const current = previewMap[uploadId];
    if (!current) return;
    const newRecords = current.previewRecords.map(r =>
      r.tempId === tempId ? { ...r, ...updated } : r
    );
    setPreview(uploadId, { ...current, previewRecords: newRecords });
  }, [uploadId, previewMap, setPreview]);

  // 更新 Store 中的字段
  const setField = <K extends keyof NonNullable<typeof state>>(
    key: K, value: NonNullable<typeof state>[K]
  ) => setRowState(uploadId, { [key]: value } as any);

  // ===== 开始匹配 =====
  const handleStartMatch = async () => {
    setField('isMatching', true);
    try {
      await axios.post(`/api/bills/match/${uploadId}`, lockedTempIds);
      pollMatchStatus();
    } catch (e: any) {
      setField('isMatching', false);
      alert('启动匹配失败: ' + (e.response?.data?.message || e.message));
    }
  };

  const pollMatchStatus = useCallback(() => {
    const interval = setInterval(async () => {
      try {
        const res = await axios.get(`/api/bills/match/status/${uploadId}`);
        if (res.data?.code === 200) {
          const s = res.data.data;
          if (s.status === 'COMPLETED') {
            clearInterval(interval);
            // 更新 preview（直接复用 previewMap）
            const current = previewMap[uploadId];
            if (current && s.preview?.previewRecords) {
              setPreview(uploadId, {
                ...current,
                previewRecords: s.preview.previewRecords,
              });
            }
            setField('isMatching', false);
            setField('processStep', 2);
          } else if (s.status === 'FAILED') {
            clearInterval(interval);
            setField('isMatching', false);
            alert('匹配失败: ' + (s.errorMsg || '未知错误'));
          }
        }
      } catch {
        // ignore polling errors
      }
    }, 1500);
  }, [uploadId, previewMap]);

  // ===== 单条测试 =====
  const handleTestSingle = async (tempId: string) => {
    try {
      const res = await axios.post(`/api/bills/match-single/${tempId}`, Array.from(selectedRuleIds));
      if (res.data?.code === 200) {
        const newRecord = res.data.data;
        updatePreviewRecord(tempId, newRecord);
        // 清除该记录的修改
        setField('modifiedRecords', Object.fromEntries(
          Object.entries(modifiedRecords).filter(([k]) => k !== tempId)
        ));
        alert(newRecord.matchRuleName
          ? `✅ 命中规则 [${newRecord.matchRuleName}]`
          : '未命中任何规则');
      }
    } catch (e: any) {
      alert('测试失败: ' + (e.response?.data?.message || e.message));
    }
  };

  // ===== 确认导入 =====
  const handleConfirmImport = async () => {
    setField('isImporting', true);
    try {
      const tempIds = preview.previewRecords.map((r: any) => r.tempId);
      const modifications = Object.entries(modifiedRecords)
        .filter(([, mod]) => Object.keys(mod as object).length > 0)
        .map(([tempId, mod]) => ({
          tempId,
          type: (mod as any).type || preview.previewRecords.find((r: any) => r.tempId === tempId)?.type,
          merchant: (mod as any).merchant || preview.previewRecords.find((r: any) => r.tempId === tempId)?.merchant,
          targetAccountId: (mod as any).targetAccountId,
        }));

      await axios.post('/api/bills/import', {
        uploadId,
        confirmedTempIds: tempIds,
        modifications: modifications.length > 0 ? modifications : undefined,
      });
      alert('导入成功！');
      onImportSuccess();
    } catch (e: any) {
      alert('导入失败: ' + (e.response?.data?.message || e.message));
    } finally {
      setField('isImporting', false);
    }
  };

  // 锁定/解锁
  const toggleLock = (tempId: string) => {
    const next = lockedTempIds.includes(tempId)
      ? lockedTempIds.filter(id => id !== tempId)
      : [...lockedTempIds, tempId];
    setField('lockedTempIds', next);
  };

  // 编辑记录
  const handleChange = (tempId: string, field: string, value: any) => {
    const current = (modifiedRecords[tempId] || {}) as any;
    const updated = { ...current, [field]: value };
    if (field === 'type' && value !== 'TRANSFER') {
      delete updated.targetAccountId;
    }
    setField('modifiedRecords', { ...modifiedRecords, [tempId]: updated });
  };

  const channel = preview?.previewRecords?.[0]?.channel;
  const dynamicColumns = getDynamicColumns(channel);

  const renderStatus = (status: string, ruleName?: string) => {
    const m: Record<string, { bg: string; text: string; icon: string; label: string }> = {
      AUTO_MATCHED: { bg: 'bg-green-100', text: 'text-green-800', icon: '🟢', label: '自动匹配' },
      INTERNAL_TRANSFER: { bg: 'bg-blue-100', text: 'text-blue-800', icon: '🔵', label: '内部转账' },
      MANUAL_EDITED: { bg: 'bg-purple-100', text: 'text-purple-800', icon: '🟣', label: '手动修改' },
      SUSPICIOUS: { bg: 'bg-yellow-100', text: 'text-yellow-800', icon: '🟡', label: '存疑' },
      ORIGINAL: { bg: 'bg-gray-100', text: 'text-gray-800', icon: '⚪', label: '原始' },
    };
    const s = m[status] || m['ORIGINAL'];
    return (
      <span className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-medium ${s.bg} ${s.text}`}>
        {s.icon} {s.label}{ruleName && `(${ruleName})`}
      </span>
    );
  };

  return (
    <div className="bg-gray-50 p-6 border-l-4 border-blue-500 shadow-inner relative">

      {/* 匹配中遮罩 */}
      {isMatching && (
        <div className="absolute inset-0 bg-white/70 backdrop-blur-sm z-20 flex flex-col items-center justify-center">
          <Loader2 className="animate-spin text-blue-600 mb-4" size={40}/>
          <span className="font-medium text-blue-800">正在应用匹配规则...</span>
        </div>
      )}

      {/* 步骤说明 + 操作按钮 */}
      <div className="flex justify-between items-start mb-4">
        <div>
          <h4 className="font-bold text-gray-800">
            {processStep === 1 ? '第一步：原始数据预览' : '第二步：匹配结果确认'}
          </h4>
          <p className="text-sm text-gray-500 mt-1">
            {processStep === 1
              ? '点击"单条测试"验证规则，点击"开始匹配"全局处理（已锁定的记录将被忽略）'
              : '确认分类无误后点击"确认导入"入库'}
          </p>
        </div>
        <div className="flex space-x-3">
          {processStep === 1 ? (
            <button
              onClick={handleStartMatch}
              disabled={isMatching}
              className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 text-sm disabled:opacity-50"
            >
              <Play size={16} className="mr-1" fill="currentColor"/>
              {isMatching ? '匹配中...' : '开始匹配'}
            </button>
          ) : (
            <>
              <button
                onClick={() => setField('processStep', 1)}
                className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-100 text-sm"
              >
                返回修改
              </button>
              <button
                onClick={handleConfirmImport}
                disabled={isImporting}
                className="flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 text-sm disabled:opacity-50"
              >
                {isImporting
                  ? <><Loader2 className="animate-spin mr-1" size={16}/>导入中</>
                  : <><Check size={16} className="mr-1"/>确认导入</>}
              </button>
            </>
          )}
        </div>
      </div>

      {/* 表格 */}
      <div className="bg-white border rounded-lg overflow-auto max-h-[600px]">
        <table className="min-w-full divide-y divide-gray-200 table-fixed">
          <thead className="bg-gray-100 sticky top-0 z-10">
            <tr>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-36">状态</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-36">时间</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-48">说明/对方</th>
              {dynamicColumns.map(c => (
                <th key={c.key} className={`px-4 py-2 text-left text-xs font-medium text-gray-500 ${c.width}`}>{c.label}</th>
              ))}
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">
                商户{processStep === 2 && <Edit2 size={12} className="inline ml-1 text-blue-500"/>}
              </th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-24">
                收支{processStep === 2 && <Edit2 size={12} className="inline ml-1 text-blue-500"/>}
              </th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">
                转账目标{processStep === 2 && <Edit2 size={12} className="inline ml-1 text-blue-500"/>}
              </th>
              <th className="px-4 py-2 text-right text-xs font-medium text-gray-500 w-24 sticky right-0 bg-gray-100 z-20">操作</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100 bg-white">
            {preview.previewRecords.map((record: any) => {
              const mod = (modifiedRecords[record.tempId] || {}) as any;
              const currentType = mod.type ?? record.type;
              const currentMerchant = mod.merchant !== undefined ? mod.merchant : record.merchant;
              const currentTarget = mod.targetAccountId !== undefined ? mod.targetAccountId : record.targetAccountId;
              const isMod = Object.keys(mod).length > 0;
              const isLocked = lockedTempIds.includes(record.tempId);
              const displayStatus = processStep === 1
                ? (record.matchStatus || 'ORIGINAL')
                : (isMod ? 'MANUAL_EDITED' : record.matchStatus);

              return (
                <tr
                  key={record.tempId}
                  className={`hover:bg-blue-50/20 ${isMod ? 'bg-yellow-50/20' : ''} ${isLocked ? 'bg-gray-50 opacity-75' : ''}`}
                >
                  <td className="px-4 py-2 whitespace-nowrap truncate" title={record.matchRuleName}>
                    {renderStatus(displayStatus, record.matchRuleName)}
                  </td>
                  <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-600 truncate">{record.transactionTime}</td>
                  <td className="px-4 py-2 text-sm text-gray-900 truncate">{record.counterparty}</td>
                  {dynamicColumns.map(c => (
                    <td key={c.key} className="px-4 py-2 whitespace-nowrap text-sm text-gray-500 truncate">
                      {record.extData?.[c.key] || '-'}
                    </td>
                  ))}

                  {/* 商户编辑 */}
                  <td className="px-4 py-2">
                    {processStep === 2 ? (
                      <input
                        type="text"
                        value={currentMerchant ?? ''}
                        onChange={e => handleChange(record.tempId, 'merchant', e.target.value)}
                        className="text-sm border-gray-300 rounded px-2 py-1 w-full border focus:ring-blue-500"
                        placeholder="未提取"
                      />
                    ) : (
                      <span className="text-sm text-gray-500 truncate block">{record.merchant || '-'}</span>
                    )}
                  </td>

                  {/* 收支编辑 */}
                  <td className="px-4 py-2">
                    {processStep === 2 ? (
                      <select
                        value={currentType}
                        onChange={e => handleChange(record.tempId, 'type', e.target.value)}
                        className={`text-sm border-gray-300 rounded px-2 py-1 w-full border focus:ring-blue-500 ${
                          currentType === 'INCOME' ? 'text-green-600' :
                          currentType === 'EXPENSE' ? 'text-red-600' : 'text-blue-600'
                        }`}
                      >
                        <option value="EXPENSE">支出</option>
                        <option value="INCOME">收入</option>
                        <option value="TRANSFER">转账</option>
                      </select>
                    ) : (
                      <span className={`text-sm ${record.type === 'INCOME' ? 'text-green-600' : record.type === 'EXPENSE' ? 'text-red-600' : 'text-gray-600'}`}>
                        {record.type}
                      </span>
                    )}
                  </td>

                  {/* 转账目标 */}
                  <td className="px-4 py-2">
                    {processStep === 2 && currentType === 'TRANSFER' ? (
                      <select
                        value={currentTarget ?? ''}
                        onChange={e => handleChange(record.tempId, 'targetAccountId', e.target.value ? Number(e.target.value) : undefined)}
                        className="text-sm border-gray-300 rounded px-2 py-1 w-full border focus:ring-blue-500"
                      >
                        <option value="">选择目标账户</option>
                        {accounts.map(a => (
                          <option key={a.id} value={a.id}>{a.accountName}</option>
                        ))}
                      </select>
                    ) : (
                      <span className="text-gray-400 text-sm">-</span>
                    )}
                  </td>

                  {/* 操作 */}
                  <td className="px-4 py-2 whitespace-nowrap text-right space-x-2 sticky right-0 bg-inherit shadow-[-4px_0_6px_-4px_rgba(0,0,0,0.1)]">
                    {processStep === 1 && (
                      <>
                        <button
                          onClick={() => handleTestSingle(record.tempId)}
                          className="text-blue-600 hover:text-blue-800 text-xs flex items-center"
                          title="单条测试"
                        >
                          <FlaskConical size={14} className="mr-0.5"/>
                          测试
                        </button>
                        <button
                          onClick={() => toggleLock(record.tempId)}
                          className={`${isLocked ? 'text-orange-500' : 'text-gray-400 hover:text-gray-600'} text-xs flex items-center`}
                          title="锁定后全局匹配将忽略此条"
                        >
                          <Pin size={14} className="mr-0.5" fill={isLocked ? 'currentColor' : 'none'}/>
                          {isLocked ? '已固定' : '固定'}
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

function getDynamicColumns(channel?: string) {
  switch (channel) {
    case 'ALIPAY':
    case 'JINGDONG':
      return [
        { key: 'paymentMethod', label: '付款方式', width: 'w-24' },
        { key: 'merchantOrderNo', label: '商家订单号', width: 'w-36' },
      ];
    case 'CITIC_CREDIT':
      return [
        { key: 'cardLast4', label: '卡号', width: 'w-20' },
        { key: 'billingAmount', label: '结算金额', width: 'w-24' },
      ];
    default:
      return [];
  }
}
