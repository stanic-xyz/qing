import {useEffect, useState} from 'react';
import axios from 'axios';
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
    ResponsiveContainer,
    PieChart,
    Pie,
    Cell
} from 'recharts';
import {Wallet, TrendingUp, TrendingDown, Landmark} from 'lucide-react';

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899', '#14b8a6', '#6366f1'];

export default function Dashboard() {
    const [stats, setStats] = useState<any>({
        totalAssets: 0,
        monthlyIncome: 0,
        monthlyExpense: 0,
        monthlyBalance: 0,
        trends: [],
        categoryStructure: [],
        accounts: []
    });

    useEffect(() => {
        fetchStats();
    }, []);

    const fetchStats = async () => {
        try {
            const res = await axios.get('/api/finance/dashboard/stats');
            setStats(res.data.data || {});
        } catch (e) {
            console.error(e);
        }
    };

    const renderIcon = (channelCode: string) => {
        const config = CHANNELS[channelCode];
        if (config && config.icon) {
            const IconComp = config.icon;
            return <IconComp className={`w-6 h-6 ${config.colorClass}`}/>;
        }
        return <Landmark className="w-6 h-6 text-gray-400"/>;
    };

    return (
        <div className="flex flex-col h-full space-y-6">
            <h1 className="text-2xl font-bold">全局资产看板</h1>

            {/* 顶部指标卡片 */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 flex items-center">
                    <div className="p-4 bg-blue-50 rounded-full mr-4">
                        <Wallet className="w-8 h-8 text-blue-500"/>
                    </div>
                    <div>
                        <h3 className="text-gray-500 text-sm font-medium">系统总资产</h3>
                        <p className="text-2xl font-bold text-gray-900 mt-1">¥ {stats.totalAssets != null ? Number(stats.totalAssets).toFixed(2) : '0.00'}</p>
                    </div>
                </div>
                <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 flex items-center">
                    <div className="p-4 bg-green-50 rounded-full mr-4">
                        <TrendingUp className="w-8 h-8 text-green-500"/>
                    </div>
                    <div>
                        <h3 className="text-gray-500 text-sm font-medium">本月总收入</h3>
                        <p className="text-2xl font-bold text-green-600 mt-1">¥ {stats.monthlyIncome != null ? Number(stats.monthlyIncome).toFixed(2) : '0.00'}</p>
                    </div>
                </div>
                <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 flex items-center">
                    <div className="p-4 bg-red-50 rounded-full mr-4">
                        <TrendingDown className="w-8 h-8 text-red-500"/>
                    </div>
                    <div>
                        <h3 className="text-gray-500 text-sm font-medium">本月总支出</h3>
                        <p className="text-2xl font-bold text-red-600 mt-1">¥ {stats.monthlyExpense != null ? Number(stats.monthlyExpense).toFixed(2) : '0.00'}</p>
                    </div>
                </div>
                <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 flex items-center">
                    <div className="p-4 bg-purple-50 rounded-full mr-4">
                        <Landmark className="w-8 h-8 text-purple-500"/>
                    </div>
                    <div>
                        <h3 className="text-gray-500 text-sm font-medium">本月结余</h3>
                        <p className={`text-2xl font-bold mt-1 ${stats.monthlyBalance >= 0 ? 'text-purple-600' : 'text-red-600'}`}>¥ {stats.monthlyBalance != null ? Number(stats.monthlyBalance).toFixed(2) : '0.00'}</p>
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* 趋势图 */}
                <div
                    className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 lg:col-span-2 flex flex-col min-h-[400px]">
                    <h3 className="text-lg font-bold text-gray-800 mb-4">近30天收支趋势</h3>
                    <div className="flex-1">
                        {stats.trends && stats.trends.length > 0 ? (
                            <ResponsiveContainer width="100%" height="100%">
                                <LineChart data={stats.trends}>
                                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f0f0f0"/>
                                    <XAxis dataKey="date" tick={{fontSize: 12, fill: '#666'}} tickMargin={10}
                                           minTickGap={30}/>
                                    <YAxis tick={{fontSize: 12, fill: '#666'}}/>
                                    <Tooltip contentStyle={{
                                        borderRadius: '8px',
                                        border: 'none',
                                        boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
                                    }}/>
                                    <Legend iconType="circle"/>
                                    <Line type="monotone" name="收入" dataKey="income" stroke="#16a34a" strokeWidth={3}
                                          dot={false} activeDot={{r: 6}}/>
                                    <Line type="monotone" name="支出" dataKey="expense" stroke="#dc2626" strokeWidth={3}
                                          dot={false} activeDot={{r: 6}}/>
                                </LineChart>
                            </ResponsiveContainer>
                        ) : (
                            <div className="h-full flex items-center justify-center text-gray-400">暂无趋势数据</div>
                        )}
                    </div>
                </div>

                {/* 结构饼图 */}
                <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 flex flex-col min-h-[400px]">
                    <h3 className="text-lg font-bold text-gray-800 mb-4">本月支出结构</h3>
                    <div className="flex-1">
                        {stats.categoryStructure && stats.categoryStructure.length > 0 ? (
                            <ResponsiveContainer width="100%" height="100%">
                                <PieChart>
                                    <Pie
                                        data={stats.categoryStructure}
                                        cx="50%"
                                        cy="50%"
                                        innerRadius={60}
                                        outerRadius={100}
                                        paddingAngle={5}
                                        dataKey="value"
                                    >
                                        {stats.categoryStructure.map((_entry: any, index: number) => (
                                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]}/>
                                        ))}
                                    </Pie>
                                    <Tooltip formatter={(value: any) => `¥ ${Number(value).toFixed(2)}`} contentStyle={{
                                        borderRadius: '8px',
                                        border: 'none',
                                        boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
                                    }}/>
                                    <Legend layout="horizontal" verticalAlign="bottom" align="center"
                                            wrapperStyle={{fontSize: '12px'}}/>
                                </PieChart>
                            </ResponsiveContainer>
                        ) : (
                            <div className="h-full flex items-center justify-center text-gray-400">暂无支出数据</div>
                        )}
                    </div>
                </div>
            </div>

            {/* 账户资金池 */}
            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
                <h3 className="text-lg font-bold text-gray-800 mb-4">账户资金池</h3>
                {stats.accounts && stats.accounts.length > 0 ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                        {stats.accounts.map((acc: any) => (
                            <div key={acc.id}
                                 className="p-4 border border-gray-100 rounded-lg hover:shadow-md transition-shadow bg-gray-50 flex items-center justify-between">
                                <div className="flex items-center space-x-3">
                                    {renderIcon(acc.channel?.icon)}
                                    <div>
                                        <h4 className="font-medium text-gray-800 text-sm">{acc.accountName}</h4>
                                        <p className="text-xs text-gray-500 mt-0.5">{acc.bankName || '未设置机构'} {acc.cardNumber ? `(${acc.cardNumber})` : ''}</p>
                                    </div>
                                </div>
                                <div className="text-right">
                                    <p className="font-bold text-gray-900">¥{acc.currentBalance != null ? Number(acc.currentBalance).toFixed(2) : '0.00'}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="text-center text-gray-500 py-8">暂无绑定的正常账户，请前往“账户管理”添加</div>
                )}
            </div>
        </div>
    );
}
