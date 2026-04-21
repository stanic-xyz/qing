import {useState, useEffect} from 'react';
import axios from 'axios';
import {Plus, Edit2, Trash2, ChevronDown, ChevronRight, Link, Settings} from 'lucide-react';
import RuleSelector from '../components/RuleSelector';
import RuleManagementPanel from '../components/RuleManagementPanel';

interface Category {
    id: number;
    name: string;
    parentId: number | null;
    level: number;
    type: string;
    icon?: string;
    children?: Category[];
}

export default function Categories() {
    const [categories, setCategories] = useState<Category[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [editingCategory, setEditingCategory] = useState<Category | null>(null);
    const [expandedRowKeys, setExpandedRowKeys] = useState<Set<number>>(new Set());

    // 规则绑定相关状态
    const [showRuleSelector, setShowRuleSelector] = useState(false);
    const [bindingItem, setBindingItem] = useState<Category | null>(null);
    const [boundRuleIds, setBoundRuleIds] = useState<number[]>([]);

    const [showRulePanelFor, setShowRulePanelFor] = useState<Category | null>(null);

    // Form states
    const [formData, setFormData] = useState({
        name: '',
        parentId: undefined as number | undefined,
        type: 'EXPENSE',
        icon: ''
    });

    const fetchCategories = async () => {
        setLoading(true);
        try {
            const res = await axios.get('/api/categories/tree');
            if (res.data.code === 200) {
                setCategories(res.data.data);
            }
        } catch (error) {
            alert('获取分类列表失败');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const handleAdd = (parentId?: number) => {
        setEditingCategory(null);
        setFormData({
            name: '',
            parentId: parentId,
            type: 'EXPENSE',
            icon: ''
        });
        setIsModalVisible(true);
    };

    const handleEdit = (record: Category) => {
        setEditingCategory(record);
        setFormData({
            name: record.name,
            parentId: record.parentId || undefined,
            type: record.type,
            icon: record.icon || ''
        });
        setIsModalVisible(true);
    };

    const handleDelete = async (id: number) => {
        if (!window.confirm('确定要删除这个分类吗？')) return;
        try {
            const res = await axios.delete(`/api/categories/${id}`);
            if (res.data.code === 200) {
                fetchCategories();
            } else {
                alert(res.data.message || '删除失败');
            }
        } catch (error: any) {
            alert(error.response?.data?.message || '删除失败，可能存在关联数据');
        }
    };

    const handleSave = async () => {
        if (!formData.name) {
            alert('请输入分类名称');
            return;
        }

        try {
            if (editingCategory) {
                const res = await axios.put(`/api/categories/${editingCategory.id}`, formData);
                if (res.data.code === 200) {
                    setIsModalVisible(false);
                    fetchCategories();
                }
            } else {
                const res = await axios.post('/api/categories', formData);
                if (res.data.code === 200) {
                    setIsModalVisible(false);
                    fetchCategories();
                }
            }
        } catch (error: any) {
            alert(error.response?.data?.message || '操作失败');
        }
    };

    const toggleExpand = (id: number) => {
        const newExpanded = new Set(expandedRowKeys);
        if (newExpanded.has(id)) {
            newExpanded.delete(id);
        } else {
            newExpanded.add(id);
        }
        setExpandedRowKeys(newExpanded);
    };

    const flattenCategories = (cats: Category[], result: Category[] = []) => {
        cats.forEach(c => {
            if (c.level === 0) {
                result.push(c);
            }
        });
        return result;
    };

    const handleBindRules = async (item: Category) => {
        try {
            const res = await axios.get('/api/finance/matchers');
            if (res.data.code === 200) {
                const matchers = res.data.data || [];
                const bound = matchers.filter((m: any) => m.targetType === 'CATEGORY' && m.targetId === item.id).map((m: any) => m.id);
                setBoundRuleIds(bound);
                setBindingItem(item);
                setShowRuleSelector(true);
            }
        } catch (e) {
            console.error(e);
            alert('获取规则列表失败');
        }
    };

    const handleSaveBindings = async (ruleIds: number[]) => {
        if (!bindingItem) return;
        try {
            const res = await axios.get('/api/finance/matchers');
            if (res.data.code === 200) {
                const allMatchers = res.data.data || [];

                const toUnbind = allMatchers.filter((m: any) => m.targetType === 'CATEGORY' && m.targetId === bindingItem.id && !ruleIds.includes(m.id));
                const toBind = allMatchers.filter((m: any) => ruleIds.includes(m.id) && !(m.targetType === 'CATEGORY' && m.targetId === bindingItem.id));

                const promises = [];
                for (const m of toUnbind) {
                    promises.push(axios.put(`/api/finance/matchers/${m.id}`, {...m, targetType: null, targetId: null}));
                }
                for (const m of toBind) {
                    promises.push(axios.put(`/api/finance/matchers/${m.id}`, {
                        ...m,
                        targetType: 'CATEGORY',
                        targetId: bindingItem.id
                    }));
                }

                await Promise.all(promises);
                setShowRuleSelector(false);
                alert('绑定成功');
            }
        } catch (e) {
            console.error(e);
            alert('绑定失败');
        }
    };

    const renderTypeTag = (type: string) => {
        switch (type) {
            case 'EXPENSE':
                return <span className="px-2 py-1 bg-red-100 text-red-700 text-xs rounded-full">支出</span>;
            case 'INCOME':
                return <span className="px-2 py-1 bg-green-100 text-green-700 text-xs rounded-full">收入</span>;
            case 'TRANSFER':
                return <span className="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded-full">转账</span>;
            default:
                return null;
        }
    };

    const renderTreeNodes = (nodes: Category[]) => {
        return nodes.map(node => (
            <div key={node.id} className="border-b border-gray-100 last:border-0">
                <div
                    className={`flex items-center justify-between p-3 hover:bg-gray-50 ${node.level > 0 ? 'pl-10 bg-gray-50/50' : ''}`}>
                    <div className="flex items-center space-x-3">
                        {node.children && node.children.length > 0 ? (
                            <button onClick={() => toggleExpand(node.id)}
                                    className="text-gray-500 hover:text-gray-700 focus:outline-none">
                                {expandedRowKeys.has(node.id) ? <ChevronDown size={18}/> : <ChevronRight size={18}/>}
                            </button>
                        ) : (
                            <span className="w-[18px]"></span>
                        )}
                        <span className="font-medium text-gray-800">{node.name}</span>
                        {renderTypeTag(node.type)}
                    </div>
                    <div className="flex space-x-2">
                        <button onClick={() => setShowRulePanelFor(node)}
                                className="p-1 text-teal-600 hover:bg-teal-50 rounded" title="规则管理">
                            <Settings size={16}/>
                        </button>
                        <button onClick={() => handleBindRules(node)}
                                className="p-1 text-purple-600 hover:bg-purple-50 rounded" title="绑定规则">
                            <Link size={16}/>
                        </button>
                        {node.level === 0 && (
                            <button onClick={() => handleAdd(node.id)}
                                    className="p-1 text-blue-600 hover:bg-blue-50 rounded" title="添加子类">
                                <Plus size={16}/>
                            </button>
                        )}
                        <button onClick={() => handleEdit(node)} className="p-1 text-gray-600 hover:bg-gray-200 rounded"
                                title="编辑">
                            <Edit2 size={16}/>
                        </button>
                        <button onClick={() => handleDelete(node.id)}
                                className="p-1 text-red-600 hover:bg-red-50 rounded" title="删除">
                            <Trash2 size={16}/>
                        </button>
                    </div>
                </div>
                {expandedRowKeys.has(node.id) && node.children && node.children.length > 0 && (
                    <div className="bg-gray-50 border-t border-gray-100">
                        {renderTreeNodes(node.children)}
                    </div>
                )}
            </div>
        ));
    };

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">分类管理</h1>
                <button onClick={() => handleAdd()}
                        className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
                    <Plus size={16}/>
                    新增主分类
                </button>
            </div>

            <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
                <div
                    className="flex items-center justify-between p-4 bg-gray-50 border-b border-gray-200 text-sm font-medium text-gray-600">
                    <div>分类名称</div>
                    <div>操作</div>
                </div>
                {loading ? (
                    <div className="p-8 text-center text-gray-500">加载中...</div>
                ) : categories.length === 0 ? (
                    <div className="p-8 text-center text-gray-500">暂无分类数据</div>
                ) : (
                    <div className="divide-y divide-gray-200">
                        {renderTreeNodes(categories)}
                    </div>
                )}
            </div>

            {isModalVisible && (
                <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h3 className="text-lg font-medium mb-4">{editingCategory ? '编辑分类' : '新增分类'}</h3>
                        <div className="space-y-4">
                            {!editingCategory && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">父级分类
                                        (可选)</label>
                                    <select
                                        value={formData.parentId || ''}
                                        onChange={e => setFormData({
                                            ...formData,
                                            parentId: e.target.value ? Number(e.target.value) : undefined
                                        })}
                                        className="w-full border-gray-300 rounded-md shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500"
                                    >
                                        <option value="">作为主分类</option>
                                        {flattenCategories(categories).map(c => (
                                            <option key={c.id} value={c.id}>{c.name}</option>
                                        ))}
                                    </select>
                                </div>
                            )}

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">分类名称 <span
                                    className="text-red-500">*</span></label>
                                <input
                                    type="text"
                                    value={formData.name}
                                    onChange={e => setFormData({...formData, name: e.target.value})}
                                    placeholder="如：餐饮、交通"
                                    className="w-full border-gray-300 rounded-md shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500"
                                />
                            </div>

                            {!editingCategory && !formData.parentId && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">收支类型</label>
                                    <select
                                        value={formData.type}
                                        onChange={e => setFormData({...formData, type: e.target.value})}
                                        className="w-full border-gray-300 rounded-md shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500"
                                    >
                                        <option value="EXPENSE">支出</option>
                                        <option value="INCOME">收入</option>
                                        <option value="TRANSFER">转账</option>
                                    </select>
                                </div>
                            )}

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">图标 (可选)</label>
                                <input
                                    type="text"
                                    value={formData.icon}
                                    onChange={e => setFormData({...formData, icon: e.target.value})}
                                    placeholder="如：icon-food"
                                    className="w-full border-gray-300 rounded-md shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500"
                                />
                            </div>
                        </div>

                        <div className="mt-6 flex justify-end space-x-3">
                            <button
                                onClick={() => setIsModalVisible(false)}
                                className="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50"
                            >
                                取消
                            </button>
                            <button
                                onClick={handleSave}
                                className="px-4 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700"
                            >
                                保存
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {bindingItem && (
                <RuleSelector
                    isOpen={showRuleSelector}
                    onClose={() => setShowRuleSelector(false)}
                    onSelect={handleSaveBindings}
                    initialSelectedIds={boundRuleIds}
                    targetType="CATEGORY"
                />
            )}
            {showRulePanelFor && (
                <div className="fixed inset-0 z-40 flex justify-end bg-black bg-opacity-30">
                    <div className="w-[800px] h-full bg-white shadow-2xl p-6 overflow-y-auto animate-fade-in-right">
                        <div className="flex justify-between items-center mb-6 border-b pb-4">
                            <h2 className="text-xl font-bold text-gray-800">规则管理 - {showRulePanelFor.name}</h2>
                            <button onClick={() => setShowRulePanelFor(null)} className="text-gray-500 hover:text-gray-800 font-medium text-lg">
                                ✕
                            </button>
                        </div>
                        <div className="h-[calc(100vh-120px)]">
                            <RuleManagementPanel
                                targetType="CATEGORY"
                                targetId={showRulePanelFor.id}
                                targetName={showRulePanelFor.name}
                                onClose={() => setShowRulePanelFor(null)}
                            />
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
