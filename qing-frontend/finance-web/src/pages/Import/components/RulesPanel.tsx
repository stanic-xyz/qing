import { FileText, X } from 'lucide-react';
import type { ActiveRule } from '../types';

interface RulesPanelProps {
  activeRules: ActiveRule[];
  selectedRuleIds: Set<number>;
  onToggleRule: (id: number) => void;
  onClose: () => void;
}

export default function RulesPanel({ activeRules, selectedRuleIds, onToggleRule, onClose }: RulesPanelProps) {
  return (
    <div className="w-1/3 bg-blue-50/50 rounded-xl border border-blue-100 p-4 shadow-sm h-[calc(100vh-200px)] overflow-y-auto">
      <div className="flex justify-between items-center mb-4 border-b border-blue-200 pb-2">
        <h3 className="font-medium text-blue-900 flex items-center">
          <FileText size={18} className="mr-2"/> 当前生效规则 ({activeRules.length})
        </h3>
        <button onClick={onClose} className="text-blue-400 hover:text-blue-600"><X size={18}/></button>
      </div>
      <div className="mb-3 text-xs text-blue-700 bg-blue-100/50 p-2 rounded">
        提示：勾选下方规则进行单条测试。若全不选，则默认测试所有启用的规则。
      </div>
      <div className="space-y-3">
        {activeRules.map((rule) => {
          const isSelected = selectedRuleIds.has(rule.id);
          return (
            <div 
              key={rule.id} 
              className={`bg-white p-3 rounded border shadow-sm cursor-pointer transition-colors ${isSelected ? 'border-blue-500 ring-1 ring-blue-500' : 'border-blue-100 hover:border-blue-300'}`}
              onClick={() => onToggleRule(rule.id)}
            >
              <div className="flex justify-between items-start">
                <div className="flex items-center space-x-2">
                  <input 
                    type="checkbox" 
                    checked={isSelected} 
                    onChange={() => {}} // Handle change via onClick on the parent div
                    className="text-blue-600 rounded border-gray-300 focus:ring-blue-500 cursor-pointer"
                  />
                  <span className="font-medium text-sm text-gray-800">{rule.name}</span>
                </div>
                <span className="text-[10px] bg-gray-100 text-gray-500 px-1.5 py-0.5 rounded">优先级 {rule.priority}</span>
              </div>
              <div className="mt-2 text-xs text-gray-600 space-y-1 pl-6">
                <p><span className="text-gray-400">渠道:</span> {rule.sourceChannel || '不限'}</p>
                <p><span className="text-gray-400">正则:</span> <code className="bg-gray-50 px-1 rounded text-blue-600 break-all">{rule.matchRegex}</code></p>
                <div className="flex flex-wrap gap-1 mt-2">
                  {rule.setType && <span className="bg-green-50 text-green-700 px-1.5 py-0.5 rounded border border-green-100">设为 {rule.setType}</span>}
                  {rule.targetMerchant && <span className="bg-purple-50 text-purple-700 px-1.5 py-0.5 rounded border border-purple-100">商户: {rule.targetMerchant}</span>}
                  {rule.targetAccountKeyword && <span className="bg-orange-50 text-orange-700 px-1.5 py-0.5 rounded border border-orange-100">转账至: {rule.targetAccountKeyword}</span>}
                </div>
              </div>
            </div>
          );
        })}
        {activeRules.length === 0 && <div className="text-sm text-gray-500 text-center py-4">暂无启用的规则</div>}
      </div>
    </div>
  );
}
