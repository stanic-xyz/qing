import {Outlet, Link} from 'react-router-dom';
import {LayoutDashboard, UploadCloud, List, Wallet, History, GitCompare} from 'lucide-react';

export default function MainLayout() {
    return (
        <div className="flex h-screen bg-gray-50">
            <aside className="w-64 bg-white border-r border-gray-200">
                <div className="h-16 flex items-center px-6 border-b border-gray-200">
                    <h1 className="text-xl font-bold text-gray-800">个人财务系统</h1>
                </div>
                <nav className="p-4 space-y-1">
                    <Link to="/"
                          className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-100">
                        <LayoutDashboard size={20}/>
                        仪表盘
                    </Link>
                    <Link to="/transactions"
                          className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-100">
                        <List size={20}/>
                        交易流水
                    </Link>
                    <Link to="/transaction-compare"
                          className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-100">
                        <GitCompare size={20}/>
                        交易比对
                    </Link>
                    <Link to="/accounts"
                          className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-100">
                        <Wallet size={20}/>
                        账户管理
                    </Link>
                    <Link to="/import"
                          className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-100">
                        <UploadCloud size={20}/>
                        账单导入
                    </Link>
                    <Link to="/import-history"
                          className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-100">
                        <History size={20}/>
                        导入记录
                    </Link>
                </nav>
            </aside>
            <main className="flex-1 overflow-auto">
                <header className="h-16 bg-white border-b border-gray-200 flex items-center px-8">
                    <h2 className="text-lg font-medium text-gray-800">欢迎回来</h2>
                </header>
                <div className="p-8">
                    <Outlet/>
                </div>
            </main>
        </div>
    );
}
