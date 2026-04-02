import { Plus, Trash2 } from 'lucide-react';

interface ConditionNode {
  operator?: 'AND' | 'OR' | string;
  children?: ConditionNode[];
  field?: string;
  value?: any;
}

interface ActionNode {
  actionType: string;
  value: any;
}

interface RuleBuilderProps {
  conditionNode: ConditionNode;
  onChangeCondition: (node: ConditionNode) => void;
  actionNode: ActionNode[];
  onChangeAction: (actions: ActionNode[]) => void;
}

const FIELDS = [
  { value: 'channel', label: '渠道' },
  { value: 'type', label: '收支类型(INCOME/EXPENSE)' },
  { value: 'amount', label: '金额' },
  { value: 'merchant', label: '商户名' },
  { value: 'counterparty', label: '交易对手' },
  { value: 'remark', label: '备注' },
  { value: 'fundType', label: '资金类型(INTERNAL/EXTERNAL)' },
  { value: 'fundSource', label: '资金来源说明' },
];

const OPERATORS = [
  { value: 'EQ', label: '等于' },
  { value: 'NEQ', label: '不等于' },
  { value: 'CONTAINS', label: '包含' },
  { value: 'NOT_CONTAINS', label: '不包含' },
  { value: 'REGEX', label: '正则匹配' },
  { value: 'GT', label: '大于' },
  { value: 'LT', label: '小于' },
];

const ACTION_TYPES = [
  { value: 'SET_TYPE', label: '设置收支类型' },
  { value: 'SET_CATEGORY', label: '设置分类' },
  { value: 'SET_COUNTERPARTY', label: '设置交易对手' },
  { value: 'SET_TARGET_ACCOUNT', label: '设为内部转账(目标账户)' },
  { value: 'SET_FUND_SOURCE_ACCOUNT', label: '关联资金源账户' },
];

export default function RuleBuilder({ conditionNode, onChangeCondition, actionNode, onChangeAction }: RuleBuilderProps) {

  const renderConditionNode = (node: ConditionNode, path: number[]) => {
    if (node.operator === 'AND' || node.operator === 'OR') {
      return (
        <div key={path.join('-')} className="border border-gray-200 rounded p-3 mb-2 bg-gray-50">
          <div className="flex items-center gap-2 mb-2">
            <select
              value={node.operator}
              onChange={(e) => {
                const newNode = { ...node, operator: e.target.value as 'AND' | 'OR' };
                updateConditionNode(path, newNode);
              }}
              className="border border-gray-300 rounded px-2 py-1 text-sm bg-white font-medium text-blue-700"
            >
              <option value="AND">并且 (AND)</option>
              <option value="OR">或者 (OR)</option>
            </select>
            <button
              onClick={() => {
                const newChild: ConditionNode = { field: 'merchant', operator: 'CONTAINS', value: '' };
                const newNode = { ...node, children: [...(node.children || []), newChild] };
                updateConditionNode(path, newNode);
              }}
              className="text-xs px-2 py-1 bg-white border border-gray-300 rounded hover:bg-gray-100 flex items-center"
            >
              <Plus className="w-3 h-3 mr-1" /> 添加条件
            </button>
            <button
              onClick={() => {
                const newChild: ConditionNode = { operator: 'AND', children: [] };
                const newNode = { ...node, children: [...(node.children || []), newChild] };
                updateConditionNode(path, newNode);
              }}
              className="text-xs px-2 py-1 bg-white border border-gray-300 rounded hover:bg-gray-100 flex items-center"
            >
              <Plus className="w-3 h-3 mr-1" /> 添加规则组
            </button>
            {path.length > 0 && (
              <button onClick={() => removeConditionNode(path)} className="text-red-500 hover:text-red-700 ml-auto">
                <Trash2 className="w-4 h-4" />
              </button>
            )}
          </div>
          <div className="pl-4 border-l-2 border-blue-200 ml-2">
            {node.children?.map((child, index) => renderConditionNode(child, [...path, index]))}
            {(!node.children || node.children.length === 0) && (
              <div className="text-xs text-gray-400 italic py-1">暂无条件，将始终匹配</div>
            )}
          </div>
        </div>
      );
    }

    return (
      <div key={path.join('-')} className="flex items-center gap-2 mb-2 bg-white p-2 rounded border border-gray-200">
        <select
          value={node.field || 'merchant'}
          onChange={(e) => updateConditionNode(path, { ...node, field: e.target.value })}
          className="border border-gray-300 rounded px-2 py-1 text-sm flex-1 max-w-[150px]"
        >
          {FIELDS.map(f => <option key={f.value} value={f.value}>{f.label}</option>)}
        </select>
        <select
          value={node.operator || 'CONTAINS'}
          onChange={(e) => updateConditionNode(path, { ...node, operator: e.target.value })}
          className="border border-gray-300 rounded px-2 py-1 text-sm w-[100px]"
        >
          {OPERATORS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
        </select>
        <input
          type="text"
          value={node.value || ''}
          onChange={(e) => updateConditionNode(path, { ...node, value: e.target.value })}
          placeholder="值..."
          className="border border-gray-300 rounded px-2 py-1 text-sm flex-1"
        />
        <button onClick={() => removeConditionNode(path)} className="text-red-500 hover:text-red-700">
          <Trash2 className="w-4 h-4" />
        </button>
      </div>
    );
  };

  const updateConditionNode = (path: number[], newNode: ConditionNode) => {
    if (path.length === 0) {
      onChangeCondition(newNode);
      return;
    }
    const root = JSON.parse(JSON.stringify(conditionNode));
    let curr: any = root;
    for (let i = 0; i < path.length - 1; i++) {
      curr = curr.children[path[i]];
    }
    curr.children[path[path.length - 1]] = newNode;
    onChangeCondition(root);
  };

  const removeConditionNode = (path: number[]) => {
    if (path.length === 0) return; // 根节点不可删
    const root = JSON.parse(JSON.stringify(conditionNode));
    let curr: any = root;
    for (let i = 0; i < path.length - 1; i++) {
      curr = curr.children[path[i]];
    }
    curr.children.splice(path[path.length - 1], 1);
    onChangeCondition(root);
  };

  const handleAddAction = () => {
    onChangeAction([...actionNode, { actionType: 'SET_CATEGORY', value: '' }]);
  };

  const handleUpdateAction = (index: number, action: ActionNode) => {
    const newActions = [...actionNode];
    newActions[index] = action;
    onChangeAction(newActions);
  };

  const handleRemoveAction = (index: number) => {
    const newActions = [...actionNode];
    newActions.splice(index, 1);
    onChangeAction(newActions);
  };

  return (
    <div className="space-y-6">
      <div>
        <h4 className="text-sm font-semibold text-gray-700 mb-2 border-b pb-1">触发条件 (当满足以下条件时)</h4>
        {renderConditionNode(conditionNode, [])}
      </div>

      <div>
        <h4 className="text-sm font-semibold text-gray-700 mb-2 border-b pb-1">执行动作 (执行以下操作)</h4>
        {actionNode.map((action, index) => (
          <div key={index} className="flex items-center gap-2 mb-2 bg-white p-2 rounded border border-gray-200">
            <select
              value={action.actionType}
              onChange={(e) => handleUpdateAction(index, { ...action, actionType: e.target.value })}
              className="border border-gray-300 rounded px-2 py-1 text-sm flex-1 max-w-[200px]"
            >
              {ACTION_TYPES.map(a => <option key={a.value} value={a.value}>{a.label}</option>)}
            </select>
            <input
              type="text"
              value={action.value || ''}
              onChange={(e) => handleUpdateAction(index, { ...action, value: e.target.value })}
              placeholder="动作值 (如：餐饮美食 / TRANSFER / 1)"
              className="border border-gray-300 rounded px-2 py-1 text-sm flex-1"
            />
            <button onClick={() => handleRemoveAction(index)} className="text-red-500 hover:text-red-700">
              <Trash2 className="w-4 h-4" />
            </button>
          </div>
        ))}
        <button
          onClick={handleAddAction}
          className="text-xs px-3 py-1.5 bg-blue-50 text-blue-600 border border-blue-200 rounded hover:bg-blue-100 flex items-center mt-2"
        >
          <Plus className="w-3 h-3 mr-1" /> 添加动作
        </button>
      </div>
    </div>
  );
}