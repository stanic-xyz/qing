import React, {useEffect, useState, useMemo} from 'react';
import axios from 'axios';
import {Plus, Edit2, Trash2, Power, PowerOff, ArrowUpDown, ChevronDown, ChevronRight} from 'lucide-react';
import RuleEditorModal from '../components/RuleEditorModal';
import {getEnumText} from '../utils/enumMap';

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
    const [categories, setCategories] = useState<any[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [editingMatcher, setEditingMatcher] = useState<Matcher | null>(null);

    const [expandedGroups, setExpandedGroups] = useState<Record<string, boolean>>({});
    const [sortField, setSortField] = useState<'priority' | 'matchCount' | 'successCount' | 'accuracy'>('priority');
    const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');

    const fetchCategories = async () => {
        try {
            const res = await axios.get('/api/categories');
            if (res.data.code === 200) {
                const flatten = (arr: any[]): any[] => {
                    let result: any[] = [];
                    arr.forEach(c => {
                        result.push(c);
                        if (c.children && c.children.length > 0) {
                            result = result.concat(flatten(c.children));
                        }
                    });
                    return result;
                };
                setCategories(flatten(res.data.data || []));
            }
        } catch (e) {
            console.error(e);
        }
    };

    useEffect(() => {
        console.info("counterparties", counterparties)
        fetchMatchers();
        fetchCounterparties();
        fetchCategories();
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

    const getTargetDisplay = (m: Matcher) => {
        if (m.targetType === 'CATEGORY') {
            const cat = categories.find(c => c.id === m.targetId);
            return {
                badge: <span className="px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full whitespace-nowrap">分类规则</span>,
                text: `分类: ${cat ? cat.name : m.targetId || '-'}`
            };
        }
        if (m.targetType === 'COUNTERPARTY') {
            const cp = counterparties.find(c => c.id === m.targetId);
            if (cp?.type === 'MERCHANT') {
                return {
                    badge: <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full whitespace-nowrap">商户规则</span>,
                    text: `商户: ${cp.name}`
                };
            }
            return {
                badge: <span className="px-2 py-1 bg-orange-100 text-orange-800 text-xs rounded-full whitespace-nowrap">交易对手规则</span>,
                text: `交易对手: ${cp ? cp.name : m.targetId || '-'}`
            };
        }
        return {
            badge: <span className="px-2 py-1 bg-gray-100 text-gray-800 text-xs rounded-full whitespace-nowrap">全局规则</span>,
            text: '全局'
        };
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
        setShowModal(true);
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
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">类型与目标</th>
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
                                    <td colSpan={7}
                                        className="px-6 py-2 text-sm font-semibold text-gray-700 flex items-center">
                                        {isExpanded ? <ChevronDown size={16} className="mr-2"/> :
                                            <ChevronRight size={16} className="mr-2"/>}
                                        {getEnumText('ruleTypeEnum', type)} <span
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
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <div className="flex flex-col gap-1">
                                                    {getTargetDisplay(m).badge}
                                                    <span className="text-xs text-gray-500">{getTargetDisplay(m).text}</span>
                                                </div>
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
                            <td colSpan={7} className="px-6 py-10 text-center text-gray-500">暂无匹配规则</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>

            {showModal && editingMatcher && (
                <RuleEditorModal
                    rule={editingMatcher}
                    onClose={() => setShowModal(false)}
                    onSaved={() => {
                        setShowModal(false);
                        fetchMatchers();
                    }}
                />
            )}

        </div>
    );
}
