import {useEffect, useState} from 'react';
import axios from 'axios';
import {getEnumText} from '../utils/enumMap';
import ChannelAccountCascader from '../components/ChannelAccountCascader';

export default function Transactions() {
    const [transactions, setTransactions] = useState([]);

    // 分页与排序状态
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(20);
    const [totalElements, setTotalElements] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [sortField, setSortField] = useState('transactionTime');
    const [sortDirection, setSortDirection] = useState('DESC');

    // 搜索过滤状态
    const [filters, setFilters] = useState({
        keyword: '',
        type: '',
        startDate: '',
        endDate: '',
        matchStatus: ''
    });

    const [selectedChannels, setSelectedChannels] = useState<number[]>([]);
    const [selectedAccounts, setSelectedAccounts] = useState<number[]>([]);

    // 编辑弹窗状态
    const [editingRecord, setEditingRecord] = useState<any>(null);

    useEffect(() => {
        fetchTransactions();
    }, [page, size, sortField, sortDirection]); // 分页和排序改变时自动请求

    const fetchTransactions = async () => {
        try {
            const params: Record<string, any> = {
                page,
                size,
                sortField,
                sortDirection,
                channelIds: selectedChannels.join(','),
                accountIds: selectedAccounts.join(','),
                ...filters
            };

            Object.keys(params).forEach(key => {
                if (params[key] === '' || params[key] === undefined) {
                    delete params[key];
                }
            });

            const res = await axios.get('/api/finance/transactions', {params});
            setTransactions(res.data.data.content || []);
            setTotalElements(res.data.data.page.totalElements || 0);
            setTotalPages(res.data.data.page.totalPages || 0);
        } catch (e) {
            console.error(e);
        }
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('确认删除该条记录吗？删除后将无法在常规列表中查看。')) {
            try {
                await axios.delete(`/api/finance/transactions/${id}`);
                fetchTransactions();
            } catch (e) {
                alert('删除失败');
            }
        }
    };

    const handleEditSave = async () => {
        if (!editingRecord) return;
        try {
            await axios.put(`/api/finance/transactions/${editingRecord.id}`, editingRecord);
            setEditingRecord(null);
            fetchTransactions();
        } catch (e) {
            alert('保存失败');
        }
    };

    const handleSearch = () => {
        setPage(0);
        fetchTransactions();
    };

    const handleReset = () => {
        setFilters({
            keyword: '',
            type: '',
            startDate: '',
            endDate: '',
            matchStatus: ''
        });
        setSelectedChannels([]);
        setSelectedAccounts([]);
        setPage(0);
        setSortField('transactionTime');
        setSortDirection('DESC');
        setTimeout(() => {
            fetchTransactions();
        }, 0);
    };

    const handleSort = (field: string) => {
        if (sortField === field) {
            setSortDirection(sortDirection === 'ASC' ? 'DESC' : 'ASC');
        } else {
            setSortField(field);
            setSortDirection('DESC');
        }
    };

    const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const {name, value} = e.target;
        setFilters(prev => ({...prev, [name]: value}));
    };

    const renderSortIcon = (field: string) => {
        if (sortField !== field) return <span className="text-gray-300 ml-1">↕</span>;
        return sortDirection === 'ASC' ? <span className="text-blue-600 ml-1">↑</span> :
            <span className="text-blue-600 ml-1">↓</span>;
    };

    return (
        <div className="flex flex-col h-full relative">
            <h1 className="text-2xl font-bold mb-6">交易流水</h1>

            {/* 搜索面板 - 卡片包裹 */}
            <div className="bg-white p-5 rounded-lg shadow-sm border border-gray-200 mb-6">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4 items-end">
                    <div>
                        <label className="block text-xs font-medium text-gray-700 mb-1">关键字</label>
                        <input
                            type="text"
                            name="keyword"
                            value={filters.keyword}
                            onChange={handleFilterChange}
                            placeholder="搜索对方/商品/备注"
                            className="w-full border-gray-300 rounded-md shadow-sm p-1.5 border text-sm focus:ring-blue-500 focus:border-blue-500 h-[38px]"
                        />
                    </div>

                    <div className="lg:col-span-2">
                        <label className="block text-xs font-medium text-gray-700 mb-1">渠道与账户</label>
                        <ChannelAccountCascader
                            selectedChannels={selectedChannels}
                            selectedAccounts={selectedAccounts}
                            onChange={(channels, accounts) => {
                                setSelectedChannels(channels);
                                setSelectedAccounts(accounts);
                            }}
                        />
                    </div>

                    <div>
                        <label className="block text-xs font-medium text-gray-700 mb-1">收支类型</label>
                        <select
                            name="type"
                            value={filters.type}
                            onChange={handleFilterChange}
                            className="w-full border-gray-300 rounded-md shadow-sm p-1.5 border text-sm focus:ring-blue-500 focus:border-blue-500 h-[38px]"
                        >
                            <option value="">全部类型</option>
                            <option value="EXPENSE">支出</option>
                            <option value="INCOME">收入</option>
                            <option value="TRANSFER">转账</option>
                        </select>
                    </div>
                    <div>
                        <label className="block text-xs font-medium text-gray-700 mb-1">匹配状态</label>
                        <select
                            name="matchStatus"
                            value={filters.matchStatus}
                            onChange={handleFilterChange}
                            className="w-full border-gray-300 rounded-md shadow-sm p-1.5 border text-sm focus:ring-blue-500 focus:border-blue-500 h-[38px]"
                        >
                            <option value="">全部类型</option>
                            <option value="ORIGINAL">原始数据</option>
                            <option value="AUTO_MATCHED">自动精确匹配</option>
                            <option value="INTERNAL_TRANSFER">自动识别为内部转账</option>
                            <option value="MANUAL_EDITED">手动修改过</option>
                            <option value="SUSPICIOUS">存疑</option>
                        </select>
                    </div>
                    <div className="flex space-x-2">
                        <div className="w-1/2">
                            <label className="block text-xs font-medium text-gray-700 mb-1">开始日期</label>
                            <input
                                type="date"
                                name="startDate"
                                value={filters.startDate}
                                onChange={handleFilterChange}
                                className="w-full border-gray-300 rounded-md shadow-sm p-1.5 border text-sm focus:ring-blue-500 focus:border-blue-500 h-[38px]"
                            />
                        </div>
                        <div className="w-1/2">
                            <label className="block text-xs font-medium text-gray-700 mb-1">结束日期</label>
                            <input
                                type="date"
                                name="endDate"
                                value={filters.endDate}
                                onChange={handleFilterChange}
                                className="w-full border-gray-300 rounded-md shadow-sm p-1.5 border text-sm focus:ring-blue-500 focus:border-blue-500 h-[38px]"
                            />
                        </div>
                    </div>
                </div>

                <div className="mt-5 flex justify-end space-x-3">
                    <button
                        onClick={handleReset}
                        className="px-4 py-1.5 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
                    >
                        重置
                    </button>
                    <button
                        onClick={handleSearch}
                        className="px-4 py-1.5 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
                    >
                        查询
                    </button>
                </div>
            </div>

            {/* 数据表格 */}
            <div className="bg-white rounded-lg shadow-sm border border-gray-100 flex-1 flex flex-col overflow-hidden">
                <div className="overflow-x-auto flex-1">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50 sticky top-0">
                        <tr>
                            <th
                                className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                                onClick={() => handleSort('transactionTime')}
                            >
                                时间 {renderSortIcon('transactionTime')}
                            </th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">渠道</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">账户</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">类型</th>
                            <th
                                className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                                onClick={() => handleSort('amount')}
                            >
                                金额 {renderSortIcon('amount')}
                            </th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">对方</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">商品/说明</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">备注</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">匹配状态</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                        </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                        {transactions.map((t: any) => (
                            <tr key={t.id} className="hover:bg-gray-50">
                                <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-500">{t.transactionTime}</td>
                                <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{getEnumText('channel', t.channel.name)}</td>
                                <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{t.accountName || '-'}</td>
                                <td className="px-4 py-3 whitespace-nowrap text-sm">
                    <span
                        className={t.type === 'INCOME' ? 'text-green-600 font-medium' : t.type === 'EXPENSE' ? 'text-red-600 font-medium' : 'text-gray-600'}>
                      {getEnumText('type', t.type)}
                    </span>
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-sm font-bold text-gray-900">
                                    {t.amount}
                                </td>
                                <td className="px-4 py-3 text-sm text-gray-500 max-w-37.5 truncate"
                                    title={t.counterparty?.remark}>
                                    [{getEnumText('counterParty', t.counterparty?.type)}]{t.counterparty?.name || '-'}
                                </td>
                                <td className="px-4 py-3 text-sm text-gray-500 max-w-37.5 truncate" title={t.merchant}>
                                    {t.merchant || '-'}
                                </td>
                                <td className="px-4 py-3 text-sm text-gray-500 max-w-37.5 truncate" title={t.remark}>
                                    {t.remark || '-'}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-sm">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        t.status === 'SUCCESS' ? 'bg-green-100 text-green-800' :
                            t.status === 'FAILED' ? 'bg-red-100 text-red-800' : 'bg-yellow-100 text-yellow-800'
                    }`}>
                      {getEnumText('status', t.status)}
                    </span>
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-sm">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        t.status === 'ORIGINAL' ? 'bg-green-100 text-green-800' :
                            t.status === 'AUTO_MATCHED' ? 'bg-green-500 text-red-800' : 'bg-yellow-100 text-yellow-800'
                    }`}>
                      {getEnumText('matchStatus', t.matchStatus)}
                    </span>
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-sm font-medium">
                                    <button onClick={() => setEditingRecord({...t})}
                                            className="text-blue-600 hover:text-blue-900 mr-3">编辑
                                    </button>
                                    <button onClick={() => handleDelete(t.id)}
                                            className="text-red-600 hover:text-red-900">删除
                                    </button>
                                </td>
                            </tr>
                        ))}
                        {transactions.length === 0 && (
                            <tr>
                                <td colSpan={10} className="px-4 py-8 text-center text-gray-500">
                                    暂无符合条件的交易记录
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>

                {/* 分页控制 */}
                <div
                    className="bg-gray-50 px-4 py-3 border-t border-gray-200 flex items-center justify-between sm:px-6">
                    <div className="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
                        <div>
                            <p className="text-sm text-gray-700">
                                共 <span className="font-medium">{totalElements}</span> 条记录，
                                当前第 <span className="font-medium">{page + 1}</span> / <span
                                className="font-medium">{totalPages || 1}</span> 页
                            </p>
                        </div>
                        <div>
                            <nav className="relative z-0 inline-flex rounded-md shadow-sm -space-x-px"
                                 aria-label="Pagination">
                                <button
                                    onClick={() => setPage(Math.max(0, page - 1))}
                                    disabled={page === 0}
                                    className="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    上一页
                                </button>
                                <select
                                    value={size}
                                    onChange={(e) => {
                                        setSize(Number(e.target.value));
                                        setPage(0);
                                    }}
                                    className="relative inline-flex items-center px-2 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 z-10 focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500"
                                >
                                    <option value="20">20 条/页</option>
                                    <option value="50">50 条/页</option>
                                    <option value="100">100 条/页</option>
                                    <option value="500">500 条/页</option>
                                </select>
                                <button
                                    onClick={() => setPage(Math.min(totalPages - 1, page + 1))}
                                    disabled={page >= totalPages - 1 || totalPages === 0}
                                    className="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    下一页
                                </button>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>

            {/* 编辑弹窗 */}
            {editingRecord && (
                <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h3 className="text-lg font-medium mb-4">编辑交易记录</h3>
                        <div className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">金额</label>
                                <input
                                    type="number"
                                    value={editingRecord.amount}
                                    onChange={(e) => setEditingRecord({...editingRecord, amount: e.target.value})}
                                    className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">收支类型</label>
                                <select
                                    value={editingRecord.type}
                                    onChange={(e) => setEditingRecord({...editingRecord, type: e.target.value})}
                                    className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
                                >
                                    <option value="INCOME">收入</option>
                                    <option value="EXPENSE">支出</option>
                                    <option value="OTHER">其他</option>
                                </select>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">对方</label>
                                <input
                                    type="text"
                                    value={editingRecord.counterparty.name || ''}
                                    onChange={(e) => setEditingRecord({...editingRecord, counterparty: e.target.value})}
                                    className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">商品/说明</label>
                                <input
                                    type="text"
                                    value={editingRecord.merchant || ''}
                                    onChange={(e) => setEditingRecord({...editingRecord, merchant: e.target.value})}
                                    className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">备注</label>
                                <input
                                    type="text"
                                    value={editingRecord.remark || ''}
                                    onChange={(e) => setEditingRecord({...editingRecord, remark: e.target.value})}
                                    className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
                                />
                            </div>
                        </div>
                        <div className="mt-6 flex justify-end space-x-3">
                            <button
                                onClick={() => setEditingRecord(null)}
                                className="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50"
                            >
                                取消
                            </button>
                            <button
                                onClick={handleEditSave}
                                className="px-4 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700"
                            >
                                保存
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
