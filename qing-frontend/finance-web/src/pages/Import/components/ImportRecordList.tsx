// 重写版 ImportRecordList：使用 Zustand store 管理展开行状态
import React, { useEffect } from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';
import axios from 'axios';
import { getEnumText } from '../../../utils/enumMap';
import { useImportStore } from '../../../store/importstore';
import ProcessTable from './ProcessTable';

interface ImportRecordListProps {
  records: any[];
  expandedUploadId: string | null;
  onToggleExpand: (uploadId: string) => void;
  onDeleteUpload: (id: number) => void;
  accounts: any[];
  selectedRuleIds: Set<number>;
  onImportSuccess: () => void;
}

export default function ImportRecordList({
  records, expandedUploadId, onToggleExpand, onDeleteUpload,
  accounts, selectedRuleIds, onImportSuccess,
}: ImportRecordListProps) {
  const { previewMap, setPreview, rowStates, setRowState, resetRow } = useImportStore();

  // 展开时加载预览
  useEffect(() => {
    if (!expandedUploadId) return;

    const existing = previewMap[expandedUploadId];
    if (existing?.previewRecords?.length > 0) return; // 已加载

    if (!rowStates[expandedUploadId]?.isLoadingPreview) {
      setRowState(expandedUploadId, { isLoadingPreview: true });
    }

    axios.get(`/api/bills/preview/${expandedUploadId}`)
      .then(res => {
        const data = res.data?.data;
        setPreview(expandedUploadId, {
          overview: null,
          previewRecords: data?.previewRecords || [],
          totalCount: data?.totalCount || 0,
          hasMore: data?.hasMore || false,
        });
      })
      .catch(console.error)
      .finally(() => {
        setRowState(expandedUploadId, { isLoadingPreview: false });
      });
  }, [expandedUploadId]);

  // 清除非展开行状态
  useEffect(() => {
    if (expandedUploadId) {
      Object.keys(previewMap).forEach(k => {
        if (k !== expandedUploadId) {
          resetRow(k);
        }
      });
    }
  }, [expandedUploadId]);

  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-100 overflow-hidden">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            {['文件名', '渠道', '状态', '解析数', '时间范围', '操作'].map(h => (
              <th key={h} className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">{h}</th>
            ))}
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {records.length === 0 ? (
            <tr>
              <td colSpan={6} className="px-4 py-12 text-center text-gray-500">
                暂无导入记录，点击右上角「上传账单」开始
              </td>
            </tr>
          ) : records.map((r: any) => {
            const isExpanded = expandedUploadId === String(r.id);
            const preview = previewMap[String(r.id)];
            const state = rowStates[String(r.id)];
            const channel = preview?.previewRecords?.[0]?.channel;

            return (
              <React.Fragment key={r.id}>

                {/* 主行 */}
                <tr
                  className={`hover:bg-gray-50 cursor-pointer transition-colors ${isExpanded ? 'bg-blue-50/30' : ''}`}
                  onClick={() => onToggleExpand(String(r.id))}
                >
                  <td className="px-4 py-3 text-sm text-gray-900 max-w-xs truncate" title={r.fileName}>
                    {r.fileName}
                  </td>
                  <td className="px-4 py-3 text-sm text-gray-500">
                    {getEnumText('channel', r.channel)}
                  </td>
                  <td className="px-4 py-3 text-sm whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      r.status === 'IMPORTED' ? 'bg-green-100 text-green-800' :
                      r.status === 'UPLOADED' ? 'bg-yellow-100 text-yellow-800' :
                      r.status === 'MATCHING' ? 'bg-blue-100 text-blue-800' :
                      'bg-gray-100 text-gray-800'
                    }`}>
                      {r.status === 'IMPORTED' ? '已导入' :
                       r.status === 'UPLOADED' ? '待处理' :
                       r.status === 'MATCHING' ? '匹配中' : r.status}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-sm text-gray-500">{r.parsedCount}</td>
                  <td className="px-4 py-3 text-sm text-gray-500">
                    {r.startTime && r.endTime
                      ? `${r.startTime.split('T')[0]} ~ ${r.endTime.split('T')[0]}`
                      : '-'}
                  </td>
                  <td className="px-4 py-3 text-sm font-medium space-x-3 flex items-center">
                    {r.status !== 'IMPORTED' ? (
                      <>
                        <button
                          onClick={e => { e.stopPropagation(); onToggleExpand(String(r.id)); }}
                          className="text-blue-600 hover:text-blue-900 flex items-center"
                        >
                          {isExpanded
                            ? <><ChevronUp size={16} className="mr-1"/>收起</>
                            : <><ChevronDown size={16} className="mr-1"/>展开处理</>}
                        </button>
                        <button
                          onClick={e => { e.stopPropagation(); onDeleteUpload(r.id); }}
                          className="text-red-600 hover:text-red-900"
                        >
                          撤销
                        </button>
                      </>
                    ) : (
                      <span className="text-gray-400 text-xs flex items-center">
                        {isExpanded
                          ? <><ChevronUp size={16} className="mr-1"/>收起</>
                          : <><ChevronDown size={16} className="mr-1"/>查看</>}
                      </span>
                    )}
                  </td>
                </tr>

                {/* 展开行 */}
                {isExpanded && (
                  <tr>
                    <td colSpan={6} className="p-0 border-b border-gray-200">
                      {state?.isLoadingPreview ? (
                        <div className="p-8 text-center text-gray-500">加载中...</div>
                      ) : preview?.previewRecords ? (
                        r.status === 'IMPORTED' ? (
                          <ImportedView preview={preview} channel={channel} />
                        ) : (
                          <ProcessTable
                            uploadId={String(r.id)}
                            preview={preview}
                            accounts={accounts}
                            selectedRuleIds={selectedRuleIds}
                            onImportSuccess={onImportSuccess}
                          />
                        )
                      ) : (
                        <div className="p-4 text-center text-red-500">预览加载失败</div>
                      )}
                    </td>
                  </tr>
                )}
              </React.Fragment>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

// 已导入记录查看视图
function ImportedView({ preview, channel }: { preview: any; channel?: string }) {
  const cols = getDynamicColumns(channel);
  const renderStatus = (status: string, ruleName?: string) => {
    const m: Record<string, { bg: string; text: string; icon: string; label: string }> = {
      AUTO_MATCHED: { bg: 'bg-green-100', text: 'text-green-800', icon: '🟢', label: '自动匹配' },
      INTERNAL_TRANSFER: { bg: 'bg-blue-100', text: 'text-blue-800', icon: '🔵', label: '内部转账' },
      MANUAL_EDITED: { bg: 'bg-purple-100', text: 'text-purple-800', icon: '🟣', label: '手动修改' },
      SUSPICIOUS: { bg: 'bg-yellow-100', text: 'text-yellow-800', icon: '🟡', label: '存疑' },
      ORIGINAL: { bg: 'bg-gray-100', text: 'text-gray-800', icon: '⚪', label: '原始数据' },
    };
    const s = m[status] || m['ORIGINAL'];
    return (
      <span className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-medium ${s.bg} ${s.text}`}>
        {s.icon} {s.label}{ruleName && `(${ruleName})`}
      </span>
    );
  };

  return (
    <div className="bg-gray-50 p-6 border-l-4 border-green-500">
      <div className="mb-4">
        <h4 className="font-bold text-gray-800">已归档交易记录</h4>
        <p className="text-sm text-gray-500">此批次已导入归档，不可修改。</p>
      </div>
      <div className="bg-white border rounded-lg overflow-auto max-h-[500px]">
        <table className="min-w-full divide-y divide-gray-200 table-fixed">
          <thead className="bg-gray-100 sticky top-0 z-10">
            <tr>
              {['匹配状态', '时间', '说明/对方', ...cols.map(c => c.label), '商户/平台', '收支', { label: '金额', sticky: true }].map((h, i) => (
                <th key={i}
                  className={`px-4 py-2 text-left text-xs font-medium text-gray-500 ${typeof h === 'object' && h.sticky ? 'sticky right-0 bg-gray-100 z-20' : ''}`}
                >
                  {typeof h === 'string' ? h : h.label}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100 bg-white">
            {preview.previewRecords.map((record: any) => (
              <tr key={record.tempId} className="hover:bg-gray-50">
                <td className="px-4 py-2 whitespace-nowrap truncate text-xs" title={record.matchRuleName}>
                  {renderStatus(record.matchStatus, record.matchRuleName)}
                </td>
                <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-600 truncate">{record.transactionTime}</td>
                <td className="px-4 py-2 text-sm text-gray-900 truncate">{record.counterparty}</td>
                {cols.map(col => (
                  <td key={col.key} className="px-4 py-2 whitespace-nowrap text-sm text-gray-500 truncate">
                    {record.extData?.[col.key] || '-'}
                  </td>
                ))}
                <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-700 truncate">{record.merchant || '-'}</td>
                <td className="px-4 py-2 whitespace-nowrap text-sm">
                  <span className={record.type === 'INCOME' ? 'text-green-600' : record.type === 'EXPENSE' ? 'text-red-600' : 'text-gray-600'}>
                    {record.type}
                  </span>
                </td>
                <td className="px-4 py-2 whitespace-nowrap text-right text-sm font-semibold text-gray-900 sticky right-0 bg-white shadow-[-4px_0_6px_-4px_rgba(0,0,0,0.1)]">
                  {record.amount}
                </td>
              </tr>
            ))}
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
        { key: 'paymentMethod', label: '付款方式' },
        { key: 'merchantOrderNo', label: '商家订单号' },
      ];
    case 'CITIC_CREDIT':
      return [
        { key: 'cardLast4', label: '卡号' },
        { key: 'billingAmount', label: '结算金额' },
      ];
    default:
      return [];
  }
}
