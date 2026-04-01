import { useEffect, useState } from 'react';
import axios from 'axios';
import { useChannelStore } from '../store/useChannelStore';
import type { Channel, Account } from '../types/channel';
import { Link } from 'react-router-dom';
import { Edit2, Trash2, Power, PowerOff, Plus } from 'lucide-react';

export default function ChannelAccountManager() {
  const { fetchEffectiveAccountsByChannel, clearCache } = useChannelStore();
  const [channels, setChannels] = useState<Channel[]>([]);
  const [selectedChannel, setSelectedChannel] = useState<Channel | null>(null);
  const [boundAccounts, setBoundAccounts] = useState<Account[]>([]);
  const [allAccounts, setAllAccounts] = useState<Account[]>([]);
  
  const [accountTypeFilter, setAccountTypeFilter] = useState('');
  
  // 渠道编辑模态框状态
  const [showChannelModal, setShowChannelModal] = useState(false);
  const [editingChannel, setEditingChannel] = useState<Partial<Channel>>({ code: '', name: '', isEnabled: true });

  useEffect(() => {
    loadChannels();
    loadAllAccounts();
  }, []);

  const loadChannels = async () => {
    try {
      const res = await axios.get('/api/finance/channels');
      if (res.data.code === 200) {
        const data = res.data.data || [];
        setChannels(data);
        if (data.length > 0 && !selectedChannel) {
          setSelectedChannel(data[0]);
        } else if (selectedChannel) {
          const updatedSelected = data.find((c: Channel) => c.id === selectedChannel.id);
          setSelectedChannel(updatedSelected || null);
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  const loadAllAccounts = async () => {
    try {
      const res = await axios.get('/api/finance/accounts');
      if (res.data.code === 200) {
        setAllAccounts(res.data.data || []);
      }
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    if (selectedChannel) {
      loadBoundAccounts(selectedChannel.id);
    }
  }, [selectedChannel]);

  const loadBoundAccounts = async (channelId: number) => {
    const accounts = await fetchEffectiveAccountsByChannel(channelId);
    setBoundAccounts(accounts);
  };

  const handleBind = async (accountId: number) => {
    if (!selectedChannel) return;
    if (!window.confirm('确认将此账户绑定到该渠道吗？')) return;
    try {
      await axios.post(`/api/finance/channels/${selectedChannel.id}/accounts/${accountId}/bind`);
      clearCache();
      loadBoundAccounts(selectedChannel.id);
    } catch (e) {
      console.error(e);
      alert('绑定失败');
    }
  };

  const handleUnbind = async (accountId: number) => {
    if (!selectedChannel) return;
    if (!window.confirm('确认解除此账户与该渠道的绑定吗？')) return;
    try {
      await axios.delete(`/api/finance/channels/${selectedChannel.id}/accounts/${accountId}/unbind`);
      clearCache();
      loadBoundAccounts(selectedChannel.id);
    } catch (e) {
      alert('解绑失败，可能存在关联流水');
    }
  };

  const handleSaveChannel = async () => {
    if (!editingChannel.code || !editingChannel.name) {
      alert('请填写编码和名称');
      return;
    }
    try {
      await axios.post('/api/finance/channels', editingChannel);
      setShowChannelModal(false);
      clearCache();
      loadChannels();
    } catch (e) {
      console.error(e);
      alert('保存失败');
    }
  };

  const handleToggleChannelStatus = async (channel: Channel, e: React.MouseEvent) => {
    e.stopPropagation();
    try {
      const updatedChannel = { ...channel, isEnabled: !channel.isEnabled };
      await axios.post('/api/finance/channels', updatedChannel);
      clearCache();
      loadChannels();
    } catch (err) {
      console.error(err);
      alert('操作失败');
    }
  };

  const handleDeleteChannel = async (channelId: number, e: React.MouseEvent) => {
    e.stopPropagation();
    if (!window.confirm('确认删除该渠道吗？（此操作仅进行软删除）')) return;
    try {
      await axios.delete(`/api/finance/channels/${channelId}`);
      if (selectedChannel?.id === channelId) {
        setSelectedChannel(null);
      }
      clearCache();
      loadChannels();
    } catch (err) {
      console.error(err);
      alert('删除失败');
    }
  };

  const filteredAllAccounts = allAccounts.filter(a => 
    !boundAccounts.find(b => b.id === a.id) &&
    (accountTypeFilter ? a.accountType === accountTypeFilter : true)
  );

  return (
    <div className="flex flex-col h-full relative">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">渠道-账户关系维护</h1>
        <Link to="/funds-flow" className="px-4 py-2 bg-purple-600 text-white rounded-md text-sm hover:bg-purple-700">
          查看资金流动图
        </Link>
      </div>

      <div className="flex flex-1 overflow-hidden space-x-6">
        {/* 左侧：渠道列表 */}
        <div className="w-1/3 bg-white border border-gray-200 rounded-lg flex flex-col overflow-hidden">
          <div className="p-4 border-b border-gray-200 bg-gray-50 flex justify-between items-center">
            <h2 className="font-semibold text-gray-700">渠道列表</h2>
            <button 
              onClick={() => {
                setEditingChannel({ code: '', name: '', isEnabled: true });
                setShowChannelModal(true);
              }}
              className="text-blue-600 hover:text-blue-800 flex items-center text-sm font-medium"
            >
              <Plus className="w-4 h-4 mr-1" /> 新增渠道
            </button>
          </div>
          <div className="overflow-y-auto flex-1 p-2">
            {channels.map(c => (
              <div 
                key={c.id} 
                onClick={() => setSelectedChannel(c)}
                className={`p-3 mb-2 rounded-md cursor-pointer border relative group ${selectedChannel?.id === c.id ? 'border-blue-500 bg-blue-50' : 'border-transparent hover:bg-gray-100'} ${!c.isEnabled ? 'opacity-60' : ''}`}
              >
                <div className="flex justify-between items-start">
                  <div className="font-medium text-gray-800 flex items-center">
                    {c.name}
                    {!c.isEnabled && <span className="ml-2 px-1.5 py-0.5 bg-gray-200 text-gray-600 text-[10px] rounded">已停用</span>}
                  </div>
                  <div className="hidden group-hover:flex space-x-2">
                    <button 
                      onClick={(e) => { e.stopPropagation(); setEditingChannel(c); setShowChannelModal(true); }}
                      className="text-gray-500 hover:text-blue-600" title="编辑"
                    >
                      <Edit2 className="w-4 h-4" />
                    </button>
                    <button 
                      onClick={(e) => handleToggleChannelStatus(c, e)}
                      className={`text-gray-500 hover:text-yellow-600`} title={c.isEnabled ? "停用" : "启用"}
                    >
                      {c.isEnabled ? <PowerOff className="w-4 h-4" /> : <Power className="w-4 h-4" />}
                    </button>
                    <button 
                      onClick={(e) => handleDeleteChannel(c.id, e)}
                      className="text-gray-500 hover:text-red-600" title="删除"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
                <div className="text-xs text-gray-500 mt-1">编码: {c.code}</div>
              </div>
            ))}
          </div>
        </div>

        {/* 右侧：账户管理 */}
        <div className="flex-1 bg-white border border-gray-200 rounded-lg flex flex-col overflow-hidden">
          <div className="p-4 border-b border-gray-200 bg-gray-50 flex justify-between items-center">
            <h2 className="font-semibold text-gray-700">
              {selectedChannel ? `已绑定账户 (${selectedChannel.name})` : '请选择渠道'}
            </h2>
            <select 
              value={accountTypeFilter} 
              onChange={e => setAccountTypeFilter(e.target.value)}
              className="text-sm border-gray-300 rounded p-1"
            >
              <option value="">全部用途/类型</option>
              <option value="DEBIT">储蓄卡</option>
              <option value="CREDIT">信用卡</option>
              <option value="WALLET">电子钱包</option>
            </select>
          </div>
          
          <div className="flex-1 overflow-y-auto p-4 flex space-x-4">
            {/* 已绑定 */}
            <div className="w-1/2 border border-gray-200 rounded-md p-2 flex flex-col">
              <h3 className="text-sm font-medium text-gray-600 mb-3 text-center">已绑定账户</h3>
              <div className="overflow-y-auto flex-1">
                {boundAccounts.length === 0 && <div className="text-center text-gray-400 text-sm mt-10">暂无绑定账户</div>}
                {boundAccounts.map(a => (
                  <div key={a.id} className="p-3 border border-gray-100 shadow-sm rounded mb-2 flex justify-between items-center">
                    <div>
                      <div className="font-medium text-sm">{a.accountName}</div>
                      <div className="text-xs text-gray-500">{a.bankName} - {a.accountType}</div>
                    </div>
                    <button onClick={() => handleUnbind(a.id)} className="text-xs text-red-500 hover:text-red-700 px-2 py-1 border border-red-200 rounded">解绑</button>
                  </div>
                ))}
              </div>
            </div>

            {/* 未绑定 */}
            <div className="w-1/2 border border-gray-200 rounded-md p-2 flex flex-col">
              <h3 className="text-sm font-medium text-gray-600 mb-3 text-center">可绑定账户</h3>
              <div className="overflow-y-auto flex-1">
                {filteredAllAccounts.length === 0 && <div className="text-center text-gray-400 text-sm mt-10">暂无可绑定账户</div>}
                {filteredAllAccounts.map(a => (
                  <div key={a.id} className="p-3 border border-gray-100 shadow-sm rounded mb-2 flex justify-between items-center">
                    <div>
                      <div className="font-medium text-sm">{a.accountName}</div>
                      <div className="text-xs text-gray-500">{a.bankName} - {a.accountType}</div>
                    </div>
                    <button onClick={() => handleBind(a.id)} className="text-xs text-blue-500 hover:text-blue-700 px-2 py-1 border border-blue-200 rounded">绑定</button>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 渠道编辑 Modal */}
      {showChannelModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-96 shadow-xl">
            <h2 className="text-xl font-bold mb-4">{editingChannel.id ? '编辑渠道' : '新增渠道'}</h2>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">渠道编码</label>
                <input 
                  type="text" 
                  value={editingChannel.code || ''} 
                  onChange={e => setEditingChannel({...editingChannel, code: e.target.value.toUpperCase()})}
                  className="w-full border-gray-300 rounded-md shadow-sm p-2 border" 
                  placeholder="如 ALIPAY" 
                  disabled={!!editingChannel.id} // 编辑时不可修改编码
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">显示名称</label>
                <input 
                  type="text" 
                  value={editingChannel.name || ''} 
                  onChange={e => setEditingChannel({...editingChannel, name: e.target.value})}
                  className="w-full border-gray-300 rounded-md shadow-sm p-2 border" 
                  placeholder="如 支付宝" 
                />
              </div>
              <div className="flex items-center">
                <input 
                  type="checkbox" 
                  id="isEnabled"
                  checked={editingChannel.isEnabled} 
                  onChange={e => setEditingChannel({...editingChannel, isEnabled: e.target.checked})}
                  className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
                <label htmlFor="isEnabled" className="ml-2 block text-sm text-gray-900">启用该渠道</label>
              </div>
            </div>
            <div className="mt-6 flex justify-end space-x-3">
              <button 
                onClick={() => setShowChannelModal(false)} 
                className="px-4 py-2 border border-gray-300 rounded-md text-sm hover:bg-gray-50"
              >
                取消
              </button>
              <button 
                onClick={handleSaveChannel} 
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
