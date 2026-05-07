# 用户指令记忆

本文件记录了用户的指令、偏好和教导，用于在未来的交互中提供参考。

## 格式

### 用户指令条目
用户指令条目应遵循以下格式：

[用户指令摘要]
- Date: [YYYY-MM-DD]
- Context: [提及的场景或时间]
- Instructions:
  - [用户教导或指示的内容，逐行描述]

### 项目知识条目
Agent 在任务执行过程中发现的条目应遵循以下格式：

[项目知识摘要]
- Date: [YYYY-MM-DD]
- Context: Agent 在执行 [具体任务描述] 时发现
- Category: [代码结构|代码模式|代码生成|构建方法|测试方法|依赖关系|环境配置]
- Instructions:
  - [具体的知识点，逐行描述]

## 去重策略
- 添加新条目前，检查是否存在相似或相同的指令
- 若发现重复，跳过新条目或与已有条目合并
- 合并时，更新上下文或日期信息
- 这有助于避免冗余条目，保持记忆文件整洁

## 条目

[项目为 Maven 聚合的前后端分仓结构]
- Date: 2026-05-07
- Context: Agent 在执行项目深入分析时发现
- Category: 代码结构
- Instructions:
  - 根仓库是 Maven 聚合工程，核心模块为 `qing-bom`、`qing-services`、`qing-support`、`qing-frontend`
  - `qing-frontend` 在当前工作区中已存在为目录，同时 Git 子模块配置里记录的路径为 `qing-web-anime/qing-frontend`，仓库结构与 `.gitmodules` 配置存在不一致风险
  - `qing-services` 下包含多个 Spring Boot 微服务，`qing-service-llm` 是当前最完整且业务指向最明确的财务服务

[项目构建与前端开发约定]
- Date: 2026-05-07
- Context: Agent 在执行项目深入分析时发现
- Category: 构建方法
- Instructions:
  - 后端使用 Java 21 与 Maven 多模块构建，父工程为 `qing-parent`
  - 文档站使用 `docs/package.json`，通过 `vitepress` 的 `docs:dev`、`docs:build` 等脚本运行
  - `qing-frontend/finance-web` 使用 Vite + React + TypeScript，开发代理已配置为将 `/api` 转发到 `http://localhost:8087`
  - `qing-frontend/finance-web/vite.config.ts` 已配置 `server.proxy`，但尚未配置 `server.allowedHosts` 中的 `.monkeycode-ai.online`

[用户确认先建统一模型再编码]
- Date: 2026-05-07
- Context: 用户在导入流程重构讨论中明确指示
- Instructions:
  - 在继续修复导入流程前，必须先定义统一草稿模型与状态机，再进入代码实现
  - 解析器路线与 LLM 路线不做二选一，均应收敛到统一中间模型
  - 在开始新一轮分析与方案前，先切换到 `feature/llm-bill-parser` 分支并基于该分支最新变更判断

[用户要求每次改动自动提交并推送]
- Date: 2026-05-07
- Context: 用户在继续开发过程中明确指示
- Instructions:
  - 后续每次完成一批代码改动后，直接提交到当前分支 `feature/llm-bill-parser`
  - 每次提交后立即推送到远端，不再等待额外确认
