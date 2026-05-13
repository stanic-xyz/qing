import axios from 'axios';
import { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { DraftBatch, DraftBatchStatus } from '../types';

const actionToStatus: Record<string, DraftBatchStatus | null> = {
  START_MATCH: 'MATCHING',
  CONFIRM: 'CONFIRMING',
  IMPORT: 'IMPORTED',
  RETRY: null,
  REFRESH_STATUS: null,
  VIEW_RESULT: null,
};

const actionLabel: Record<string, string> = {
  START_MATCH: '开始匹配',
  CONFIRM: '确认批次',
  IMPORT: '执行入账',
  RETRY: '重试',
  REFRESH_STATUS: '刷新状态',
  VIEW_RESULT: '查看结果',
};

const matchStatusUi: Record<string, { label: string; className: string }> = {
  MATCHED: { label: '已匹配', className: 'bg-emerald-100 text-emerald-700' },
  REVIEW_REQUIRED: { label: '待复核', className: 'bg-amber-100 text-amber-700' },
  UNMATCHED: { label: '未匹配', className: 'bg-slate-100 text-slate-700' },
};

export default function NewDraftFlowCard() {
  const navigate = useNavigate();
  const [batch, setBatch] = useState<DraftBatch | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [records, setRecords] = useState<any[]>([]);
  const [recordsPage, setRecordsPage] = useState(0);
  const [recordsSize] = useState(10);
  const [recordsTotal, setRecordsTotal] = useState(0);
  const [recordsLoading, setRecordsLoading] = useState(false);
  const [hasLoadedRecords, setHasLoadedRecords] = useState(false);
  const [matchStatusFilter, setMatchStatusFilter] = useState('');

  const canPoll = useMemo(() => {
    if (!batch) return false;
    return batch.allowedActions?.includes('REFRESH_STATUS') || batch.status === 'MATCHING';
  }, [batch]);

  const createBatch = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await axios.post('/api/import/draft/batches', {
        adapterType: 'PARSER',
        totalRecords: 0,
      });
      setBatch(res.data?.data || null);
    } catch (e: any) {
      setError(e?.response?.data?.message || '创建批次失败');
    } finally {
      setLoading(false);
    }
  };

  const refreshBatch = async (silent = false) => {
    if (!batch) return null;
    if (!silent) {
      setLoading(true);
    }
    if (!silent) {
      setError(null);
    }
    try {
      const res = await axios.get(`/api/import/draft/batches/${batch.id}`);
      const next = res.data?.data || null;
      setBatch(next);
      return next;
    } catch (e: any) {
      if (!silent) {
        setError(e?.response?.data?.message || '刷新批次失败');
      }
      return null;
    } finally {
      if (!silent) {
        setLoading(false);
      }
    }
  };

    const runAction = async (action: string) => {
    if (!batch) return;
    if (action === 'REFRESH_STATUS') {
      await refreshBatch(false);
      return;
    }

    if (action === 'VIEW_RESULT') {
      await fetchRecords(0);
      return;
    }

    if (action === 'IMPORT') {
      setLoading(true);
      setError(null);
      try {
        const res = await axios.post(`/api/import/draft/batches/${batch.id}/commit`);
        const result = res.data?.data;
        const nextBatch = res.data?.data?.batch || res.data?.data;
        if (nextBatch) setBatch(nextBatch);
        await refreshBatch(true);
        if (hasLoadedRecords) await fetchRecords(recordsPage);
        if (result?.message) {
          alert(result.message);
        }
      } catch (e: any) {
        setError(e?.response?.data?.message || '执行入账失败');
      } finally {
        setLoading(false);
      }
      return;
    }

    const target = actionToStatus[action];
    if (!target && action !== 'RETRY') return;
    const effectiveTarget = action === 'RETRY' ? 'MATCHING' : target;

    setLoading(true);
    setError(null);
    try {
      const res = await axios.post(`/api/import/draft/batches/${batch.id}/actions`, {
        targetStatus: effectiveTarget,
      });
      const nextBatch = res.data?.data || null;
      setBatch(nextBatch);
      if (action === 'CONFIRM' || action === 'START_MATCH' || action === 'RETRY') {
        await refreshBatch(true);
      }
      if (hasLoadedRecords) {
        await fetchRecords(recordsPage);
      }
    } catch (e: any) {
      setError(e?.response?.data?.message || `执行动作失败: ${action}`);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!canPoll || !batch) return;
    const timer = window.setInterval(() => {
      refreshBatch(true);
    }, 3000);
    return () => window.clearInterval(timer);
  }, [canPoll, batch?.id]);

  const fetchRecords = async (page = recordsPage, status = matchStatusFilter) => {
    if (!batch) return;
    setRecordsLoading(true);
    setError(null);
    try {
      const query = new URLSearchParams();
      query.set('page', String(page));
      query.set('size', String(recordsSize));
      if (status) query.set('matchStatus', status);
      const res = await axios.get(`/api/import/draft/batches/${batch.id}/records?${query.toString()}`);
      const data = res.data?.data;
      setRecords(data?.content || []);
      setRecordsTotal(data?.totalElements || 0);
      setRecordsPage(data?.number ?? page);
      setHasLoadedRecords(true);
    } catch (e: any) {
      setError(e?.response?.data?.message || '加载草稿记录失败');
    } finally {
      setRecordsLoading(false);
    }
  };

  const formatDateTime = (value?: string) => {
    if (!value) return '-';
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) return value;
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
  };

  const renderMatchStatus = (status?: string) => {
    if (!status) {
      return <span className="inline-flex px-2 py-0.5 rounded bg-gray-100 text-gray-700">-</span>;
    }
    const ui = matchStatusUi[status] || { label: status, className: 'bg-gray-100 text-gray-700' };
    return <span className={`inline-flex px-2 py-0.5 rounded ${ui.className}`}>{ui.label}</span>;
  };

  return (
    <div className="bg-blue-50 border border-blue-200 rounded-xl p-4 mb-4">
      <div className="flex items-start justify-between gap-3">
        <div>
          <h3 className="text-base font-semibold text-blue-900">新导入流程（灰度）</h3>
          <p className="text-sm text-blue-700 mt-1">先走统一草稿批次状态机，旧导入流程保持不变。</p>
        </div>
        <button
          onClick={createBatch}
          disabled={loading}
          className="px-3 py-1.5 text-sm bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-60"
        >
          新建批次
        </button>
      </div>

      {batch && (
        <div className="mt-4 p-3 bg-white border border-blue-100 rounded-lg">
          <div className="text-sm text-gray-700">批次号: <span className="font-mono">{batch.batchNo}</span></div>
          <div className="text-sm text-gray-700 mt-1">状态: <span className="font-medium">{batch.status}</span></div>
          <div className="text-sm text-gray-700 mt-1">进度: <span className="font-medium">{batch.progress}%</span></div>
          <div className="text-sm text-gray-700 mt-1">允许动作: {batch.allowedActions?.join(', ') || '-'}</div>
          {error && <div className="text-sm text-red-600 mt-2">{error}</div>}

          <div className="mt-3 flex gap-2 flex-wrap">
            {batch.allowedActions?.map((action) => (
              <button
                key={action}
                onClick={() => runAction(action)}
                disabled={loading}
                className="px-3 py-1.5 text-sm bg-white border border-gray-200 rounded-md hover:bg-gray-50 disabled:opacity-60"
              >
                {actionLabel[action] || action}
              </button>
            ))}
            {batch.status === 'IMPORTED' && (
              <button
                onClick={() => navigate(`/import?highlightBatchNo=${encodeURIComponent(batch.batchNo)}`)}
                className="px-3 py-1.5 text-sm bg-indigo-600 text-white rounded-md hover:bg-indigo-700"
              >
                跳转旧版记录
              </button>
            )}
          </div>

          <div className="mt-4 border-t pt-3">
            <div className="flex items-center justify-between mb-2">
              <div className="text-sm font-medium text-gray-800">草稿记录（灰度）</div>
              <div className="flex items-center gap-2">
                <select
                  value={matchStatusFilter}
                  onChange={(e) => {
                    const v = e.target.value;
                    setMatchStatusFilter(v);
                    fetchRecords(0, v);
                  }}
                  className="px-2 py-1 text-xs border border-gray-200 rounded-md bg-white"
                >
                  <option value="">全部状态</option>
                  <option value="MATCHED">已匹配</option>
                  <option value="REVIEW_REQUIRED">待复核</option>
                  <option value="UNMATCHED">未匹配</option>
                </select>
                <button
                  onClick={() => fetchRecords(0, matchStatusFilter)}
                  disabled={recordsLoading}
                  className="px-2.5 py-1 text-xs bg-white border border-gray-200 rounded-md hover:bg-gray-50 disabled:opacity-60"
                >
                  刷新记录
                </button>
              </div>
            </div>

            {recordsLoading ? (
              <div className="text-sm text-gray-500">加载中...</div>
            ) : records.length === 0 ? (
              <div className="text-sm text-gray-500">暂无草稿记录</div>
            ) : (
              <div className="overflow-x-auto border border-gray-100 rounded-lg">
                <table className="min-w-full text-xs">
                  <thead className="bg-gray-50 text-gray-600">
                    <tr>
                      <th className="px-3 py-2 text-left">时间</th>
                      <th className="px-3 py-2 text-left">方向</th>
                      <th className="px-3 py-2 text-right">金额</th>
                      <th className="px-3 py-2 text-left">对手方</th>
                      <th className="px-3 py-2 text-left">匹配状态</th>
                    </tr>
                  </thead>
                  <tbody>
                    {records.map((r) => (
                      <tr key={r.id} className="border-t border-gray-100 text-gray-700">
                        <td className="px-3 py-2 whitespace-nowrap">{formatDateTime(r.transactionTime)}</td>
                        <td className="px-3 py-2 whitespace-nowrap">{r.direction || '-'}</td>
                        <td className="px-3 py-2 text-right whitespace-nowrap">{r.amount ?? '-'}</td>
                        <td className="px-3 py-2">{r.counterparty || '-'}</td>
                        <td className="px-3 py-2">
                          {renderMatchStatus(r.matchStatus)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}

            {recordsTotal > recordsSize && (
              <div className="mt-2 flex gap-2">
                <button
                  onClick={() => fetchRecords(Math.max(0, recordsPage - 1))}
                  disabled={recordsLoading || recordsPage <= 0}
                  className="px-2.5 py-1 text-xs bg-white border border-gray-200 rounded-md hover:bg-gray-50 disabled:opacity-60"
                >
                  上一页
                </button>
                <button
                  onClick={() => fetchRecords(recordsPage + 1)}
                  disabled={recordsLoading || (recordsPage + 1) * recordsSize >= recordsTotal}
                  className="px-2.5 py-1 text-xs bg-white border border-gray-200 rounded-md hover:bg-gray-50 disabled:opacity-60"
                >
                  下一页
                </button>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
