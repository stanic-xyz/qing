import { useState, useEffect } from 'react';
import axios from 'axios';
import RuleBuilder from './RuleBuilder';

export default function RuleEditorModal({ rule, lockedTarget, onClose, onSaved }: any) {
  const [editingMatcher, setEditingMatcher] = useState<any>(rule);
  const [categories, setCategories] = useState<any[]>([]);
  const [counterparties, setCounterparties] = useState<any[]>([]);

  useEffect(() => {
    axios.get('/api/categories').then(res => {
      const flatten = (arr: any[]): any[] => {
        let result: any[] = [];
        arr.forEach(c => {
          result.push(c);
          if (c.children && c.children.length > 0) result = result.concat(flatten(c.children));
        });
        return result;
      };
      setCategories(flatten(res.data.data || []));
    });
    axios.get('/api/finance/counterparties/active').then(res => {
      setCounterparties(res.data.data || []);
    });
  }, []);

  const handleSave = async () => {
    if (!editingMatcher?.name) return alert('请填写规则名称');
    try {
      if (editingMatcher.id) {
        await axios.put(`/api/finance/matchers/${editingMatcher.id}`, editingMatcher);
      } else {
        await axios.post('/api/finance/matchers', editingMatcher);
      }
      onSaved();
    } catch (e) {
      alert('保存失败');
    }
  };

  const currentCardValue = lockedTarget
    ? (lockedTarget.type === 'CATEGORY' ? 'CATEGORY' : 'COUNTERPARTY_MERCHANT')
    : (editingMatcher.targetType === 'CATEGORY' ? 'CATEGORY' : (!editingMatcher.targetType ? 'GLOBAL' : 'COUNTERPARTY_MERCHANT'));

  return (
    <div className="fixed inset-0 bg-gray-900/50 backdrop-blur-sm flex items-center justify-center z-[60]">
      <div className="bg-white rounded-lg p-6 w-full max-w-6xl max-h-[90vh] overflow-y-auto shadow-xl">
        <div className="flex justify-between items-center mb-6 border-b pb-3">
          <h3 className="text-xl font-bold">{editingMatcher.id ? '编辑积木式匹配规则' : '新增积木式匹配规则'}</h3>
        </div>
        <div className="grid grid-cols-3 gap-6">
          <div className="col-span-2 space-y-6">
            <div className="grid grid-cols-2 gap-4">
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-1">规则类型卡片区</label>
                <div className="grid grid-cols-4 gap-2">
                  {[
                    { label: '商户规则', value: 'COUNTERPARTY_MERCHANT', color: 'bg-blue-50 text-blue-700 border-blue-200' },
                    { label: '交易对手规则', value: 'COUNTERPARTY_INDIVIDUAL', color: 'bg-orange-50 text-orange-700 border-orange-200' },
                    { label: '分类规则', value: 'CATEGORY', color: 'bg-green-50 text-green-700 border-green-200' },
                    { label: '全局规则', value: 'GLOBAL', color: 'bg-gray-50 text-gray-700 border-gray-200' }
                  ].map(opt => {
                    let isSelected = currentCardValue === opt.value;
                    if (!lockedTarget && editingMatcher.targetType === 'COUNTERPARTY') {
                       const cp = counterparties.find(c => c.id === editingMatcher.targetId);
                       if (cp?.type === 'MERCHANT' && opt.value === 'COUNTERPARTY_MERCHANT') isSelected = true;
                       else if (cp?.type !== 'MERCHANT' && opt.value === 'COUNTERPARTY_INDIVIDUAL') isSelected = true;
                    }
                    const disabled = lockedTarget && opt.value !== currentCardValue;
                    return (
                      <div
                        key={opt.value}
                        onClick={() => {
                          if (disabled) return;
                          const newM = { ...editingMatcher, targetId: undefined };
                          if (opt.value === 'GLOBAL') newM.targetType = undefined;
                          else if (opt.value === 'CATEGORY') newM.targetType = 'CATEGORY';
                          else newM.targetType = 'COUNTERPARTY';
                          setEditingMatcher(newM);
                        }}
                        className={`border rounded p-2 text-center text-sm ${disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer hover:shadow-md'} transition-all ${isSelected ? opt.color + ' ring-2 ring-offset-1 ring-' + opt.color.split(' ')[1].split('-')[1] + '-400' : 'border-gray-200 bg-white text-gray-600'}`}
                      >
                        {opt.label}
                      </div>
                    );
                  })}
                </div>
              </div>
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-1">所属目标</label>
                {lockedTarget ? (
                  <div className="text-sm text-gray-500 p-2 bg-gray-100 rounded border cursor-not-allowed">
                    锁定至: {lockedTarget.name} (ID: {lockedTarget.id})
                  </div>
                ) : (!editingMatcher.targetType) ? (
                  <div className="text-sm text-gray-500 p-2 bg-gray-50 rounded border">全局通用，无需选择目标</div>
                ) : (
                  <select
                    value={editingMatcher.targetId || ''}
                    onChange={e => setEditingMatcher({ ...editingMatcher, targetId: e.target.value ? Number(e.target.value) : undefined })}
                    className="w-full border-gray-300 rounded p-2 text-sm"
                  >
                    <option value="">请选择绑定目标</option>
                    {editingMatcher.targetType === 'CATEGORY' && categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                    {editingMatcher.targetType === 'COUNTERPARTY' && counterparties.map(c => <option key={c.id} value={c.id}>{c.name} ({c.type})</option>)}
                  </select>
                )}
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">规则类型(内置引擎分类)</label>
                <select value={editingMatcher.ruleType || 'MERCHANT'} onChange={e => setEditingMatcher({...editingMatcher, ruleType: e.target.value})} className="w-full border-gray-300 rounded p-2 text-sm">
                  <option value="CHANNEL">渠道匹配</option>
                  <option value="MERCHANT">商户匹配</option>
                  <option value="COUNTERPARTY">对手账户匹配</option>
                  <option value="INTERNAL_TRANSFER">内部转账匹配</option>
                  <option value="CUSTOM">自定义通用匹配</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">规则名称*</label>
                <input type="text" value={editingMatcher.name || ''} onChange={e => setEditingMatcher({...editingMatcher, name: e.target.value})} className="w-full border-gray-300 rounded p-2 text-sm" placeholder="如：银联识别高德打车" />
              </div>
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-1">规则描述</label>
                <input type="text" value={editingMatcher.description || ''} onChange={e => setEditingMatcher({...editingMatcher, description: e.target.value})} className="w-full border-gray-300 rounded p-2 text-sm" placeholder="对该规则的简单描述" />
              </div>
            </div>
            <RuleBuilder
              conditionNode={editingMatcher.conditionNode}
              onChangeCondition={(node: any) => setEditingMatcher({ ...editingMatcher, conditionNode: node })}
              actionNode={editingMatcher.actionNode}
              onChangeAction={(actions: any[]) => setEditingMatcher({ ...editingMatcher, actionNode: actions })}
            />
          </div>
          <div className="col-span-1 space-y-6 border-l pl-6">
            <div className="space-y-4">
              <h4 className="font-semibold text-gray-700 border-b pb-2">规则属性</h4>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">优先级(越大越优先)</label>
                <input type="number" value={editingMatcher.priority || 0} onChange={e => setEditingMatcher({...editingMatcher, priority: parseInt(e.target.value)})} className="w-full border-gray-300 rounded p-2 text-sm" />
              </div>
              <div className="flex flex-col gap-2 pt-2">
                <label className="flex items-center cursor-pointer">
                  <input type="checkbox" checked={editingMatcher.stopOnMatch} onChange={e => setEditingMatcher({...editingMatcher, stopOnMatch: e.target.checked})} className="mr-2 rounded text-blue-600 focus:ring-blue-500" />
                  <span className="text-sm font-medium text-gray-700">命中后停止后续匹配</span>
                </label>
                <label className="flex items-center cursor-pointer">
                  <input type="checkbox" checked={editingMatcher.isActive} onChange={e => setEditingMatcher({...editingMatcher, isActive: e.target.checked})} className="mr-2 rounded text-blue-600 focus:ring-blue-500" />
                  <span className="text-sm font-medium text-gray-700">启用该规则</span>
                </label>
              </div>
            </div>
            <div className="mt-8 pt-4 border-t flex justify-end space-x-3">
              <button onClick={onClose} className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">取消</button>
              <button onClick={handleSave} className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">保存规则</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
