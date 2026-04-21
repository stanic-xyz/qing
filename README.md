# Qing

[![License](https://img.shields.io/badge/license-MulanPSL2-blue)](http://license.coscl.org.cn/MulanPSL2)
[![CI](https://github.com/stanic-xyz/qing/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/stanic-xyz/qing/actions/workflows/ci.yml)

> 领域驱动的微服务实践平台

## 🧩 模块结构

```
qing/
├── qing-services/
│   └── qing-service-llm/        # 财务智能后端服务（Spring Boot 3 + JPA）
├── qing-support/               # 公共基础库
├── qing-bom/                   # Maven BOM
├── qing-frontend/              # 前端（另仓）
└── charts/                    # Helm 部署配置
```

## ⚙️ CI/CD 流水线

| Workflow | 触发条件 | 职责 |
|----------|---------|------|
| **CI** | PR / push 到 main | 编译、类型检查、单元测试 |
| **Pages** | docs/ 变更 | VitePress 文档站构建部署 |
| **Release** | charts/ 变更 / 手动 | Helm Chart 发布 / 全量构建 |

## 📂 服务说明

### qing-service-llm

财务智能后端服务，支持账单导入、智能匹配、交易追踪、数据分析。

详细文档：[qing-services/qing-service-llm/README.md](qing-services/qing-service-llm/README.md)

## 🤝 贡献

1. Fork 本仓库
2. 创建功能分支 `git checkout -b feat/your-feature`
3. 提交变更 `git commit -m 'feat: add something'`
4. 推送分支 `git push origin feat/your-feature`
5. 提交 Pull Request

## 📄 许可证

[木兰宽松许可证 第2版](LICENSE.TXT)
