import { useEffect, useState } from 'react';
import axios from 'axios';
import { Plus, Edit2, Trash2, Power, PowerOff } from 'lucide-react';
import RuleEditorModal from './RuleEditorModal';

interface RuleManagementPanelProps {
  targetType: string;
  targetId: number;
  targetName: string;
  onClose?: () => void;
}

export default function RuleManagementPanel({ targetType, targetId, targetName }: RuleManagementPanelProps) {
  const [rules, setRules] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [showEditor, setShowEditor] = useState(false);
  const [editingRule, setEditingRule] = useState<any>(null);

  const fetchRules = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/finance/matchers');
      const allRules = res.data.data || [];
      const boundRules = allRules.filter((r: any) => r.targetType === targetType && r.targetId === targetId);
      setRules(boundRules.sort((a: any, b: any) => b.priority - a.priority));
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRules();
  }, [targetType, targetId]);

  const handleToggleStatus = async (rule: any) => {
    try {
      await axios.post(`/api/finance/matchers`, { ...rule, isActive: !rule.isActive });
      fetchRules();
    } catch (e) {
      alert('状态切换失败');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('确定删除此规则吗？')) {
      try {
        await axios.delete(`/api/finance/matchers/${id}`);
        fetchRules();
      } catch (e) {
        alert('删除失败');
      }
    }
  };

  const handleAdd = () => {
    let ruleType = 'GLOBAL';
    if (targetType === 'CATEGORY') ruleType = 'CATEGORY';
    else if (targetType === 'COUNTERPARTY') {
        // Need to know if merchant or not, we can default to MERCHANT and let backend or UI handle
        ruleType = 'COUNTERPARTY_MERCHANT'; 
    }
    
    setEditingRule({
      name: '',
      description: '',
      ruleType: 'MERCHANT', // Backend enum
      targetType: targetType,
      targetId: targetId,
      conditionNode: { operator: 'AND', children: [] },
      actionNode: [],
      priority: 100,
      stopOnMatch: true,
      isActive: true,
      _uiRuleType: ruleType
    });
    setShowEditor(true);
  };

  return (
    <div className="bg-white flex flex-col h-full">
      <div className="flex justify-between items-center mb-4">
        <h3 className="text-lg font-bold text-gray-800">
          匹配规则管理 <span className="text-sm font-normal text-gray-500">({targetName})</span>
        </h3>
        <button
          onClick={handleAdd}
          className="px-3 py-1.5 bg-blue-600 text-white text-sm rounded hover:bg-blue-700 flex items-center"
        >
          <Plus size={14} className="mr-1" /> 添加规则
        </button>
      </div>

      {loading ? (
        <div className="py-8 text-center text-gray-500">加载中...</div>
      ) : rules.length === 0 ? (
        <div className="py-8 text-center text-gray-500 bg-gray-50 rounded border border-gray-200">
          当前实体暂未绑定任何匹配规则
        </div>
      ) : (
        <div className="overflow-auto border border-gray-200 rounded">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">状态</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">优先级</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">名称</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500">操作</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200 text-sm">
              {rules.map(r => (
                <tr key={r.id} className={!r.isActive ? 'bg-gray-50 opacity-60' : 'hover:bg-gray-50'}>
                  <td className="px-4 py-2">
                    <span className={`px-2 py-1 text-xs rounded-full ${r.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                      {r.isActive ? '已启用' : '已停用'}
                    </span>
                  </td>
                  <td className="px-4 py-2">{r.priority}</td>
                  <td className="px-4 py-2 font-medium">{r.name}</td>
                  <td className="px-4 py-2 flex space-x-2">
                    <button onClick={() => { setEditingRule(r); setShowEditor(true); }} className="text-blue-600 hover:text-blue-900">
                      <Edit2 size={14} />
                    </button>
                    <button onClick={() => handleToggleStatus(r)} className="text-yellow-600 hover:text-yellow-900">
                      {r.isActive ? <PowerOff size={14} /> : <Power size={14} />}
                    </button>
                    <button onClick={() => handleDelete(r.id)} className="text-red-600 hover:text-red-900">
                      <Trash2 size={14} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showEditor && editingRule && (
        <RuleEditorModal
          rule={editingRule}
          lockedTarget={{ type: targetType, id: targetId, name: targetName }}
          onClose={() => setShowEditor(false)}
          onSaved={() => {
            setShowEditor(false);
            fetchRules();
          }}
        />
      )}
    </div>
  );
}