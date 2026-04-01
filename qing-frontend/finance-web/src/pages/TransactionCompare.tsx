import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { getEnumText } from '../utils/enumMap';
import { Search, RotateCcw } from 'lucide-react';

export default function TransactionCompare() {
  const [compareGroups, setCompareGroups] = useState([]);
  const [expandedGroupId, setExpandedGroupId] = useState<string | null>(null);
  const [accounts, setAccounts] = useState<any[]>([]);

  const [filters, setFilters] = useState({
    startDate: '',
    endDate: '',
    minAmount: '',
    maxAmount: '',
    type: '',
    compareStatus: 'MATCHED',
    channel: '',
    accountId: ''
  });

  useEffect(() => {
    fetchCompareData();
    fetchAccounts();
  }, []);

  const fetchAccounts = async () => {
    try {
      const res = await axios.get('/api/finance/accounts');
      setAccounts(res.data.data || []);
    } catch (e) {
      console.error(e);
    }
  };

  const fetchCompareData = async (currentFilters = filters) => {
    try {
      const params = new URLSearchParams();
      Object.entries(currentFilters).forEach(([key, value]) => {
        if (value !== '') {
          params.append(key, String(value));
        }
      });
      const res = await axios.get(`/api/finance/compare?${params.toString()}`);
      setCompareGroups(res.data.data || []);
      setExpandedGroupId(null);
    } catch (e) {
      console.error(e);
    }
  };

  const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const handleSearch = () => {
    fetchCompareData(filters);
  };

  const handleReset = () => {
    const defaultFilters = {
      startDate: '',
      endDate: '',
      minAmount: '',
      maxAmount: '',
      type: '',
      compareStatus: 'MATCHED',
      channel: '',
      accountId: ''
    };
    setFilters(defaultFilters);
    fetchCompareData(defaultFilters);
  };

  const toggleExpand = (groupId: string) => {
    setExpandedGroupId(expandedGroupId === groupId ? null : groupId);
  };

  const CHANNELS = ['ALIPAY', 'WECHAT', 'CMB', 'PINGAN', 'BOC_CREDIT', 'CITIC_CREDIT', 'CCB', 'BOCOM_CREDIT', 'QIANJI', 'JINGDONG'];

  return (
    <div className="flex flex-col h-full gap-4">
      <div>
        <h1 className="text-2xl font-bold mb-2">对账工具</h1>
        <p className="text-gray-500 text-sm">此视图将金额和日期完全相同的交易聚合在一起，横向比对不同账单渠道的记录差异。</p>
      </div>

      {/* 高级过滤栏 */}
      <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-100">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label className="block text-xs font-medium text-gray-700 mb-1">开始日期</label>
            <input type="date" name="startDate" value={filters.startDate} onChange={handleFilterChange} className="w-full border-gray-300 rounded-md shadow-sm p-2 text-sm border focus:ring-blue-500 focus:border-blue-500" />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-700 mb-1">结束日期</label>
            <input type="date" name="endDate" value={filters.endDate} onChange={handleFilterChange} className="w-full border-gray-300 rounded-md shadow-sm p-2 text-sm border focus:ring-blue-500 focus:border-blue-500" />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-700 mb-1">最小金额</label>
            <input type="number" name="minAmount" value={filters.minAmount} onChange={handleFilterChange} placeholder="￥0.00" className="w-full border-gray-300 rounded-md shadow-sm p-2 text-sm border focus:ring-blue-500 focus:border-blue-500" />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-700 mb-1">最大金额</label>
            <input type="number" name="maxAmount" value={filters.maxAmount} onChange={handleFilterChange} placeholder="￥9999.00" className="w-full border-gray-300 rounded-md shadow-sm p-2 text-sm border focus:ring-blue-500 focus:border-blue-500" />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-700 mb-1">收支类型</label>
            <select name="type" value={filters.type} onChange={handleFilterChange} className="w-full border-gray-300 rounded-md shadow-sm p-2 text-sm border focus:ring-blue-500 focus:border-blue-500">
              <option value="">全部</option>
              <option value="EXPENSE">支出</option>
              <option value="INCOME">收入</option>
              <option value="TRANSFER">转账</option>
            </select>
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-700 mb-1">对比状态</label>
            <select name="compareStatus" value={filters.compareStatus} onChange={handleFilterChange} className="w-full border-gray-300 rounded-md shadow-sm p-2 text-sm border focus:ring-blue-500 focus:border-blue-500">
              <option value="ALL">全部 (含单边孤立账单)</option>
              <option value="MATCHED">仅看已匹配 (&gt;=2个渠道)</option>
              <option value="UNMATCHED">仅看未匹配 (单边孤立账单)</option>
            </select>
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-700 mb-1">包含渠道</label>
            <select name="channel" value={filters.channel} onChange={handleFilterChange} className="w-full border-gray-300 rounded-md shadow-sm p-2 text-sm border focus:ring-blue-500 focus:border-blue-500">
              <option value="">不限</option>
              {CHANNELS.map(ch => <option key={ch} value={ch}>{getEnumText('channel', ch)}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-700 mb-1">包含账户</label>
            <select name="accountId" value={filters.accountId} onChange={handleFilterChange} className="w-full border-gray-300 rounded-md shadow-sm p-2 text-sm border focus:ring-blue-500 focus:border-blue-500">
              <option value="">不限</option>
              {accounts.map(acc => <option key={acc.id} value={acc.id}>{acc.accountName}</option>)}
            </select>
          </div>
        </div>
        <div className="mt-4 flex justify-end space-x-3">
          <button onClick={handleReset} className="flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none">
            <RotateCcw className="w-4 h-4 mr-2" />
            重置
          </button>
          <button onClick={handleSearch} className="flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none">
            <Search className="w-4 h-4 mr-2" />
            查询
          </button>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-100 flex-1 overflow-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50 sticky top-0 z-10">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">基础信息 (日期/金额)</th>
              {CHANNELS.map(ch => (
                <th key={ch} className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">{getEnumText('channel', ch)}</th>
              ))}
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {compareGroups.map((group: any) => {
              const isExpanded = expandedGroupId === group.groupId;
              return (
                <React.Fragment key={group.groupId}>
                  {/* 主行 */}
                  <tr
                    className={`hover:bg-gray-50 cursor-pointer ${isExpanded ? 'bg-blue-50' : ''}`}
                    onClick={() => toggleExpand(group.groupId)}
                  >
                    <td className="px-4 py-4 whitespace-nowrap border-b border-gray-100">
                      <div className="text-sm font-medium text-gray-900">{group.mainDate}</div>
                      <div className={`text-sm font-bold mt-1 ${group.mainType === 'INCOME' ? 'text-green-600' : 'text-red-600'}`}>
                        {group.mainAmount}
                      </div>
                    </td>
                    {CHANNELS.map(ch => {
                      const hasRecord = !!group.channelRecords[ch];
                      return (
                        <td key={ch} className="px-4 py-4 border-b border-gray-100">
                          {hasRecord ? (
                            <span className="inline-block px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full">有记录</span>
                          ) : (
                            <span className="text-gray-300 text-sm">-</span>
                          )}
                        </td>
                      );
                    })}
                  </tr>

                  {/* 展开行（详情列表） */}
                  {isExpanded && (
                    <tr>
                      <td colSpan={CHANNELS.length + 1} className="p-0 border-b-2 border-blue-200 bg-gray-50">
                        <div className="p-4">
                          <h4 className="text-sm font-bold text-gray-700 mb-3 pl-2 border-l-4 border-blue-500">详细渠道数据比对</h4>
                          <table className="min-w-full bg-white border border-gray-200 rounded-md overflow-hidden shadow-sm">
                            <thead className="bg-gray-100">
                              <tr>
                                <th className="px-4 py-2 text-left text-xs font-medium text-gray-600 uppercase">渠道</th>
                                <th className="px-4 py-2 text-left text-xs font-medium text-gray-600 uppercase">时间</th>
                                <th className="px-4 py-2 text-left text-xs font-medium text-gray-600 uppercase">对方</th>
                                <th className="px-4 py-2 text-left text-xs font-medium text-gray-600 uppercase">说明/备注</th>
                              </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                              {Object.entries(group.channelRecords).map(([ch, record]: [string, any]) => (
                                <tr key={ch} className="hover:bg-gray-50">
                                  <td className="px-4 py-3 whitespace-nowrap text-sm font-medium text-blue-700">{getEnumText('channel', ch)}</td>
                                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-600">{record.transactionTime.split('T')[1]}</td>
                                  <td className="px-4 py-3 text-sm text-gray-800 max-w-xs truncate" title={record.counterparty}>{record.counterparty || '-'}</td>
                                  <td className="px-4 py-3 text-sm text-gray-600">
                                    <div className="max-w-md truncate" title={record.merchant}>说明: {record.merchant || '-'}</div>
                                    {record.remark && <div className="max-w-md truncate text-gray-400 mt-1" title={record.remark}>备注: {record.remark}</div>}
                                  </td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                        </div>
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              );
            })}
            {compareGroups.length === 0 && (
              <tr>
                <td colSpan={CHANNELS.length + 1} className="px-4 py-8 text-center text-gray-500">
                  暂无匹配的横向比对数据
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
