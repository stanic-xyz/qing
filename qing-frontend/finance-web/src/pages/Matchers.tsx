import React, {useEffect, useState, useMemo} from 'react';
import axios from 'axios';
import {Plus, Edit2, Trash2, Power, PowerOff, ArrowUpDown, ChevronDown, ChevronRight, Search} from 'lucide-react';
import RuleBuilder from '../components/RuleBuilder';
import TransactionSelector from '../components/TransactionSelector';

interface Counterparty {
    id: number;
    name: string;
    type: string;
}

interface Matcher {
    id?: number;
    name: string;
    description?: string;
    ruleType: string;
    targetType?: string;
    targetId?: number;
    conditionNode: any;
    actionNode: any[];
    priority: number;
    stopOnMatch: boolean;
    isActive: boolean;
    matchCount?: number;
    successCount?: number;
}

export default function Matchers() {
    const [matchers, setMatchers] = useState<Matcher[]>([]);
    const [counterparties, setCounterparties] = useState<Counterparty[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [editingMatcher, setEditingMatcher] = useState<Matcher | null>(null);

    const [expandedGroups, setExpandedGroups] = useState<Record<string, boolean>>({});
    const [sortField, setSortField] = useState<'priority' | 'matchCount' | 'successCount' | 'accuracy'>('priority');
    const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');

    // 测试沙盒相关
    const [showTransactionSelector, setShowTransactionSelector] = useState(false);
    const [testTransaction, setTestTransaction] = useState<any>(null);
    const [testResult, setTestResult] = useState<any>(null);
    const [testing, setTesting] = useState(false);

    useEffect(() => {
        console.info("counterparties", counterparties)
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
            valA = (a.matchCount || 0) === 0 ? 0 : ((a.successCount || 0) / (a.matchCount || 1)) * 100;
            valB = (b.matchCount || 0) === 0 ? 0 : ((b.successCount || 0) / (b.matchCount || 1)) * 100;
        }

        return sortOrder === 'desc' ? valB - valA : valA - valB;
    });

    const groupedMatchers = useMemo(() => {
        const groups: Record<string, Matcher[]> = {};
        sortedMatchers.forEach(m => {
            const type = m.ruleType || 'GENERAL';
            if (!groups[type]) {
                groups[type] = [];
            }
            groups[type].push(m);
        });
        return groups;
    }, [sortedMatchers]);

    const toggleGroup = (type: string) => {
        setExpandedGroups(prev => ({
            ...prev,
            [type]: prev[type] === undefined ? false : !prev[type]
        }));
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
        if (!editingMatcher?.name) {
            alert('请填写规则名称');
            return;
        }
        try {
            if (editingMatcher.id) {
                await axios.put(`/api/finance/matchers/${editingMatcher.id}`, editingMatcher); // Notice: assuming PUT mapping exists or POST handles both. Controller uses POST for save. Let's use POST.
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
            await axios.post(`/api/finance/matchers`, {...matcher, isActive: !matcher.isActive});
            fetchMatchers();
        } catch (e) {
            alert('切换状态失败');
        }
    };

    const openModal = (matcher?: Matcher) => {
        if (matcher) {
            setEditingMatcher({
                ...matcher,
                conditionNode: matcher.conditionNode || {operator: 'AND', children: []},
                actionNode: matcher.actionNode || []
            });
        } else {
            setEditingMatcher({
                name: '',
                description: '',
                ruleType: 'MERCHANT',
                conditionNode: {operator: 'AND', children: []},
                actionNode: [],
                priority: 100,
                stopOnMatch: true,
                isActive: true
            });
        }
        setTestTransaction(null);
        setTestResult(null);
        setShowModal(true);
    };

    const handleTestRule = async () => {
        if (!testTransaction || !editingMatcher) return;
        setTesting(true);
        setTestResult(null);
        try {
            const res = await axios.post('/api/finance/matchers/test', {
                transactionId: testTransaction.id,
                matcher: editingMatcher
            });
            if (res.data.code === 200) {
                setTestResult(res.data.data);
            } else {
                alert(res.data.message || '测试失败');
            }
        } catch (e) {
            alert('接口请求失败');
        } finally {
            setTesting(false);
        }
    };

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">匹配规则管理</h1>
                <button onClick={() => openModal()}
                        className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center">
                    <Plus className="w-4 h-4 mr-2"/> 新增规则
                </button>
            </div>

            <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase cursor-pointer hover:bg-gray-100"
                            onClick={() => toggleSort('priority')}>
                            <div className="flex items-center">优先级 <ArrowUpDown size={12} className="ml-1"/></div>
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">规则名称</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">描述</th>
                        <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase cursor-pointer hover:bg-gray-100"
                            onClick={() => toggleSort('matchCount')}>
                            <div className="flex items-center justify-end">命中数 <ArrowUpDown size={12}
                                                                                               className="ml-1"/></div>
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200 text-sm">
                    {Object.keys(groupedMatchers).map(type => {
                        const isExpanded = expandedGroups[type] !== false; // 默认展开
                        const groupRules = groupedMatchers[type];

                        return (
                            <React.Fragment key={type}>
                                {/* 分组头 */}
                                <tr className="bg-gray-100 cursor-pointer hover:bg-gray-200"
                                    onClick={() => toggleGroup(type)}>
                                    <td colSpan={6}
                                        className="px-6 py-2 text-sm font-semibold text-gray-700 flex items-center">
                                        {isExpanded ? <ChevronDown size={16} className="mr-2"/> :
                                            <ChevronRight size={16} className="mr-2"/>}
                                        {type} <span
                                        className="ml-2 text-xs font-normal text-gray-500">({groupRules.length} 个规则)</span>
                                    </td>
                                </tr>

                                {/* 分组内的规则行 */}
                                {isExpanded && groupRules.map((m) => {
                                    return (
                                        <tr key={m.id}
                                            className={!m.isActive ? 'bg-gray-50 opacity-60' : 'hover:bg-gray-50'}>
                                            <td className="px-6 py-4 whitespace-nowrap">
                          <span
                              className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${m.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                            {m.isActive ? '已启用' : '已停用'}
                          </span>
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap font-medium text-gray-900">{m.priority}</td>
                                            <td className="px-6 py-4">
                                                <div className="font-medium text-gray-900">{m.name}</div>
                                            </td>
                                            <td className="px-6 py-4 text-xs text-gray-500 max-w-[200px] truncate">{m.description || '-'}</td>
                                            <td className="px-6 py-4 whitespace-nowrap text-right">{m.matchCount || 0}</td>
                                            <td className="px-6 py-4 whitespace-nowrap text-left text-sm font-medium">
                                                <div className="flex space-x-3">
                                                    <button onClick={() => openModal(m)}
                                                            className="text-blue-600 hover:text-blue-900"><Edit2
                                                        className="w-4 h-4"/></button>
                                                    <button onClick={() => handleToggleStatus(m)}
                                                            className="text-yellow-600 hover:text-yellow-900">
                                                        {m.isActive ? <PowerOff className="w-4 h-4"/> :
                                                            <Power className="w-4 h-4"/>}
                                                    </button>
                                                    <button onClick={() => handleDelete(m.id!)}
                                                            className="text-red-600 hover:text-red-900"><Trash2
                                                        className="w-4 h-4"/></button>
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
                            <td colSpan={6} className="px-6 py-10 text-center text-gray-500">暂无匹配规则</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>

            {showModal && editingMatcher && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-6xl max-h-[90vh] overflow-y-auto shadow-xl">
                        <div className="flex justify-between items-center mb-6 border-b pb-3">
                            <h3 className="text-xl font-bold">{editingMatcher.id ? '编辑积木式匹配规则' : '新增积木式匹配规则'}</h3>
                        </div>

                        <div className="grid grid-cols-3 gap-6">
                            {/* 左侧：基本配置 & 动作配置 */}
                            <div className="col-span-2 space-y-6">
                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">规则类型</label>
                                        <select
                                            value={editingMatcher.ruleType || 'MERCHANT'}
                                            onChange={(e) => setEditingMatcher({
                                                ...editingMatcher,
                                                ruleType: e.target.value
                                            })}
                                            className="w-full border-gray-300 rounded p-2 text-sm"
                                        >
                                            <option value="CHANNEL">渠道匹配</option>
                                            <option value="MERCHANT">商户匹配</option>
                                            <option value="COUNTERPARTY">对手账户匹配</option>
                                            <option value="INTERNAL_TRANSFER">内部转账匹配</option>
                                            <option value="CUSTOM">自定义通用匹配</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">规则名称
                                            *</label>
                                        <input type="text" value={editingMatcher.name || ''}
                                               onChange={e => setEditingMatcher({
                                                   ...editingMatcher,
                                                   name: e.target.value
                                               })} className="w-full border-gray-300 rounded p-2 text-sm"
                                               placeholder="如：银联识别高德打车"/>
                                    </div>
                                    <div className="col-span-2">
                                        <label className="block text-sm font-medium text-gray-700 mb-1">规则描述</label>
                                        <input type="text" value={editingMatcher.description || ''}
                                               onChange={e => setEditingMatcher({
                                                   ...editingMatcher,
                                                   description: e.target.value
                                               })} className="w-full border-gray-300 rounded p-2 text-sm"
                                               placeholder="对该规则的简单描述"/>
                                    </div>
                                </div>

                                <RuleBuilder
                                    conditionNode={editingMatcher.conditionNode}
                                    onChangeCondition={(node) => setEditingMatcher({
                                        ...editingMatcher,
                                        conditionNode: node
                                    })}
                                    actionNode={editingMatcher.actionNode}
                                    onChangeAction={(actions) => setEditingMatcher({
                                        ...editingMatcher,
                                        actionNode: actions
                                    })}
                                />
                            </div>

                            {/* 右侧：属性 & 测试 */}
                            <div className="col-span-1 space-y-6 border-l pl-6">
                                <div className="space-y-4">
                                    <h4 className="font-semibold text-gray-700 border-b pb-2">规则属性</h4>
                                    <div className="text-sm">
                                        <span
                                            className="font-medium text-gray-700 block mb-1">所属目标 (多态绑定)</span>
                                        {editingMatcher.targetType && editingMatcher.targetId ? (
                                            <div
                                                className="bg-gray-50 border border-gray-200 rounded px-3 py-2 flex items-center justify-between">
                        <span className="text-blue-600 font-medium">
                          {editingMatcher.targetType === 'COUNTERPARTY' ? '交易对手' :
                              editingMatcher.targetType === 'CATEGORY' ? '分类' :
                                  editingMatcher.targetType === 'ACCOUNT' ? '账户' : editingMatcher.targetType}
                            (ID: {editingMatcher.targetId})
                        </span>
                                                <span className="text-xs text-gray-500 ml-2">(由各实体页面分配)</span>
                                            </div>
                                        ) : (
                                            <div
                                                className="bg-gray-50 border border-gray-200 rounded px-3 py-2 text-gray-500 italic">
                                                全局通用规则 (未绑定特定实体)
                                            </div>
                                        )}
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">优先级
                                            (越大越优先)</label>
                                        <input type="number" value={editingMatcher.priority || 0}
                                               onChange={e => setEditingMatcher({
                                                   ...editingMatcher,
                                                   priority: parseInt(e.target.value)
                                               })} className="w-full border-gray-300 rounded p-2 text-sm"/>
                                    </div>
                                    <div className="flex flex-col gap-2 pt-2">
                                        <label className="flex items-center cursor-pointer">
                                            <input type="checkbox" checked={editingMatcher.stopOnMatch}
                                                   onChange={e => setEditingMatcher({
                                                       ...editingMatcher,
                                                       stopOnMatch: e.target.checked
                                                   })} className="text-blue-600 rounded mr-2"/>
                                            <span className="text-sm text-gray-700">命中后阻断 (阻止低优规则)</span>
                                        </label>
                                        <label className="flex items-center cursor-pointer">
                                            <input type="checkbox" checked={editingMatcher.isActive}
                                                   onChange={e => setEditingMatcher({
                                                       ...editingMatcher,
                                                       isActive: e.target.checked
                                                   })} className="text-blue-600 rounded mr-2"/>
                                            <span className="text-sm text-gray-700">启用规则</span>
                                        </label>
                                    </div>
                                </div>

                                <div className="pt-4 border-t border-gray-200">
                                    <h4 className="font-semibold text-gray-700 mb-2">沙盒测试</h4>
                                    <div className="bg-gray-50 p-3 rounded border border-gray-200 mb-3">
                                        {testTransaction ? (
                                            <div className="text-sm space-y-1">
                                                <div className="font-medium truncate"
                                                     title={testTransaction.merchant || testTransaction.counterparty}>{testTransaction.merchant || testTransaction.counterparty || '无对手方'}</div>
                                                <div className="text-gray-500 flex justify-between">
                                                    <span>{testTransaction.transactionTime?.split(' ')[0]}</span>
                                                    <span
                                                        className="font-medium text-gray-900">{testTransaction.amount}</span>
                                                </div>
                                                <button onClick={() => setShowTransactionSelector(true)}
                                                        className="text-blue-600 text-xs mt-2 hover:underline">重新选择
                                                </button>
                                            </div>
                                        ) : (
                                            <div className="text-center py-4">
                                                <button onClick={() => setShowTransactionSelector(true)}
                                                        className="text-blue-600 text-sm flex items-center justify-center w-full hover:underline">
                                                    <Search className="w-4 h-4 mr-1"/> 选择一条流水进行测试
                                                </button>
                                            </div>
                                        )}
                                    </div>

                                    <button
                                        onClick={handleTestRule}
                                        disabled={!testTransaction || testing}
                                        className={`w-full py-2 rounded text-sm text-white ${!testTransaction || testing ? 'bg-gray-400 cursor-not-allowed' : 'bg-gray-800 hover:bg-gray-900'}`}
                                    >
                                        {testing ? '测试中...' : '运行测试'}
                                    </button>

                                    {testResult && (
                                        <div
                                            className={`mt-3 p-3 rounded text-sm ${testResult.matched ? 'bg-green-50 border border-green-200' : 'bg-red-50 border border-red-200'}`}>
                                            {testResult.matched ? (
                                                <div>
                                                    <p className="font-bold text-green-800 mb-2">✅ 匹配成功！</p>
                                                    <div className="space-y-1 text-xs text-gray-700">
                                                        <p>
                                                            <strong>修改前:</strong> {testResult.originalRecord.type} | {testResult.originalRecord.category || '无分类'}
                                                        </p>
                                                        <p>
                                                            <strong>修改后:</strong> {testResult.modifiedRecord.type} | {testResult.modifiedRecord.category || '无分类'}
                                                        </p>
                                                        {testResult.modifiedRecord.counterparty !== testResult.originalRecord.counterparty && (
                                                            <p>
                                                                <strong>新对手:</strong> {testResult.modifiedRecord.counterparty}
                                                            </p>
                                                        )}
                                                    </div>
                                                </div>
                                            ) : (
                                                <p className="font-bold text-red-800">❌ 未命中该规则</p>
                                            )}
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>

                        <div className="mt-8 flex justify-end space-x-3 border-t pt-4">
                            <button onClick={() => setShowModal(false)}
                                    className="px-5 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50">取消
                            </button>
                            <button onClick={handleSave}
                                    className="px-5 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700">保存规则
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <TransactionSelector
                isOpen={showTransactionSelector}
                onClose={() => setShowTransactionSelector(false)}
                onSelect={(record) => {
                    setTestTransaction(record);
                    setShowTransactionSelector(false);
                    setTestResult(null);
                }}
            />
        </div>
    );
}
