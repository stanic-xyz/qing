# Qing Service LLM (财务智能后端服务)

这是一个基于 Spring Boot 构建的个人财务管理核心后端服务。它的核心愿景是：**让每一笔资金流向都清清楚楚，让您随时掌握自己的财务全貌**。

无论是日常的碎片化消费、不同银行卡之间的内部转账，还是各个支付渠道的零散账单，本系统都能帮您将它们统一归集、自动对账并智能分类。告别糊涂账，真正实现“资产变动有迹可循，历史财富一目了然”。

## ✨ 核心功能 (Features)

- **账单与流水管理**：支持支付宝、微信、银行卡等多种渠道账单的上传、解析与对账。
- **智能匹配引擎**：自定义正则表达式匹配规则，自动识别交易对手（商户/个人）、内部转账并自动分类。
- **渠道与账户管理**：管理不同的支付渠道及其绑定的具体资金账户。
- **交易对手库**：类似“通讯录”的常用收付款人与商户管理，支持绑定默认消费分类。
- **大模型智能问答**：集成了 LangChain4j，提供与财务数据结合的 AI 智能对话与数据分析能力。
- **审批工作流**：集成了状态机/Flowable，支持资金操作或特定规则的审批流转。

## 🛠️ 技术栈 (Tech Stack)

- **开发语言**：Java 21
- **核心框架**：Spring Boot 3.x
- **数据持久化**：Spring Data JPA
- **AI 框架**：LangChain4j
- **其他组件**：Caffeine Cache, Spring Statemachine / Flowable

## 🚀 快速启动 (Getting Started)

### 环境要求

- JDK 21 或以上
- Maven 3.6+
- 关系型数据库 (如 PostgreSQL)

### 运行步骤

1. 修改 `src/main/resources/application.yaml` 中的数据库连接配置及 LLM 的 API Key。
2. 运行数据库初始化脚本（如 `init_default_matchers.sql`）以生成默认的匹配规则和交易对手。
3. 编译并启动服务：
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## 📂 模块结构简介

- `controller/`：提供各业务模块的 RESTful API 接口。
- `service/`：核心业务逻辑（如 `UploadService` 负责账单解析与导入，`MatcherService` 负责交易智能分类）。
- `entity/`：数据库映射实体类（Account, Category, Counterparty, TransactionRecord 等）。
- `repository/`：数据访问层接口。
- `config/`：配置类（状态机配置、LLM 配置等）。

