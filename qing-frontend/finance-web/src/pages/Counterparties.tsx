import { useEffect, useState } from 'react';
import axios from 'axios';
import { Plus, Edit2, Trash2, Power, PowerOff } from 'lucide-react';

interface Counterparty {
  id: number;
  name: string;
  type: string;
  defaultCategory: string;
  remark: string;
  isActive: boolean;
}

export default function Counterparties() {
  const [counterparties, setCounterparties] = useState<Counterparty[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [editingItem, setEditingItem] = useState<Partial<Counterparty> | null>(null);

  useEffect(() => {
    fetchCounterparties();
  }, []);

  const fetchCounterparties = async () => {
    try {
      const res = await axios.get('/api/finance/counterparties');
      if (res.data.code === 200) {
        setCounterparties(res.data.data || []);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleSave = async () => {
    if (!editingItem?.name) {
      alert('请填写交易对手名称');
      return;
    }
    try {
      if (editingItem.id) {
        await axios.put(`/api/finance/counterparties/${editingItem.id}`, editingItem);
      } else {
        await axios.post('/api/finance/counterparties', {
          ...editingItem,
          isActive: true
        });
      }
      setShowModal(false);
      fetchCounterparties();
    } catch (e) {
      console.error(e);
      alert('保存失败');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('确定要删除吗？')) {
      try {
        await axios.delete(`/api/finance/counterparties/${id}`);
        fetchCounterparties();
      } catch (e) {
        console.error(e);
        alert('删除失败');
      }
    }
  };

  const handleToggleStatus = async (item: Counterparty) => {
    try {
      await axios.put(`/api/finance/counterparties/${item.id}`, {
        ...item,
        isActive: !item.isActive
      });
      fetchCounterparties();
    } catch (e) {
      console.error(e);
      alert('状态切换失败');
    }
  };

  const openModal = (item?: Counterparty) => {
    if (item) {
      setEditingItem(item);
    } else {
      setEditingItem({ type: 'MERCHANT' });
    }
    setShowModal(true);
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">交易对手管理</h1>
          <p className="mt-1 text-sm text-gray-500">管理常用的付款人、收款人和商户，并设置默认分类。</p>
        </div>
        <button
          onClick={() => openModal()}
          className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors shadow-sm"
        >
          <Plus size={18} className="mr-2" />
          新增交易对手
        </button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">名称</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">类型</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">默认分类</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">备注</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200 text-sm">
            {counterparties.map((m) => (
              <tr key={m.id} className={!m.isActive ? 'bg-gray-50 opacity-60' : 'hover:bg-gray-50'}>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${m.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                    {m.isActive ? '已启用' : '已停用'}
                  </span>
                </td>
                <td className="px-6 py-4 font-medium text-gray-900">{m.name}</td>
                <td className="px-6 py-4 whitespace-nowrap">{m.type === 'MERCHANT' ? '商户' : m.type === 'INDIVIDUAL' ? '个人' : m.type === 'CORPORATE' ? '企业' : m.type}</td>
                <td className="px-6 py-4 whitespace-nowrap">{m.defaultCategory || '-'}</td>
                <td className="px-6 py-4 text-gray-500">{m.remark || '-'}</td>
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
            ))}
            {counterparties.length === 0 && (
              <tr>
                <td colSpan={6} className="px-6 py-10 text-center text-gray-500">暂无交易对手</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
            <h3 className="text-lg font-medium text-gray-900 mb-4">{editingItem?.id ? '编辑交易对手' : '新增交易对手'}</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">名称</label>
                <input
                  type="text"
                  value={editingItem?.name || ''}
                  onChange={(e) => setEditingItem({ ...editingItem, name: e.target.value })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                  placeholder="例如: 鲜农果蔬"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">类型</label>
                <select
                  value={editingItem?.type || 'MERCHANT'}
                  onChange={(e) => setEditingItem({ ...editingItem, type: e.target.value })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                >
                  <option value="MERCHANT">商户</option>
                  <option value="INDIVIDUAL">个人</option>
                  <option value="CORPORATE">企业</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">默认分类</label>
                <input
                  type="text"
                  value={editingItem?.defaultCategory || ''}
                  onChange={(e) => setEditingItem({ ...editingItem, defaultCategory: e.target.value })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                  placeholder="例如: 餐饮美食"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">备注</label>
                <input
                  type="text"
                  value={editingItem?.remark || ''}
                  onChange={(e) => setEditingItem({ ...editingItem, remark: e.target.value })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                />
              </div>
            </div>
            <div className="mt-6 flex justify-end space-x-3">
              <button
                onClick={() => setShowModal(false)}
                className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                onClick={handleSave}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
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