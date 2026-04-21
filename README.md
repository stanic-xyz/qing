# Qing - 个人财务管理系统

[![License](https://img.shields.io/badge/license-MulanPSL2-blue)](http://license.coscl.org.cn/MulanPSL2)
[![CI](https://github.com/stanic-xyz/qing/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/stanic-xyz/qing/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21-blue)]()

> 基于 Spring Boot + React 的个人财务管理系统，支持账单导入、智能匹配、交易追踪、数据分析。

## 🚀 快速开始

### 前置条件

- JDK 21+
- Node.js 20+
- Maven 3.9+
- pnpm 9+

### 后端

```bash
cd qing-services/qing-service-llm
mvn spring-boot:run
# API 文档：http://localhost:8080/swagger-ui.html
```

### 前端

```bash
cd qing-frontend/finance-web
npm install
npm run dev
# 访问：http://localhost:5173
```

## 🧩 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3 / Spring Data JPA |
| 前端框架 | React 18 + TypeScript + Vite |
| UI 组件 | shadcn/ui + Tailwind CSS |
| 图表 | Recharts |
| 数据库 | MySQL 8 |
| 缓存 | Redis |
| 部署 | Docker + Helm |

## 📁 项目结构

```
qing/
├── qing-services/
│   └── qing-service-llm/          # Spring Boot 后端
│       └── src/main/java/
│           └── cn/chenyunlong/qing/service/llm/
│               ├── controler/               # REST 接口
│               ├── service/                # 业务逻辑
│               ├── repository/             # 数据访问
│               ├── entity/                # JPA 实体
│               ├── enums/                 # 枚举类型
│               └── dto/                   # 数据传输对象
├── qing-frontend/
│   └── finance-web/                    # React 前端
│       └── src/
│           ├── pages/                    # 页面组件
│           ├── components/               # 公共组件
│           ├── hooks/                    # 自定义 Hooks
│           └── api/                      # 接口封装
├── docs/                                # VitePress 文档站
└── charts/                              # Helm 部署配置
```

## ⚙️ CI/CD 流水线

| Workflow | 触发条件 | 职责 |
|----------|---------|------|
| **CI** | PR / push 到 main | 编译、类型检查、单元测试 |
| **Pages** | docs/ 变更 | VitePress 文档站构建部署 |
| **Release** | charts/ 变更 / 手动 | Helm Chart 发布 / 全量构建 |

## 🤝 贡献

1. Fork 本仓库
2. 创建功能分支 `git checkout -b feat/your-feature`
3. 提交变更 `git commit -m 'feat: add something'`
4. 推送分支 `git push origin feat/your-feature`
5. 提交 Pull Request

## 📄 许可证

[木兰宽松许可证 第2版](LICENSE.TXT)
