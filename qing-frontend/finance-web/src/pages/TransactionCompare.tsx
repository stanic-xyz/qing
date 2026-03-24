import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { getEnumText } from '../utils/enumMap';

export default function TransactionCompare() {
  const [compareGroups, setCompareGroups] = useState([]);
  const [expandedGroupId, setExpandedGroupId] = useState<string | null>(null);

  useEffect(() => {
    fetchCompareData();
  }, []);

  const fetchCompareData = async () => {
    try {
      const res = await axios.get('/api/finance/compare');
      setCompareGroups(res.data.data || []);
    } catch (e) {
      console.error(e);
    }
  };

  const toggleExpand = (groupId: string) => {
    setExpandedGroupId(expandedGroupId === groupId ? null : groupId);
  };

  const CHANNELS = ['ALIPAY', 'WECHAT', 'CMB', 'PINGAN', 'BOC_CREDIT', 'CITIC_CREDIT', 'CCB', 'BOCOM_CREDIT', 'QIANJI', 'JINGDONG'];

  return (
    <div className="flex flex-col h-full">
      <h1 className="text-2xl font-bold mb-6">交易比对</h1>
      <p className="text-gray-500 mb-4 text-sm">此视图将金额和日期完全相同的交易聚合在一起，横向比对不同账单渠道的记录差异。</p>
      
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