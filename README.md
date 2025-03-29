# Project-青

[![License](https://img.shields.io/badge/license-MulanPSL2-blue)](http://license.coscl.org.cn/MulanPSL2)
[![Gitee Stars](https://gitee.com/stanChen/qing/badge/star.svg?theme=dark)](https://gitee.com/stanChen/qing)
[![GitHub Build](https://github.com/stanic-xyz/qing/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/stanic-xyz/qing/actions/workflows/build.yml)

### 🌐 多仓库镜像

| 后端源码                                                                                     | 前端源码                                                                                                       |
|------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| [Gitee](https://gitee.com/stanChen/qing) \| [GitHub](https://github.com/stanic-xyz/qing) | [Gitee](https://gitee.com/stanChen/qing-frontend) \| [GitHub](https://github.com/stanic-xyz/qing-frontend) |

---

## 🚀 项目概览

**领域驱动的微服务实践平台**，基于Spring Cloud Alibaba构建，提供模块化业务中台解决方案。

**核心特性**：

- ✅ 基于DDD的六边形架构
- ✅ 多租户支持与统一身份认证
- ✅ 分布式事务解决方案
- ✅ 全链路监控与日志追踪
- 🚧 规则引擎集成（开发中）

**[📘 完整文档](doc/README.md)** | **[🗺️ 架构设计](doc/ARCHITECTURE.md)** | **[📆 开发路线](docs/Roadmap.md)**

---

### 本地启动

```bash
# 1. 启动基础设施
docker-compose -f docker-compose-infra.yml up

# 2. 编译项目
mvn clean install -DskipTests

# 3. 启动网关服务
java -jar qing-bootstrap/gateway/target/*.jar

# 访问 Swagger 文档
open http://localhost:8888/swagger-ui.html
```

🔧 详细部署指南 | 🧪 API测试用例

🧩 核心模块
模块 功能说明
qing-auth 统一认证中心（OAuth2/JWT）
qing-user 用户管理体系（RBAC权限模型）
qing-gateway 动态路由与流量管控
qing-file 分布式文件存储服务
qing-monitor 系统健康监控中心
