# 项目分析报告：qing 财务系统

> 生成时间: 2026-04-20
> 分析师: AutoClaw (基于代码静态分析)

---

## 一、后端架构分析 (qing-service-llm)

### 1.1 技术栈

| 层次 | 技术 |
|------|------|
| 框架 | Spring Boot 3.x |
| ORM | JPA / Hibernate (JPA Repository) |
| AI | LangChain4j (@MemoryId, @UserMessage) |
| 数据库 | PostgreSQL |
| 文件解析 | Apache POI (Excel) + OpenCSV |
| 工具库 | Hutool |
| 构建 | Maven |

### 1.2 核心实体关系

```
TransactionRecord (核心)
├── Account (账户: 储蓄卡/信用卡/钱包)
├── Channel (渠道: 支付宝/微信/各银行)
├── Category (分类)
├── Counterparty (对手方)
└── RecordDetail (交易明细)

UploadBatch / UploadFileRecord (导入批次跟踪)
TransactionMatcher (规则引擎: 条件AST + 动作列表)
ParserConfig (解析器配置)
```

### 1.3 导入流程 (已实现)

```
上传文件
  → FileParser.parse()  [支付宝/微信/各银行专用解析器]
  → UploadService.parseAndPreviewBatch()
    → 分批 (200条/批) 创建 UploadBatch
    → 临时保存 TransactionRecord (isImported=false)
  → 前端预览 (分页)
  → startMatchingAsync()
    → RuleEngineService.applyRules()  [老规则引擎]
    → MatcherService.applyMatchers()  [新匹配器引擎]
      → 条件 AST 递归求值
      → 执行动作 (SET_TYPE / SET_CATEGORY / SET_COUNTERPARTY / SET_TARGET_ACCOUNT)
      → applyCrossBillMatch()  [跨账单撮合: 钱包↔银行卡]
  → importConfirmed()
    → 账户余额联动更新
    → 标记 isImported=true
    → 触发异步对账 reconciliationService
```

### 1.4 AI 层现状

**Assistant.java** (仅定义了接口):
```java
public interface Assistant {
    String chat(@MemoryId int userId, @UserMessage String message);
}
```

这是一个 LangChain4j 的 AI 服务接口，**目前只定义了接口，没有实现，也没有集成到任何业务流程中**。

### 1.5 当前痛点分析

#### 痛点1: 导入功能的"未完成"
从代码看，导入流程的核心链路是通的，但存在以下不完善之处：

- **解析器是硬编码的**: 每个银行/渠道一个 Parser 类，新增渠道需要写新类 + 改代码
- **没有 AI 增强解析**: 解析器只能处理固定格式，格式稍有变化就失效
- **规则引擎只能做确定性操作**: SET_CATEGORY、SET_TYPE 等，无法处理模糊匹配
- **跨账单撮合逻辑简单**: 只支持"24小时内金额完全相等"，漏报率较高
- **缺少对账结果反馈**: 匹配成功率没有可视化报告

#### 痛点2: 基础功能缺失
从用户描述和代码结构推断，以下功能可能未完善或缺失：

- 仪表盘统计 (Dashboard) - 后端是否有统计 API 不明确
- 分类管理 (Categories) - CRUD 不完整
- 账户余额实时同步
- 定期对账功能 (ReconciliationService 只有占位方法)
- 规则的可视化编辑
- 多账本/预算功能

---

## 二、前端架构分析 (finance-web)

### 2.1 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | React 18 + TypeScript |
| 构建 | Vite |
| 路由 | 未明确 (可能是 React Router) |
| 状态 | useState / useEffect (局部状态) |
| UI库 | Lucide React (图标) |
| HTTP | Axios |
| 样式 | Tailwind CSS (推断) |

### 2.2 页面结构

```
pages/
├── Import/           # 导入 (最复杂)
│   ├── index.tsx         (主容器)
│   ├── UploadView.tsx    (上传组件)
│   ├── ImportRecordList.tsx
│   ├── ProcessTable.tsx  (处理表格)
│   ├── RulesPanel.tsx
│   └── ParserConfigDrawer.tsx
├── Accounts.tsx         (账户管理)
├── Categories.tsx       (分类管理)
├── Dashboard.tsx        (仪表盘)
├── FundsFlowChart.tsx   (资金流向)
├── Matchers.tsx         (规则管理)
├── Transactions.tsx     (交易列表)
├── TransactionTrace.tsx (交易追溯)
├── Counterparties.tsx   (对手方管理)
└── ChannelAccountManager.tsx
```

### 2.3 前端问题分析

**"逻辑一团糟"的具体体现:**

1. **状态分散在 props 层层传递**: Import/index.tsx 中 `processPreview / isMatching / isImporting / modifiedRecords / lockedTempIds / processStep` 全堆在一个大组件里，300+ 行
2. **没有全局状态管理**: 所有数据靠 `useState + useEffect` + 重复 `axios.get()` 获取
3. **API 调用散落各处**: 没有统一的 API service 层，每个页面各自调用
4. **类型定义混乱**: `any` 使用过多 (`Set<any>`、`Record<string, any>`)
5. **没有请求缓存/防抖**: 频繁切换标签页会重复请求
6. **RulesPanel 和 Import 状态强耦合**: 分离开很困难
7. **缺少加载/错误状态处理**: 大部分 API 调用只有 `console.error`

---

## 三、LLM 能帮上忙的地方

### 3.1 LLM 适用的场景 (确定性不高的任务)

| 场景 | 为什么 LLM 适合 |
|------|----------------|
| **交易分类** | "这个商家是什么类型？" 需要语义理解 |
| **对手方消歧** | "拼多多平台商户" 实际是购物还是餐饮？ |
| **商户名归一化** | "海底捞火锅（成都店）" → "海底捞火锅" |
| **异常交易检测** | 金额/时间/频率异常需要上下文判断 |
| **规则条件生成** | "帮我建一个规则：所有外卖都归餐饮" → AST |
| **对账结果解释** | "为什么这笔被标记为可疑？" |

### 3.2 LLM 不适合的地方 (确定性强)

- 文件格式解析 (已有硬编码解析器)
- 精确金额匹配/对账
- 账户余额计算
- 数据格式校验
- 规则条件中 `GT/LT/REGEX` 等确定性判断

### 3.3 LLM 编程的核心思路

```
传统编程: if (merchant.contains("银行")) → category = "金融"
LLM编程:  send_to_llm("商家={merchant}, 金额={amount}, 
           账户类型={accountType}, 请判断这笔交易的分类...") 
           → category
```

两者的边界：**规则明确 → 传统代码；语义模糊 → LLM**

---

## 四、演进计划

### Phase 1: 基础稳固 (1-2周)

**目标**: 修复导入流程中的确定性 bug，完善基础功能

1. **统一 API 层**: 创建 `src/api/` 目录，按模块拆分 API 调用 (accounts.ts, transactions.ts, import.ts, matchers.ts)
2. **引入 React Query / TanStack Query**: 解决状态管理混乱、请求缓存、加载/错误状态问题
3. **提取共享组件**: ChannelAccountCascader、RuleBuilder、MultiSelect 抽到 `components/` 通用目录
4. **后端补全**: Dashboard 统计 API (月度收支/分类聚合/趋势数据)
5. **Parser 接口增强**: 每个 Parser 实现 `detectCharset()` 和 `validateFile()` 方法，减少解析失败率

### Phase 2: LLM 增强导入 (2-3周)

**目标**: 用 LLM 提升分类准确率，但不完全依赖 LLM

1. **实现 Assistant 接口**: 接入 DeepSeek API 或 OpenAI API
2. **LLM 分类服务**:
   - 单条分类: `LLMClassifier.classify(transaction) → Category`
   - 批量分类: 攒 50 条一起发给 LLM，降低 API 调用成本
   - 结果缓存: Redis 缓存同商家名的分类结果
3. **规则条件 LLM 辅助生成**:
   - 用户输入 "所有外卖都归餐饮" → LLM 生成 AST JSON → 存入 TransactionMatcher
   - 降低规则编写门槛
4. **对账结果 LLM 解释**:
   - 可疑交易旁边显示 "AI 判断：此交易与另一笔金额相同但时间相差3天，建议人工核实"

### Phase 3: 智能分析 (长期)

1. **月度财务报告自动生成**: LLM 读聚合数据，生成自然语言报告
2. **消费异常提醒**: 订阅式通知 ("本月餐饮支出已达预算的 90%" )
3. **多账本支持**: 不同用途的账本隔离
4. **预算管理**: 月度预算 + 警戒线 + LLM 预警

---

## 五、立即可执行的任务清单

### 可立即开始 (无需设计决策)

- [ ] **后端**: 补全 `ReconciliationService.autoReconcileForRecords()` 实际逻辑
- [ ] **后端**: 实现 `DashboardController` 统计 API (月度/年度聚合)
- [ ] **前端**: 拆分 `Import/index.tsx` 为子组件 (UploadSection / PreviewSection / RulesSection)
- [ ] **前端**: 创建 `src/api/` 统一 API 调用层
- [ ] **前端**: 引入 React Query，替换散落的 useEffect + axios
- [ ] **前端**: 统一 TypeScript 类型，消灭 `any`

### 需要设计决策后再做

- [ ] LLM Provider 选择 (DeepSeek / OpenAI / 本地模型)
- [ ] LLM 分类是实时还是异步
- [ ] Token 预算和成本控制策略
- [ ] 对账失败时的用户通知机制

---

## 六、项目可改进的代码层面

### 后端

1. **Parser 增加 `canParse()` 探测方法**: 不用改文件名后缀，根据内容特征判断文件类型
2. **引入 MapStruct**: DTO ↔ Entity 映射，减少手写 Boilerplate
3. **统一异常处理**: `@ControllerAdvice` 全局异常捕获，返回统一格式
4. **规则引擎持久化**: TransactionMatcher 的 conditionNode/actionNode 用 JSON 列存，但缺少版本管理
5. **导入幂等性**: 同一文件 hash 重复上传时给出明确提示而不是抛异常

### 前端

1. **引入 Zustand / Redux Toolkit**: 解决跨组件状态共享
2. **组件库统一**: 混入的 UI 库 (Ant Design / shadcn? / Radix?) 需要明确
3. **ErrorBoundary**: 每个页面加错误边界，防止白屏
4. **国际化**: 如果未来要支持多语言，现在就开始留接口
5. **拆分路由**: React Router v6 + 懒加载

---

*以上分析基于 qing-services/qing-service-llm 和 qing-frontend/finance-web 的代码静态分析，不包含运行时验证。部分判断基于代码结构和命名约定的推断，实际情况需以运行测试为准。*
