// 通用错误类型 - 扩展 Error 接口
export interface ApiError extends Error {
  businessCode?: number;
  response?: unknown;
}

// 通用修改记录接口
export interface RecordModification {
  tempId: string;
  type?: string;
  merchant?: string;
  targetAccountId?: number;
  category?: string;
  counterparty?: string;
  [key: string]: unknown; // 保留扩展性
}

// 测试结果接口
export interface TestResult {
  success: boolean;
  records?: unknown[];
  error?: string;
}

// API 响应结构
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

// Matcher 规则接口
export interface MatcherRule {
  id: number;
  name: string;
  priority: number;
  targetType: 'ACCOUNT' | 'CHANNEL';
  targetId: number;
  matchRegex?: string;
  ruleType?: string;
  counterpartyId?: number;
}

// 规则节点接口
export interface RuleNode {
  type: string;
  field?: string;
  operator?: string;
  value?: unknown;
  children?: RuleNode[];
}

// 动作节点接口
export interface ActionNode {
  type: string;
  params?: Record<string, unknown>;
}

// 交易追溯记录接口
export interface TraceRecord {
  id: string;
  amount: number;
  transactionTime: string;
  directionType?: string;
  channel?: string;
  type?: string;
  account?: unknown;
  counterparty?: string;
  merchant?: string;
  status?: string;
  matchStatus?: string;
  matchRuleName?: string;
  targetAccountId?: number;
  fundType?: string;
  fundSource?: string;
  fundSourceAccountId?: number;
}
