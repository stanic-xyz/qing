import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Search, X } from 'lucide-react';
import { getEnumText } from '../utils/enumMap';

interface TransactionSelectorProps {
  isOpen: boolean;
  onClose: () => void;
  onSelect: (record: any) => void;
}

export default function TransactionSelector({ isOpen, onClose, onSelect }: TransactionSelectorProps) {
  const [keyword, setKeyword] = useState('');
  const [transactions, setTransactions] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedId, setSelectedId] = useState<number | null>(null);

  useEffect(() => {
    if (isOpen) {
      fetchTransactions();
    }
  }, [isOpen]);

  const fetchTransactions = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/finance/transactions', {
        params: {
          page: 0,
          size: 20,
          keyword: keyword
        }
      });
      if (res.data.code === 200) {
        setTransactions(res.data.data.content || []);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    fetchTransactions();
  };

  const handleConfirm = () => {
    if (selectedId) {
      const record = transactions.find(t => t.id === selectedId);
      if (record) {
        onSelect(record);
      }
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" onClick={onClose}></div>
      <div className="relative bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all w-full max-w-4xl max-h-[90vh] flex flex-col">
        <div className="bg-white px-4 pt-5 pb-4 sm:p-6 flex-1 overflow-hidden flex flex-col">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-lg leading-6 font-medium text-gray-900" id="modal-title">
              选择交易流水
            </h3>
            <button onClick={onClose} className="text-gray-400 hover:text-gray-500">
              <X className="h-6 w-6" />
            </button>
          </div>

          <form onSubmit={handleSearch} className="flex gap-2 mb-4">
              <div className="relative flex-1">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Search className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  type="text"
                  className="focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 sm:text-sm border-gray-300 rounded-md border p-2"
                  placeholder="搜索商户、对手、备注..."
                  value={keyword}
                  onChange={(e) => setKeyword(e.target.value)}
                />
              </div>
              <button
                type="submit"
                className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700"
              >
                搜索
              </button>
            </form>

            <div className="flex-1 overflow-y-auto border rounded-md">
            {loading ? (
              <div className="p-4 text-center text-gray-500">加载中...</div>
            ) : transactions.length === 0 ? (
              <div className="p-4 text-center text-gray-500">未找到匹配的流水</div>
            ) : (
              <table className="min-w-full divide-y divide-gray-200 relative">
                <thead className="bg-gray-50 sticky top-0 z-10">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50"></th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50">时间</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50">渠道</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50">金额</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50">商户/对手</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50">备注</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                    {transactions.map((t) => (
                      <tr
                        key={t.id}
                        onClick={() => setSelectedId(t.id)}
                        className={`cursor-pointer hover:bg-blue-50 ${selectedId === t.id ? 'bg-blue-50' : ''}`}
                      >
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          <input
                            type="radio"
                            name="transaction_select"
                            checked={selectedId === t.id}
                            onChange={() => setSelectedId(t.id)}
                            className="focus:ring-blue-500 h-4 w-4 text-blue-600 border-gray-300"
                          />
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{t.transactionTime}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{getEnumText('channel', t.channel?.id)}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{t.amount}</td>
                        <td className="px-6 py-4 text-sm text-gray-500 max-w-[200px] truncate" title={t.merchant || t.counterparty}>
                          {t.merchant || t.counterparty || '-'}
                        </td>
                        <td className="px-6 py-4 text-sm text-gray-500 max-w-[150px] truncate" title={t.remark}>{t.remark || '-'}</td>
                      </tr>
                    ))}
                  </tbody>
            </table>
          )}
        </div>
        </div>
        <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse rounded-b-lg">
            <button
              type="button"
              disabled={!selectedId}
              onClick={handleConfirm}
              className={`w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 text-base font-medium text-white sm:ml-3 sm:w-auto sm:text-sm
                ${selectedId ? 'bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500' : 'bg-blue-300 cursor-not-allowed'}`}
            >
              确定
            </button>
            <button
              type="button"
              onClick={onClose}
              className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
            >
              取消
            </button>
        </div>
      </div>
    </div>
  );
}
