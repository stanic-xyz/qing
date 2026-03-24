import { useEffect, useState } from 'react';
import axios from 'axios';

export default function Accounts() {
  const [accounts, setAccounts] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingAccount, setEditingAccount] = useState<any>(null);

  useEffect(() => {
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

  const handleSave = async () => {
    try {
      if (editingAccount.id) {
        await axios.put(`/api/finance/accounts/${editingAccount.id}`, editingAccount);
      } else {
        await axios.post('/api/finance/accounts', editingAccount);
      }
      setShowModal(false);
      fetchAccounts();
    } catch (e) {
      alert('保存失败');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('确认删除该账户吗？')) {
      try {
        await axios.delete(`/api/finance/accounts/${id}`);
        fetchAccounts();
      } catch (e) {
        alert('删除失败');
      }
    }
  };

  const openModal = (account: any = { accountName: '', accountType: 'DEBIT', bankName: '', cardNumber: '', initialBalance: 0, status: 'ACTIVE' }) => {
    setEditingAccount({ ...account });
    setShowModal(true);
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">账户管理</h1>
        <button onClick={() => openModal()} className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">添加账户</button>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {accounts.map((acc: any) => (
          <div key={acc.id} className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 relative group">
            <div className="absolute top-4 right-4 hidden group-hover:flex space-x-2">
              <button onClick={() => openModal(acc)} className="text-blue-500 hover:text-blue-700 text-sm">编辑</button>
              <button onClick={() => handleDelete(acc.id)} className="text-red-500 hover:text-red-700 text-sm">删除</button>
            </div>
            <h3 className="text-lg font-bold text-gray-800">{acc.accountName} <span className="text-xs font-normal text-gray-500 bg-gray-100 px-2 py-1 rounded ml-2">{acc.accountType}</span></h3>
            <p className="text-gray-500 text-sm mt-1">{acc.bankName || '无机构'} {acc.cardNumber ? `- ${acc.cardNumber}` : ''}</p>
            <div className="mt-4 pt-4 border-t border-gray-100">
              <p className="text-sm text-gray-500">期初余额 / 当前余额</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">¥ {acc.initialBalance || '0.00'}</p>
            </div>
          </div>
        ))}
        {accounts.length === 0 && (
          <div className="col-span-3 bg-white p-6 rounded-lg shadow-sm border border-gray-100 text-center text-gray-500">
            暂无账户数据，请导入账单或手动添加
          </div>
        )}
      </div>

      {showModal && editingAccount && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h3 className="text-lg font-medium mb-4">{editingAccount.id ? '编辑账户' : '添加账户'}</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">账户名称</label>
                <input type="text" value={editingAccount.accountName} onChange={e => setEditingAccount({...editingAccount, accountName: e.target.value})} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">账户类型</label>
                <select value={editingAccount.accountType} onChange={e => setEditingAccount({...editingAccount, accountType: e.target.value})} className="w-full border-gray-300 rounded-md shadow-sm p-2 border">
                  <option value="DEBIT">借记卡/储蓄卡</option>
                  <option value="CREDIT">信用卡</option>
                  <option value="WALLET">数字钱包(支付宝/微信)</option>
                  <option value="CASH">现金</option>
                  <option value="DEBT">债务(亲友)</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">发卡机构/平台</label>
                <input type="text" value={editingAccount.bankName || ''} onChange={e => setEditingAccount({...editingAccount, bankName: e.target.value})} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">卡号/账号(后四位)</label>
                <input type="text" value={editingAccount.cardNumber || ''} onChange={e => setEditingAccount({...editingAccount, cardNumber: e.target.value})} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">期初余额</label>
                <input type="number" value={editingAccount.initialBalance} onChange={e => setEditingAccount({...editingAccount, initialBalance: e.target.value})} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" />
              </div>
            </div>
            <div className="mt-6 flex justify-end space-x-3">
              <button onClick={() => setShowModal(false)} className="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50">取消</button>
              <button onClick={handleSave} className="px-4 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700">保存</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
