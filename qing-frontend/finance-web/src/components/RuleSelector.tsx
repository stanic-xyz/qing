import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Search, X } from 'lucide-react';

interface RuleSelectorProps {
  isOpen: boolean;
  onClose: () => void;
  onSelect: (ruleIds: number[]) => void;
  initialSelectedIds?: number[];
  targetType: 'COUNTERPARTY' | 'CATEGORY' | 'ACCOUNT';
}

export default function RuleSelector({ isOpen, onClose, onSelect, initialSelectedIds = [], targetType }: RuleSelectorProps) {
  const [keyword, setKeyword] = useState('');
  const [rules, setRules] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedIds, setSelectedIds] = useState<number[]>(initialSelectedIds);

  useEffect(() => {
    if (isOpen) {
      setSelectedIds(initialSelectedIds);
      fetchRules();
    }
  }, [isOpen, initialSelectedIds]);

  const fetchRules = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/finance/matchers');
      if (res.data.code === 200) {
        let list = res.data.data || [];
        if (keyword) {
          list = list.filter((r: any) => r.name.toLowerCase().includes(keyword.toLowerCase()) ||
                                       (r.description && r.description.toLowerCase().includes(keyword.toLowerCase())));
        }
        setRules(list);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    fetchRules();
  };

  const toggleSelect = (id: number) => {
    if (selectedIds.includes(id)) {
      setSelectedIds(selectedIds.filter(i => i !== id));
    } else {
      setSelectedIds([...selectedIds, id]);
    }
  };

  const handleConfirm = () => {
    onSelect(selectedIds);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" onClick={onClose}></div>
      <div className="relative bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all w-full max-w-2xl max-h-[90vh] flex flex-col">
        <div className="bg-white px-4 pt-5 pb-4 sm:p-6 flex-1 overflow-hidden flex flex-col">
          <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg leading-6 font-medium text-gray-900" id="modal-title">
                选择并绑定匹配规则
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
                placeholder="搜索规则名称..."
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
              ) : rules.length === 0 ? (
                <div className="p-4 text-center text-gray-500">未找到匹配的规则</div>
              ) : (
                <table className="min-w-full divide-y divide-gray-200 relative">
                  <thead className="bg-gray-50 sticky top-0 z-10">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-12 bg-gray-50">选择</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50">规则名称</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50">状态</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50">优先级</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                  {rules.map((r) => (
                    <tr
                      key={r.id}
                      onClick={() => toggleSelect(r.id)}
                      className={`cursor-pointer hover:bg-blue-50 ${selectedIds.includes(r.id) ? 'bg-blue-50' : ''}`}
                    >
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <input
                          type="checkbox"
                          checked={selectedIds.includes(r.id)}
                          onChange={() => toggleSelect(r.id)}
                          onClick={(e) => e.stopPropagation()}
                          className="focus:ring-blue-500 h-4 w-4 text-blue-600 border-gray-300 rounded"
                        />
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-900 max-w-[200px] truncate" title={r.description}>
                        <div className="font-medium">{r.name}</div>
                        <div className="text-xs text-gray-500">{r.description}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {r.isActive ? <span className="text-green-600">已启用</span> : <span className="text-gray-400">已禁用</span>}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{r.priority}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>

          <div className="mt-2 text-sm text-gray-500">
            已选择 {selectedIds.length} 个规则。绑定后，这些规则的 {targetType === 'COUNTERPARTY' ? '关联交易对手' : targetType === 'CATEGORY' ? '关联分类' : '关联账户'} 将被设置为当前项。
          </div>
        </div>
        <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse rounded-b-lg">
            <button
              type="button"
              onClick={handleConfirm}
              className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 text-base font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm"
            >
              保存绑定
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
