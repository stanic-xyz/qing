import { useEffect, useState } from 'react';
import axios from 'axios';
import { getEnumText } from '../utils/enumMap';

export default function ImportHistory() {
  const [records, setRecords] = useState([]);
  const [selectedUploadId, setSelectedUploadId] = useState<number | null>(null);
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    fetchUploads();
  }, []);

  const fetchUploads = async () => {
    try {
      const res = await axios.get('/api/finance/uploads');
      setRecords(res.data.data.content || []);
    } catch (e) {
      console.error(e);
    }
  };

  const fetchTransactions = async (id: number) => {
    try {
      const res = await axios.get(`/api/finance/uploads/${id}/transactions`);
      setTransactions(res.data.data || []);
      setSelectedUploadId(id);
    } catch (e) {
      console.error(e);
    }
  };

  const handleDeleteUpload = async (id: number) => {
    if (window.confirm('确认撤销/删除该批次导入吗？该操作会将所有关联交易记录标记为删除。')) {
      try {
        await axios.delete(`/api/finance/uploads/${id}?softDelete=true`);
        fetchUploads();
        if (selectedUploadId === id) {
          fetchTransactions(id);
        }
      } catch (e) {
        alert('删除失败');
      }
    }
  };

  return (
    <div className="flex flex-col h-full gap-6">
      <div>
        <h1 className="text-2xl font-bold mb-6">导入记录</h1>
        <div className="bg-white rounded-lg shadow-sm border border-gray-100 overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">文件名</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">渠道</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">解析数</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">文件大小</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">时间范围</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {records.map((r: any) => (
                <tr key={r.id} className="hover:bg-gray-50 cursor-pointer" onClick={() => fetchTransactions(r.id)}>
                  <td className="px-4 py-3 text-sm text-gray-900 max-w-xs truncate" title={r.fileName}>{r.fileName}</td>
                  <td className="px-4 py-3 text-sm text-gray-500">{getEnumText('channel', r.channel)}</td>
                  <td className="px-4 py-3 text-sm text-gray-500">{r.status}</td>
                  <td className="px-4 py-3 text-sm text-gray-500">{r.parsedCount}</td>
                  <td className="px-4 py-3 text-sm text-gray-500">{r.fileSize ? (r.fileSize / 1024).toFixed(2) + ' KB' : '-'}</td>
                  <td className="px-4 py-3 text-sm text-gray-500">{r.startTime && r.endTime ? `${r.startTime.split('T')[0]} ~ ${r.endTime.split('T')[0]}` : '-'}</td>
                  <td className="px-4 py-3 text-sm font-medium">
                    <button onClick={(e) => { e.stopPropagation(); handleDeleteUpload(r.id); }} className="text-red-600 hover:text-red-900">撤销</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {selectedUploadId && (
        <div>
          <h2 className="text-xl font-bold mb-4">批次详情 (高亮标记：<span className="text-red-500">红色背景为已删除</span>，<span className="text-yellow-600">黄色文字为被修改过</span>)</h2>
          <div className="bg-white rounded-lg shadow-sm border border-gray-100 overflow-hidden overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">时间</th>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">类型</th>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">金额</th>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">对方</th>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">说明</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {transactions.map((t: any) => (
                  <tr key={t.id} className={`${t.isDeleted ? 'bg-red-50' : 'hover:bg-gray-50'}`}>
                    <td className={`px-4 py-3 text-sm ${t.isModified ? 'text-yellow-600 font-medium' : 'text-gray-500'}`}>{t.transactionTime}</td>
                    <td className={`px-4 py-3 text-sm ${t.isModified ? 'text-yellow-600 font-medium' : 'text-gray-500'}`}>{getEnumText('type', t.type)}</td>
                    <td className={`px-4 py-3 text-sm font-bold ${t.isModified ? 'text-yellow-600 font-medium' : 'text-gray-900'}`}>{t.amount}</td>
                    <td className={`px-4 py-3 text-sm max-w-xs truncate ${t.isModified ? 'text-yellow-600 font-medium' : 'text-gray-500'}`} title={t.counterparty}>{t.counterparty || '-'}</td>
                    <td className={`px-4 py-3 text-sm max-w-xs truncate ${t.isModified ? 'text-yellow-600 font-medium' : 'text-gray-500'}`} title={t.merchant}>{t.merchant || '-'}</td>
                  </tr>
                ))}
                {transactions.length === 0 && (
                  <tr><td colSpan={5} className="px-4 py-8 text-center text-gray-500">无记录</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
