import { useEffect, useState } from 'react';
import axios from 'axios';
import { CHANNELS, CHANNEL_LIST } from '../config/channels';
import { Banknote, UploadCloud, Scale, Link } from 'lucide-react';
import AccountImportModal from '../components/AccountImportModal';
import RuleSelector from '../components/RuleSelector';

export default function Accounts() {
  const [accounts, setAccounts] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showImportModal, setShowImportModal] = useState(false);
  const [showCalibrateModal, setShowCalibrateModal] = useState(false);
  const [editingAccount, setEditingAccount] = useState<any>(null);
  const [calibratingAccount, setCalibratingAccount] = useState<any>(null);
  const [calibrateAmount, setCalibrateAmount] = useState<string>('');

  // 规则绑定相关状态
  const [showRuleSelector, setShowRuleSelector] = useState(false);
  const [bindingItem, setBindingItem] = useState<any>(null);
  const [boundRuleIds, setBoundRuleIds] = useState<number[]>([]);

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

  const handleCalibrate = async () => {
    if (!calibrateAmount || isNaN(Number(calibrateAmount))) {
      alert('请输入有效的金额');
      return;
    }
    try {
      await axios.post(`/api/finance/accounts/${calibratingAccount.id}/calibrate`, {
        newBalance: Number(calibrateAmount)
      });
      setShowCalibrateModal(false);
      setCalibrateAmount('');
      setCalibratingAccount(null);
      fetchAccounts();
    } catch (e: any) {
      alert('平账失败: ' + (e.response?.data?.message || e.message));
    }
  };

  const openCalibrateModal = (account: any) => {
    setCalibratingAccount(account);
    setCalibrateAmount(account.currentBalance != null ? String(account.currentBalance) : '0');
    setShowCalibrateModal(true);
  };

  const openModal = (account: any = { accountName: '', accountType: 'DEBIT', bankName: '', channel: '', remark: '', cardNumber: '', initialBalance: 0, status: 'ACTIVE' }) => {
    setEditingAccount({ ...account });
    setShowModal(true);
  };

  const handleChannelChange = (channelCode: string) => {
    const selectedChannel = CHANNELS[channelCode];
    if (selectedChannel) {
      setEditingAccount({
        ...editingAccount,
        channel: channelCode,
        bankName: selectedChannel.name
      });
    } else {
      setEditingAccount({
        ...editingAccount,
        channel: channelCode
      });
    }
  };

  const handleBindRules = async (item: any) => {
    try {
      const res = await axios.get('/api/finance/matchers');
      if (res.data.code === 200) {
        const matchers = res.data.data || [];
        const bound = matchers.filter((m: any) => m.targetType === 'ACCOUNT' && m.targetId === item.id).map((m: any) => m.id);
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
        
        const toUnbind = allMatchers.filter((m: any) => m.targetType === 'ACCOUNT' && m.targetId === bindingItem.id && !ruleIds.includes(m.id));
        const toBind = allMatchers.filter((m: any) => ruleIds.includes(m.id) && !(m.targetType === 'ACCOUNT' && m.targetId === bindingItem.id));

        const promises = [];
        for (const m of toUnbind) {
          promises.push(axios.put(`/api/finance/matchers/${m.id}`, { ...m, targetType: null, targetId: null }));
        }
        for (const m of toBind) {
          promises.push(axios.put(`/api/finance/matchers/${m.id}`, { ...m, targetType: 'ACCOUNT', targetId: bindingItem.id }));
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

  const renderIcon = (channelCode: string) => {
    const config = CHANNELS[channelCode];
    if (config && config.icon) {
      const IconComp = config.icon;
      return <IconComp className={`w-8 h-8 ${config.colorClass} mb-2`} />;
    }
    return <Banknote className="w-8 h-8 text-gray-400 mb-2" />;
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">账户管理</h1>
        <div className="flex space-x-3">
          <button
            onClick={() => setShowImportModal(true)}
            className="flex items-center px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors"
          >
            <UploadCloud className="w-4 h-4 mr-2" /> 批量导入
          </button>
          <button onClick={() => openModal()} className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">添加账户</button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {accounts.map((acc: any) => (
          <div key={acc.id} className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 relative group flex flex-col">
            <div className="absolute top-4 right-4 hidden group-hover:flex space-x-2">
              <button onClick={() => handleBindRules(acc)} className="text-purple-500 hover:text-purple-700 text-sm flex items-center" title="绑定规则"><Link className="w-3 h-3 mr-1"/>绑定规则</button>
              <button onClick={() => openCalibrateModal(acc)} className="text-orange-500 hover:text-orange-700 text-sm flex items-center"><Scale className="w-3 h-3 mr-1"/>平账</button>
              <button onClick={() => openModal(acc)} className="text-blue-500 hover:text-blue-700 text-sm">编辑</button>
              <button onClick={() => handleDelete(acc.id)} className="text-red-500 hover:text-red-700 text-sm">删除</button>
            </div>
            <div className="flex items-start justify-between">
              <div>
                {renderIcon(acc.channel)}
                <h3 className="text-lg font-bold text-gray-800">{acc.accountName} <span className="text-xs font-normal text-gray-500 bg-gray-100 px-2 py-1 rounded ml-2">{acc.accountType}</span></h3>
              </div>
            </div>
            <p className="text-gray-500 text-sm mt-1">{acc.bankName || '无机构'} {acc.cardNumber ? `- ${acc.cardNumber}` : ''}</p>
            {acc.remark && <p className="text-gray-400 text-xs mt-1">备注: {acc.remark}</p>}
            <div className="mt-auto pt-4 border-t border-gray-100">
              <p className="text-sm text-gray-500">当前可用余额</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">¥ {acc.currentBalance != null ? Number(acc.currentBalance).toFixed(2) : '0.00'}</p>
              <p className="text-xs text-gray-400 mt-1">期初余额: ¥ {acc.initialBalance != null ? Number(acc.initialBalance).toFixed(2) : '0.00'}</p>
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
                <input type="text" value={editingAccount.accountName} onChange={e => setEditingAccount({...editingAccount, accountName: e.target.value})} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" placeholder="如：日常支付宝" />
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
                <label className="block text-sm font-medium text-gray-700 mb-1">所属渠道/平台</label>
                <select value={editingAccount.channel || ''} onChange={e => handleChannelChange(e.target.value)} className="w-full border-gray-300 rounded-md shadow-sm p-2 border">
                  <option value="">未指定 / 手动输入</option>
                  {CHANNEL_LIST.map(c => (
                    <option key={c.code} value={c.code}>{c.name}</option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">发卡机构名称</label>
                <input type="text" value={editingAccount.bankName || ''} onChange={e => setEditingAccount({...editingAccount, bankName: e.target.value})} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" placeholder="如：建设银行" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">卡号/账号(后四位)</label>
                <input type="text" value={editingAccount.cardNumber || ''} onChange={e => setEditingAccount({...editingAccount, cardNumber: e.target.value})} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" placeholder="如：8888" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">备注名</label>
                <input type="text" value={editingAccount.remark || ''} onChange={e => setEditingAccount({...editingAccount, remark: e.target.value})} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" placeholder="如：用于还房贷" />
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

      {showImportModal && (
        <AccountImportModal
          onClose={() => setShowImportModal(false)}
          onSuccess={() => {
            setShowImportModal(false);
            fetchAccounts();
          }}
        />
      )}

      {showCalibrateModal && calibratingAccount && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h3 className="text-lg font-medium mb-4 flex items-center"><Scale className="w-5 h-5 mr-2 text-orange-500"/> 余额校准 (平账)</h3>
            <p className="text-sm text-gray-500 mb-4">
              为 <strong>{calibratingAccount.accountName}</strong> 进行余额校准。如果系统记录的余额与您实际的银行余额不符，请在此输入最新实际余额，系统将自动生成一笔“平账流水”来抹平差额。
            </p>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">系统当前余额</label>
                <input type="text" disabled value={calibratingAccount.currentBalance != null ? calibratingAccount.currentBalance : '0'} className="w-full bg-gray-100 border-gray-300 rounded-md shadow-sm p-2 border text-gray-500" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">实际最新余额</label>
                <input type="number" step="0.01" value={calibrateAmount} onChange={e => setCalibrateAmount(e.target.value)} className="w-full border-gray-300 rounded-md shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500" placeholder="请输入实际余额" autoFocus />
              </div>
            </div>
            <div className="mt-6 flex justify-end space-x-3">
              <button onClick={() => setShowCalibrateModal(false)} className="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50">取消</button>
              <button onClick={handleCalibrate} className="px-4 py-2 bg-orange-500 text-white rounded-md text-sm hover:bg-orange-600">确认校准</button>
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
          targetType="ACCOUNT"
        />
      )}
    </div>
  );
}
