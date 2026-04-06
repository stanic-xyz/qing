import React from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';
import { getEnumText } from '../../../utils/enumMap';
import ProcessTable from './ProcessTable';
import type { Account, PreviewRecord } from '../types';

interface ImportRecordListProps {
  records: any[];
  expandedUploadId: string | null;
  onToggleExpand: (uploadId: string) => void;
  onDeleteUpload: (uploadId: number) => void;
  processPreview: { uploadId: string; previewRecords: PreviewRecord[] } | null;
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

export default function ImportRecordList({
  records, expandedUploadId, onToggleExpand, onDeleteUpload,
  processPreview, setProcessPreview, accounts, selectedRuleIds,
  processStep, setProcessStep, modifiedRecords, setModifiedRecords,
  lockedTempIds, setLockedTempIds, isMatching, setIsMatching,
  isImporting, setIsImporting, onImportSuccess
}: ImportRecordListProps) {

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

  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-100 overflow-hidden">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">文件名</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">渠道</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">解析数</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">时间范围</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {records.map((r: any) => (
            <React.Fragment key={r.id}>
              <tr
                className={`hover:bg-gray-50 cursor-pointer ${expandedUploadId === String(r.id) ? 'bg-blue-50/30' : ''}`}
                onClick={() => onToggleExpand(String(r.id))}
              >
                <td className="px-4 py-3 text-sm text-gray-900 max-w-xs truncate" title={r.fileName}>{r.fileName}</td>
                <td className="px-4 py-3 text-sm text-gray-500">{getEnumText('channel', r.channel)}</td>
                <td className="px-4 py-3 text-sm whitespace-nowrap">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                    r.status === 'IMPORTED' ? 'bg-green-100 text-green-800' :
                    r.status === 'UPLOADED' ? 'bg-yellow-100 text-yellow-800' :
                    r.status === 'MATCHING' ? 'bg-blue-100 text-blue-800' : 'bg-gray-100 text-gray-800'
                  }`}>
                    {r.status === 'IMPORTED' ? '已导入' : r.status === 'UPLOADED' ? '待处理(已解析)' : r.status === 'MATCHING' ? '匹配中' : r.status}
                  </span>
                </td>
                <td className="px-4 py-3 text-sm text-gray-500">{r.parsedCount}</td>
                <td className="px-4 py-3 text-sm text-gray-500">{r.startTime && r.endTime ? `${r.startTime.split('T')[0]} ~ ${r.endTime.split('T')[0]}` : '-'}</td>
                <td className="px-4 py-3 text-sm font-medium space-x-3 flex items-center">
                  {r.status !== 'IMPORTED' && (
                    <button
                      onClick={(e) => { e.stopPropagation(); onToggleExpand(String(r.id)); }}
                      className="text-blue-600 hover:text-blue-900 flex items-center"
                    >
                      {expandedUploadId === String(r.id) ? <><ChevronUp size={16} className="mr-1"/>收起</> : <><ChevronDown size={16} className="mr-1"/>展开处理</>}
                    </button>
                  )}
                  {r.status !== 'IMPORTED' && (
                    <button
                      onClick={(e) => { e.stopPropagation(); onDeleteUpload(r.id); }}
                      className="text-red-600 hover:text-red-900"
                    >
                      撤销
                    </button>
                  )}
                  {r.status === 'IMPORTED' && (
                    <span className="text-gray-400 text-xs flex items-center">
                      {expandedUploadId === String(r.id) ? <><ChevronUp size={16} className="mr-1"/>收起查看</> : <><ChevronDown size={16} className="mr-1"/>展开查看</>}
                    </span>
                  )}
                </td>
              </tr>
              {/* 内联展开的表格处理区域 */}
              {expandedUploadId === String(r.id) && processPreview && (
                <tr>
                  <td colSpan={6} className="p-0 border-b border-gray-200">
                    {r.status === 'IMPORTED' ? (
                      <div className="bg-gray-50 p-6 border-l-4 border-green-500 shadow-inner">
                        <div className="flex justify-between items-center mb-4">
                          <div>
                            <h4 className="font-bold text-gray-800">已归档交易记录</h4>
                            <p className="text-sm text-gray-500">此批次数据已成功导入并归档，不可修改。以下为最终入库的流水详情及命中的规则。</p>
                          </div>
                        </div>
                        <div className="bg-white border rounded-lg overflow-hidden max-h-150 overflow-x-auto overflow-y-auto">
                          <table className="min-w-full divide-y divide-gray-200 table-fixed">
                            <thead className="bg-gray-100 sticky top-0 z-10">
                              <tr>
                                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">匹配状态</th>
                                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-40">时间</th>
                                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-64">说明/对方</th>
                                {dynamicColumns.map(col => (
                                  <th key={col.key} className={`px-4 py-2 text-left text-xs font-medium text-gray-500 ${col.width}`}>{col.label}</th>
                                ))}
                                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-32">商户/平台</th>
                                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 w-24">收支</th>
                                <th className="px-4 py-2 text-right text-xs font-medium text-gray-500 w-32 sticky right-0 bg-gray-100 z-10">金额</th>
                              </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100 bg-white">
                              {processPreview.previewRecords.map((record) => (
                                <tr key={record.tempId} className="hover:bg-gray-50">
                                  <td className="px-4 py-2 whitespace-nowrap truncate" title={record.matchRuleName || record.matchStatus}>
                                    {renderMatchStatus(record.matchStatus, record.matchRuleName)}
                                  </td>
                                  <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-600 truncate" title={record.transactionTime}>{record.transactionTime}</td>
                                  <td className="px-4 py-2 text-sm text-gray-900 truncate" title={record.counterparty}>{record.counterparty}{record.account.accountName}</td>
                                  {dynamicColumns.map(col => (
                                    <td key={col.key} className="px-4 py-2 whitespace-nowrap text-sm text-gray-500 truncate" title={record.extData?.[col.key] || '-'}>
                                      {record.extData?.[col.key] || '-'}
                                    </td>
                                  ))}
                                  <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-700 truncate" title={record.merchant || '-'}>{record.merchant || '-'}</td>
                                  <td className="px-4 py-2 whitespace-nowrap">
                                    <span className={`text-sm ${record.type === 'INCOME' ? 'text-green-600' : record.type === 'EXPENSE' ? 'text-red-600' : 'text-gray-600'}`}>
                                      {record.type}
                                    </span>
                                  </td>
                                  <td className="px-4 py-2 whitespace-nowrap text-right text-sm font-semibold text-gray-900 sticky right-0 bg-inherit shadow-[-4px_0_6px_-4px_rgba(0,0,0,0.1)]">{record.amount}</td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                        </div>
                      </div>
                    ) : (
                      <ProcessTable
                        uploadId={String(r.id)}
                        processPreview={processPreview}
                        setProcessPreview={setProcessPreview}
                        accounts={accounts}
                        selectedRuleIds={selectedRuleIds}
                        processStep={processStep}
                        setProcessStep={setProcessStep}
                        modifiedRecords={modifiedRecords}
                        setModifiedRecords={setModifiedRecords}
                        lockedTempIds={lockedTempIds}
                        setLockedTempIds={setLockedTempIds}
                        isMatching={isMatching}
                        setIsMatching={setIsMatching}
                        isImporting={isImporting}
                        setIsImporting={setIsImporting}
                        onImportSuccess={onImportSuccess}
                      />
                    )}
                  </td>
                </tr>
              )}
            </React.Fragment>
          ))}
          {records.length === 0 && (
            <tr><td colSpan={6} className="px-4 py-8 text-center text-gray-500">暂无导入记录</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
