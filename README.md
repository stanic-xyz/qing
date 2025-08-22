# Project-青

[![License](https://img.shields.io/badge/license-MulanPSL2-blue)](http://license.coscl.org.cn/MulanPSL2)
[![Gitee Stars](https://gitee.com/stanChen/qing/badge/star.svg?theme=dark)](https://gitee.com/stanChen/qing)
[![GitHub Build](https://github.com/stanic-xyz/qing/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/stanic-xyz/qing/actions/workflows/build.yml)

> 🌟 **领域驱动的微服务实践平台** - 基于Spring Cloud Alibaba构建的现代化业务中台解决方案

## 🚀 快速开始

```bash
# 1. 克隆项目
git clone https://gitee.com/stanChen/qing.git
cd qing

# 2. 启动基础设施
docker-compose -f scripts/docker-compose-infra.yml up -d

# 3. 编译并启动
mvn clean install -DskipTests
mvn spring-boot:run -pl qing-service-cloud-gateway

# 4. 访问服务
open http://localhost:8080/doc.html
```

## 📚 文档导航

| 文档类型 | 链接 | 描述 |
|---------|------|------|
| 📖 **用户指南** | [docs/用户指南.md](docs/用户指南.md) | 详细使用说明和最佳实践 |
| ⚡ **快速开始** | [docs/快速开始.md](docs/快速开始.md) | 5分钟快速上手指南 |
| 🏗️ **架构设计** | [docs/架构设计.md](docs/架构设计.md) | 系统架构和设计理念 |
| 🚀 **部署指南** | [docs/部署指南.md](docs/部署指南.md) | 生产环境部署方案 |
| 🗺️ **发展路线** | [ROADMAP.md](ROADMAP.md) | 项目发展规划 |
| 🤝 **参与贡献** | [CONTRIBUTING.md](CONTRIBUTING.md) | 贡献指南和开发规范 |
| 🔧 **代码生成器** | [qing-codegen-plugin/readme.md](qing-codegen-plugin/readme.md) | 自动化代码生成工具 |

## ✨ 核心特性

- 🏛️ **DDD架构** - 基于领域驱动设计的六边形架构
- 🔐 **统一认证** - 多租户支持与OAuth2/JWT认证
- 📊 **可观测性** - 全链路监控、日志追踪、性能指标
- 🔄 **分布式事务** - 基于Seata的分布式事务解决方案
- 🚀 **代码生成** - 智能化代码生成器，提升开发效率
- 🐳 **容器化** - 完整的Docker和Kubernetes部署方案

## 🧩 模块架构

```
qing/
├── qing-services/          # 业务服务层
│   ├── qing-service-auth/   # 认证服务
│   └── qing-service-anime/  # 动漫管理服务
├── qing-infrastructure/     # 基础设施层
├── qing-codegen-plugin/     # 代码生成器
├── qing-starters/          # 自动配置启动器
└── scripts/                # 部署脚本
```

## 🌐 多仓库镜像

| 仓库 | Gitee | GitHub |
|------|-------|--------|
| 后端源码 | [stanChen/qing](https://gitee.com/stanChen/qing) | [stanic-xyz/qing](https://github.com/stanic-xyz/qing) |
| 前端源码 | [stanChen/qing-frontend](https://gitee.com/stanChen/qing-frontend) | [stanic-xyz/qing-frontend](https://github.com/stanic-xyz/qing-frontend) |

## 📄 许可证

本项目采用 [木兰宽松许可证第2版](LICENSE.TXT) 开源协议。
