# Requirements Document

## Introduction

本需求文档定义 `qing-service-llm` 导入流程 v1 重构目标：建立统一草稿模型与导入状态机，收敛当前“LLM 解析流程”和“旧账单导入流程”的双轨并行问题，优先保证数据准确性、流程可恢复性与前后端语义一致性。

## Glossary

- **导入草稿 (Import Draft)**: 指尚未正式入账的标准化交易记录。
- **统一草稿模型 (Unified Draft Model)**: 所有来源文件在进入匹配和确认前必须转换到的唯一中间格式。
- **导入批次 (Import Batch)**: 一次文件导入产生的草稿集合及其流程状态。
- **正式入账 (Final Import)**: 将确认后的草稿原子化写入正式交易表并触发余额联动的动作。
- **来源适配器 (Source Adapter)**: 将来源账单转为统一草稿模型的实现，可为解析器、脚本或 LLM。

## Requirements

### Requirement 1 - 建立统一草稿模型

**User Story:** AS 账单导入系统维护者, I want 所有来源账单先转换为统一草稿模型, so that 后续匹配和确认阶段有稳定输入。

#### Acceptance Criteria

1. WHEN 任意来源文件被接收, THE `qing-service-llm` SHALL 生成统一草稿模型记录并保存草稿批次。
2. WHEN 来源适配失败, THE `qing-service-llm` SHALL 标记草稿记录失败原因并保留可审计原始文本片段。
3. WHILE 草稿记录处于未入账状态, THE `qing-service-llm` SHALL 不对正式交易表和账户余额产生影响。
4. IF 草稿记录缺少必填字段, THE `qing-service-llm` SHALL 将草稿记录标记为 `NEED_REVIEW` 并阻止进入正式入账阶段。

### Requirement 2 - 建立导入状态机

**User Story:** AS 产品开发者, I want 导入流程具有明确状态机, so that 前后端能按同一语义协同。

#### Acceptance Criteria

1. WHEN 导入批次创建成功, THE `qing-service-llm` SHALL 将批次状态设为 `DRAFTED`。
2. WHEN 草稿预览通过且启动匹配, THE `qing-service-llm` SHALL 将批次状态推进到 `MATCHING`。
3. WHEN 自动匹配完成, THE `qing-service-llm` SHALL 将批次状态推进到 `MATCHED`。
4. WHEN 用户执行锁定与确认, THE `qing-service-llm` SHALL 将批次状态推进到 `CONFIRMING`。
5. WHEN 去重检查通过且入账完成, THE `qing-service-llm` SHALL 将批次状态推进到 `IMPORTED`。
6. IF 任一阶段发生不可恢复错误, THE `qing-service-llm` SHALL 将批次状态设为 `FAILED` 并记录错误上下文。

### Requirement 3 - 明确字段责任边界

**User Story:** AS 导入流程维护者, I want 区分原始事实字段与推断字段, so that 匹配和人工确认不会污染原始数据。

#### Acceptance Criteria

1. WHEN 草稿记录创建, THE `qing-service-llm` SHALL 持久化原始事实字段与推断字段的分层结构。
2. WHEN 自动匹配更新建议值, THE `qing-service-llm` SHALL 仅更新推断字段并保留原始事实字段不变。
3. WHEN 用户锁定某字段, THE `qing-service-llm` SHALL 记录锁定来源、锁定时间与锁定人。
4. IF 用户撤销锁定, THE `qing-service-llm` SHALL 恢复字段为最近一次系统推断值并保留审计日志。

### Requirement 4 - 去重与幂等安全

**User Story:** AS 账务系统使用者, I want 导入具备交易级去重与幂等保护, so that 重试和并发不会导致脏账。

#### Acceptance Criteria

1. WHEN 执行正式入账, THE `qing-service-llm` SHALL 基于交易级幂等键执行去重校验。
2. IF 检测到重复交易, THE `qing-service-llm` SHALL 标记重复原因并跳过该交易写入。
3. WHEN 同一批次被重复提交, THE `qing-service-llm` SHALL 返回幂等成功结果且不得重复变更账户余额。
4. IF 批次状态已为 `IMPORTED`, THE `qing-service-llm` SHALL 拒绝再次执行入账事务。

### Requirement 5 - 异步任务可恢复与可追踪

**User Story:** AS 平台运维人员, I want 异步解析与匹配任务可持久化追踪, so that 服务重启后状态不丢失。

#### Acceptance Criteria

1. WHEN 创建异步任务, THE `qing-service-llm` SHALL 持久化任务状态、进度与关联批次。
2. WHEN 服务重启后查询任务状态, THE `qing-service-llm` SHALL 返回最新持久化状态。
3. IF 任务执行失败, THE `qing-service-llm` SHALL 持久化错误码、错误摘要与失败阶段。
4. WHEN 任务完成, THE `qing-service-llm` SHALL 记录完成时间与耗时指标。

### Requirement 6 - 前后端交互契约统一

**User Story:** AS 前端开发者, I want 导入流程接口契约稳定一致, so that 页面状态与后端状态机一致。

#### Acceptance Criteria

1. WHEN 前端查询批次详情, THE `qing-service-llm` SHALL 返回批次状态、可执行动作和字段锁定信息。
2. WHEN 前端执行动作, THE `qing-service-llm` SHALL 验证动作是否符合当前状态机。
3. IF 前端提交非法状态动作, THE `qing-service-llm` SHALL 返回可诊断错误信息与当前允许动作列表。
4. WHILE 批次处于 `MATCHING`, THE `qing-service-llm` SHALL 返回可轮询进度与剩余待处理数。

### Requirement 7 - 多适配器并存策略

**User Story:** AS 系统架构师, I want 解析器、脚本、LLM 共存并可路由, so that 能按来源和成本选择最佳策略。

#### Acceptance Criteria

1. WHEN 创建导入批次, THE `qing-service-llm` SHALL 记录适配策略类型与版本。
2. WHEN 来源命中显式解析器规则, THE `qing-service-llm` SHALL 优先使用确定性适配器。
3. IF 来源无法被确定性适配器处理, THE `qing-service-llm` SHALL 回退到脚本或 LLM 适配器。
4. WHEN 适配结果置信度低于阈值, THE `qing-service-llm` SHALL 标记草稿记录为 `NEED_REVIEW`。
