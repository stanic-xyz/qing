import React, { useEffect, useState, useMemo } from 'react';
import axios from 'axios';
import { Plus, Edit2, Trash2, Power, PowerOff, ArrowUpDown, ChevronDown, ChevronRight } from 'lucide-react';
import { CHANNELS } from '../config/channels';

interface Counterparty {
  id: number;
  name: string;
  type: string;
}

interface Matcher {
  id: number;
  name: string;
  sourceChannel: string;
  matchRegex: string;
  setType: string;
  targetMerchant: string;
  targetCounterparty: string;
  targetCategory: string;
  targetAccountKeyword: string;
  ruleType: string;
  counterpartyId?: number;
  priority: number;
  isActive: boolean;
  matchCount: number;
  successCount: number;
}

export default function Matchers() {
  const [matchers, setMatchers] = useState<Matcher[]>([]);
  const [counterparties, setCounterparties] = useState<Counterparty[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [editingMatcher, setEditingMatcher] = useState<Partial<Matcher> | null>(null);

  // 分组展开状态
  const [expandedGroups, setExpandedGroups] = useState<Record<string, boolean>>({});

  // 排序状态
  const [sortField, setSortField] = useState<'priority' | 'matchCount' | 'successCount' | 'accuracy'>('priority');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');

  // Regex testing
  const [testText, setTestText] = useState('');
  const [testResult, setTestResult] = useState<any>(null);

  useEffect(() => {
    fetchMatchers();
    fetchCounterparties();
  }, []);

  const fetchCounterparties = async () => {
    try {
      const res = await axios.get('/api/finance/counterparties/active');
      if (res.data.code === 200) {
        setCounterparties(res.data.data || []);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const fetchMatchers = async () => {
    try {
      const res = await axios.get('/api/finance/matchers');
      if (res.data.code === 200) {
        setMatchers(res.data.data || []);
      }
    } catch (e) {
      console.error(e);
    }
  };

  // 排序处理
  const sortedMatchers = [...matchers].sort((a, b) => {
    let valA = 0;
    let valB = 0;

    if (sortField === 'priority') {
      valA = a.priority || 0;
      valB = b.priority || 0;
    } else if (sortField === 'matchCount') {
      valA = a.matchCount || 0;
      valB = b.matchCount || 0;
    } else if (sortField === 'successCount') {
      valA = a.successCount || 0;
      valB = b.successCount || 0;
    } else if (sortField === 'accuracy') {
      valA = (a.matchCount || 0) === 0 ? 0 : ((a.successCount || 0) / a.matchCount) * 100;
      valB = (b.matchCount || 0) === 0 ? 0 : ((b.successCount || 0) / b.matchCount) * 100;
    }

    return sortOrder === 'desc' ? valB - valA : valA - valB;
  });

  // 按来源渠道分组
  const groupedMatchers = useMemo(() => {
    const groups: Record<string, Matcher[]> = {};
    sortedMatchers.forEach(m => {
      const channel = m.sourceChannel || 'GENERAL';
      if (!groups[channel]) {
        groups[channel] = [];
      }
      groups[channel].push(m);
    });
    return groups;
  }, [sortedMatchers]);

  const toggleGroup = (channel: string) => {
    setExpandedGroups(prev => ({
      ...prev,
      [channel]: prev[channel] === undefined ? false : !prev[channel]
    }));
  };

  const getChannelName = (key: string) => {
    if (key === 'GENERAL') return '通用 (不限渠道)';
    return CHANNELS[key]?.name || key;
  };

  const toggleSort = (field: 'priority' | 'matchCount' | 'successCount' | 'accuracy') => {
    if (sortField === field) {
      setSortOrder(sortOrder === 'desc' ? 'asc' : 'desc');
    } else {
      setSortField(field);
      setSortOrder('desc');
    }
  };

  const handleSave = async () => {
    if (!editingMatcher?.name || !editingMatcher?.matchRegex) {
      alert('请填写规则名称和匹配正则');
      return;
    }
    try {
      if (editingMatcher.id) {
        await axios.put(`/api/finance/matchers/${editingMatcher.id}`, editingMatcher);
      } else {
        await axios.post('/api/finance/matchers', editingMatcher);
      }
      setShowModal(false);
      fetchMatchers();
    } catch (e) {
      alert('保存失败');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('确认删除此规则吗？')) {
      try {
        await axios.delete(`/api/finance/matchers/${id}`);
        fetchMatchers();
      } catch (e) {
        alert('删除失败');
      }
    }
  };

  const handleToggleStatus = async (matcher: Matcher) => {
    try {
      await axios.put(`/api/finance/matchers/${matcher.id}`, { ...matcher, isActive: !matcher.isActive });
      fetchMatchers();
    } catch (e) {
      alert('切换状态失败');
    }
  };

  const openModal = (matcher: Partial<Matcher> = { name: '', matchRegex: '', isActive: true, priority: 100, ruleType: 'MERCHANT' }) => {
    setEditingMatcher(matcher);
    setTestText('');
    setTestResult(null);
    setShowModal(true);
  };

  const handleTestRegex = () => {
    if (!editingMatcher?.matchRegex || !testText) return;
    try {
      const regex = new RegExp(editingMatcher.matchRegex);
      const match = regex.exec(testText);
      if (match) {
        setTestResult({
          matched: true,
          groups: match.groups || {},
          fullMatch: match[0]
        });
      } else {
        setTestResult({ matched: false });
      }
    } catch (e: any) {
      setTestResult({ error: e.message });
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">匹配规则管理</h1>
        <button onClick={() => openModal()} className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center">
          <Plus className="w-4 h-4 mr-2" /> 新增规则
        </button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase cursor-pointer hover:bg-gray-100" onClick={() => toggleSort('priority')}>
                <div className="flex items-center">优先级 <ArrowUpDown size={12} className="ml-1"/></div>
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">规则名称</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">匹配正则</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">强制类型</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase cursor-pointer hover:bg-gray-100" onClick={() => toggleSort('matchCount')}>
                <div className="flex items-center justify-end">命中数 <ArrowUpDown size={12} className="ml-1"/></div>
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase cursor-pointer hover:bg-gray-100" onClick={() => toggleSort('successCount')}>
                <div className="flex items-center justify-end">成功数 <ArrowUpDown size={12} className="ml-1"/></div>
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase cursor-pointer hover:bg-gray-100" onClick={() => toggleSort('accuracy')}>
                <div className="flex items-center justify-end">正确率 <ArrowUpDown size={12} className="ml-1"/></div>
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200 text-sm">
            {Object.keys(groupedMatchers).map(channel => {
              const isExpanded = expandedGroups[channel] !== false; // 默认展开
              const groupRules = groupedMatchers[channel];

              return (
                <React.Fragment key={channel}>
                  {/* 分组头 */}
                  <tr className="bg-gray-100 cursor-pointer hover:bg-gray-200" onClick={() => toggleGroup(channel)}>
                    <td colSpan={9} className="px-6 py-2 text-sm font-semibold text-gray-700 flex items-center">
                      {isExpanded ? <ChevronDown size={16} className="mr-2" /> : <ChevronRight size={16} className="mr-2" />}
                      {getChannelName(channel)} <span className="ml-2 text-xs font-normal text-gray-500">({groupRules.length} 个规则)</span>
                    </td>
                  </tr>

                  {/* 分组内的规则行 */}
                  {isExpanded && groupRules.map((m) => {
                    const accuracy = m.matchCount > 0 ? ((m.successCount / m.matchCount) * 100).toFixed(1) : '-';
                    const counterpartyName = counterparties.find(c => c.id === m.counterpartyId)?.name;
                    return (
                      <tr key={m.id} className={!m.isActive ? 'bg-gray-50 opacity-60' : 'hover:bg-gray-50'}>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${m.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                            {m.isActive ? '已启用' : '已停用'}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap font-medium text-gray-900">{m.priority}</td>
                        <td className="px-6 py-4">
                          <div className="font-medium text-gray-900">{m.name}</div>
                          <div className="text-xs text-gray-500 mt-1">
                            {m.ruleType === 'MERCHANT' ? '商户规则' : m.ruleType === 'COUNTERPARTY' ? '对手规则' : m.ruleType === 'INTERNAL_TRANSFER' ? '转账规则' : m.ruleType}
                          </div>
                        </td>
                        <td className="px-6 py-4 font-mono text-xs text-gray-600 break-all">{m.matchRegex}</td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          {m.setType ? <div className="text-xs bg-gray-100 px-2 py-1 rounded inline-block mb-1">类型: {m.setType}</div> : null}
                          {m.counterpartyId ? <div className="text-xs bg-blue-50 text-blue-700 px-2 py-1 rounded inline-block">对手: {counterpartyName}</div> : null}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-right">{m.matchCount || 0}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-right">{m.successCount || 0}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-right font-medium text-blue-600">{accuracy === '-' ? '-' : `${accuracy}%`}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                          <div className="flex space-x-3">
                            <button onClick={() => openModal(m)} className="text-blue-600 hover:text-blue-900"><Edit2 className="w-4 h-4" /></button>
                            <button onClick={() => handleToggleStatus(m)} className="text-yellow-600 hover:text-yellow-900">
                              {m.isActive ? <PowerOff className="w-4 h-4" /> : <Power className="w-4 h-4" />}
                            </button>
                            <button onClick={() => handleDelete(m.id)} className="text-red-600 hover:text-red-900"><Trash2 className="w-4 h-4" /></button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </React.Fragment>
              );
            })}
            {Object.keys(groupedMatchers).length === 0 && (
              <tr>
                <td colSpan={9} className="px-6 py-10 text-center text-gray-500">暂无匹配规则</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && editingMatcher && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-3xl max-h-[90vh] overflow-y-auto shadow-xl">
            <h3 className="text-xl font-bold mb-6">{editingMatcher.id ? '编辑匹配规则' : '新增匹配规则'}</h3>

            <div className="grid grid-cols-2 gap-6">
              {/* 左侧：基本配置 */}
              <div className="space-y-4">
                <h4 className="font-semibold text-gray-700 border-b pb-2">触发条件</h4>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">规则类型</label>
                  <select
                    value={editingMatcher?.ruleType || 'MERCHANT'}
                    onChange={(e) => setEditingMatcher({ ...editingMatcher, ruleType: e.target.value })}
                    className="w-full border-gray-300 rounded p-2 text-sm"
                  >
                    <option value="CHANNEL">渠道匹配</option>
                    <option value="MERCHANT">商户匹配</option>
                    <option value="COUNTERPARTY">对手账户匹配</option>
                    <option value="INTERNAL_TRANSFER">内部转账匹配</option>
                    <option value="CUSTOM">自定义通用匹配</option>
                  </select>
                </div>

                {(editingMatcher?.ruleType === 'COUNTERPARTY' || editingMatcher?.ruleType === 'MERCHANT') && (
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">关联交易对手</label>
                    <select
                      value={editingMatcher?.counterpartyId || ''}
                      onChange={(e) => setEditingMatcher({ ...editingMatcher, counterpartyId: e.target.value ? Number(e.target.value) : undefined })}
                      className="w-full border-gray-300 rounded p-2 text-sm"
                    >
                      <option value="">-- 请选择 --</option>
                      {counterparties.map(cp => (
                        <option key={cp.id} value={cp.id}>{cp.name} ({cp.type === 'MERCHANT' ? '商户' : '个人'})</option>
                      ))}
                    </select>
                    <p className="text-xs text-gray-500 mt-1">关联后，命中该规则的流水将直接归属给此交易对手。</p>
                  </div>
                )}

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">规则名称 *</label>
                  <input type="text" value={editingMatcher.name || ''} onChange={e => setEditingMatcher({...editingMatcher, name: e.target.value})} className="w-full border-gray-300 rounded p-2 text-sm" placeholder="如：银联识别高德打车" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">匹配正则表达式 * (支持命名捕获组如 ?&lt;merchant&gt;)</label>
                  <textarea value={editingMatcher.matchRegex || ''} onChange={e => setEditingMatcher({...editingMatcher, matchRegex: e.target.value})} className="w-full border-gray-300 rounded p-2 text-sm font-mono" rows={3} placeholder=".*高德打车.*云闪付.*" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">指定来源渠道 (为空则不限)</label>
                  <select value={editingMatcher.sourceChannel || ''} onChange={e => setEditingMatcher({...editingMatcher, sourceChannel: e.target.value})} className="w-full border-gray-300 rounded p-2 text-sm">
                    <option value="">全部渠道</option>
                    {Object.keys(CHANNELS).map(key => <option key={key} value={key}>{CHANNELS[key].name}</option>)}
                  </select>
                </div>
                <div className="flex space-x-4">
                  <div className="w-1/2">
                    <label className="block text-sm font-medium text-gray-700 mb-1">优先级</label>
                    <input type="number" value={editingMatcher.priority || 0} onChange={e => setEditingMatcher({...editingMatcher, priority: parseInt(e.target.value)})} className="w-full border-gray-300 rounded p-2 text-sm" />
                  </div>
                  <div className="w-1/2 flex items-end pb-2">
                    <label className="flex items-center cursor-pointer">
                      <input type="checkbox" checked={editingMatcher.isActive} onChange={e => setEditingMatcher({...editingMatcher, isActive: e.target.checked})} className="text-blue-600 rounded" />
                      <span className="ml-2 text-sm text-gray-700">启用规则</span>
                    </label>
                  </div>
                </div>
              </div>

              {/* 右侧：动作与测试 */}
              <div className="space-y-4">
                <h4 className="font-semibold text-gray-700 border-b pb-2">执行动作</h4>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">强制设置收支类型</label>
                  <select value={editingMatcher.setType || ''} onChange={e => setEditingMatcher({...editingMatcher, setType: e.target.value})} className="w-full border-gray-300 rounded p-2 text-sm">
                    <option value="">不修改</option>
                    <option value="EXPENSE">支出 (EXPENSE)</option>
                    <option value="INCOME">收入 (INCOME)</option>
                    <option value="TRANSFER">内部转账 (TRANSFER)</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">提取商户/平台</label>
                  <input type="text" value={editingMatcher.targetMerchant || ''} onChange={e => setEditingMatcher({...editingMatcher, targetMerchant: e.target.value})} className="w-full border-gray-300 rounded p-2 text-sm" placeholder="固定值，或依赖正则组提取" />
                </div>
                {editingMatcher.setType === 'TRANSFER' && (
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">目标账户关键字 (卡号/姓名)</label>
                    <input type="text" value={editingMatcher.targetAccountKeyword || ''} onChange={e => setEditingMatcher({...editingMatcher, targetAccountKeyword: e.target.value})} className="w-full border-gray-300 rounded p-2 text-sm" placeholder="如提取到 6214... 将自动寻找对方账户" />
                  </div>
                )}

                <div className="mt-6 pt-4 border-t border-gray-200">
                  <h4 className="font-semibold text-gray-700 mb-2">沙盒测试</h4>
                  <div className="flex space-x-2">
                    <input type="text" value={testText} onChange={e => setTestText(e.target.value)} placeholder="输入待测试的原始交易描述..." className="flex-1 border-gray-300 rounded p-2 text-sm" />
                    <button onClick={handleTestRegex} className="px-3 py-2 bg-gray-800 text-white rounded text-sm hover:bg-gray-900">测试</button>
                  </div>
                  {testResult && (
                    <div className={`mt-2 p-3 rounded text-sm ${testResult.matched ? 'bg-green-50 text-green-800 border border-green-200' : testResult.error ? 'bg-red-50 text-red-800' : 'bg-yellow-50 text-yellow-800'}`}>
                      {testResult.error ? `正则错误: ${testResult.error}` :
                       testResult.matched ? (
                         <div>
                           <p className="font-bold mb-1">✅ 匹配成功！</p>
                           <p>完整匹配: <span className="font-mono bg-white px-1 rounded">{testResult.fullMatch}</span></p>
                           {Object.keys(testResult.groups).length > 0 && (
                             <div className="mt-1">捕获组: <pre className="text-xs bg-white p-1 rounded mt-1">{JSON.stringify(testResult.groups, null, 2)}</pre></div>
                           )}
                         </div>
                       ) : '❌ 未匹配'}
                    </div>
                  )}
                </div>
              </div>
            </div>

            <div className="mt-8 flex justify-end space-x-3 border-t pt-4">
              <button onClick={() => setShowModal(false)} className="px-5 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50">取消</button>
              <button onClick={handleSave} className="px-5 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700">保存规则</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
