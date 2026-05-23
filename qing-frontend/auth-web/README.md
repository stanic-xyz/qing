# Qing Auth 前端管理系统

基于 React + TypeScript + Ant Design 构建的企业级用户权限管理系统前端应用。

## 📋 项目简介

Qing Auth Frontend 是配套的后台管理前端，提供完整的用户管理、角色管理、权限管理功能。

### 技术栈

- **前端框架**: React 19.2.0
- **路由**: React Router DOM 7.11.0
- **UI 组件库**: Ant Design 6.1.2
- **构建工具**: Vite (rolldown-vite 7.2.5)
- **HTTP 客户端**: Axios 1.15.0
- **样式**: Tailwind CSS 4.3.0
- **语言**: TypeScript 5.9.3

## 🚀 快速开始

### 环境要求

- Node.js >= 18.0.0
- npm >= 9.0.0
- 后端服务运行中（默认: http://localhost:8080）

### 安装依赖

```bash
cd auth-web
npm install
```

### 启动开发服务器

```bash
npm run dev
```

前端服务将运行在 http://localhost:5173（或其他可用端口）

### 构建生产版本

```bash
npm run build
```

## ⚙️ 环境配置

### 前端配置

前端通过 Vite 代理访问后端 API，无需额外配置。

代理配置（vite.config.ts）：
```typescript
server.proxy['/api'] = {
  target: 'http://localhost:8080',
  changeOrigin: true
}
```

### 后端依赖

确保以下服务正常运行：
- **后端服务**: http://localhost:8080
- **PostgreSQL**: 192.168.3.112:5432
- **Redis**: 192.168.3.112:6379

### 检查后端连接

运行诊断脚本：
```powershell
cd scripts
.\check-backend.ps1
```

## 📁 项目结构

```
auth-web/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API 请求封装
│   │   ├── auth.ts        # 认证相关 API
│   │   ├── user.ts        # 用户管理 API
│   │   ├── role.ts       # 角色管理 API
│   │   ├── permission.ts  # 权限管理 API
│   │   ├── request.ts    # Axios 封装
│   │   └── types.ts     # 类型定义
│   ├── pages/             # 页面组件
│   │   ├── admin/        # 管理后台页面
│   │   │   ├── UserManagement.tsx
│   │   │   └── RoleManagement.tsx
│   │   ├── auth/         # 认证页面
│   │   │   └── Login.tsx
│   │   └── user/         # 用户页面
│   │       └── Profile.tsx
│   ├── components/         # 公共组件
│   ├── layouts/           # 布局组件
│   ├── router/            # 路由配置
│   └── App.tsx           # 根组件
├── scripts/               # 工具脚本
│   ├── check-backend.ps1  # 检查后端状态
│   └── test-backend.ps1   # 测试后端接口
├── package.json
├── vite.config.ts
└── README.md
```

## 🎯 功能模块

### 1. 用户管理
- 用户列表查询
- 用户创建
- 用户激活/禁用
- 用户删除
- 角色分配
- 批量角色分配

### 2. 角色管理
- 角色列表查询
- 角色创建/编辑
- 角色删除
- 权限分配
- 批量权限分配

### 3. 个人中心
- 查看个人信息
- 编辑个人资料
- 修改邮箱
- 头像设置

### 4. 认证模块
- 用户登录
- 令牌刷新
- 权限验证
- 自动注销

## 📡 API 接口

### 认证接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | /api/v1/auth/login | 用户登录 | ❌ |
| POST | /api/v1/auth/register | 用户注册 | ❌ |
| POST | /api/v1/auth/logout | 用户注销 | ✅ |
| POST | /api/v1/auth/refresh | 刷新 Token | ❌ |
| GET | /api/v1/auth/me | 获取当前用户信息 | ✅ |

### 用户管理接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/v1/users | 用户列表 | ✅ |
| POST | /api/v1/users | 创建用户 | ✅ |
| GET | /api/v1/users/{id} | 获取用户详情 | ✅ |
| PUT | /api/v1/users/{id} | 更新用户 | ✅ |
| DELETE | /api/v1/users/{id} | 删除用户 | ✅ |
| PATCH | /api/v1/users/{id}/activate | 激活用户 | ✅ |
| PATCH | /api/v1/users/{id}/deActivate | 禁用用户 | ✅ |
| POST | /api/v1/users/assignRolesToUser | 批量分配角色 | ✅ |

### 角色管理接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/v1/roles | 角色列表 | ✅ |
| POST | /api/v1/roles | 创建角色 | ✅ |
| GET | /api/v1/roles/{id} | 获取角色详情 | ✅ |
| PUT | /api/v1/roles/{id} | 更新角色 | ✅ |
| DELETE | /api/v1/roles/{id} | 删除角色 | ✅ |
| GET | /api/v1/roles/{id}/permissions | 获取角色权限 | ✅ |
| POST | /api/v1/roles/{id}/permissions | 分配权限 | ✅ |

## 🔧 开发指南

### 添加新的 API 接口

1. 在 `src/api/` 目录下创建或编辑对应的 API 文件
2. 示例：

```typescript
// src/api/user.ts
import request from './request';

export const getUsers = (params: any) => {
    return request.get('/users', { params });
};
```

### 添加新的页面

1. 在 `src/pages/` 目录下创建页面组件
2. 在路由配置中添加路由
3. 配置菜单权限

### 样式规范

- 使用 Ant Design 组件库
- 使用 Tailwind CSS 进行辅助样式
- 遵循组件库的设计规范

## 🧪 测试

### 运行测试

```bash
npm run test
```

### 测试账号

- **用户名**: admin
- **密码**: admin123
- **角色**: 超级管理员

## 🐛 常见问题

### 1. 请求超时 (timeout)

**症状**: 
```
响应错误: undefined timeout of 10000ms exceeded
```

**解决方案**:
- 确保后端服务运行在 http://localhost:8080
- 检查防火墙设置
- 验证数据库和 Redis 连接

### 2. CORS 错误

**症状**:
```
Access to XMLHttpRequest at 'http://localhost:8080/api/v1/auth/login' 
from origin 'http://localhost:5173' has been blocked by CORS policy
```

**解决方案**:
- 确保后端 SecurityConfig 正确配置 CORS
- 重启后端服务

### 3. Token 过期

**症状**:
```
响应错误: 401 Unauthorized
```

**解决方案**:
- 清除浏览器 localStorage
- 重新登录

### 4. 数据不显示

**症状**: 表格显示空白

**解决方案**:
- 检查浏览器控制台是否有错误
- 确认 API 请求是否正常
- 验证后端数据库连接

## 📊 查看日志

### 前端日志
打开浏览器开发者工具 (F12) → Console 标签

### 后端日志
```bash
cd d:\workspace\repo\op\qing\qing-services\qing-service-auth
tail -f logs/application.log
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目遵循 Mulan PSL v2 开源许可证。


## 🙏 致谢

- [React](https://react.dev/)
- [Ant Design](https://ant.design/)
- [Vite](https://vitejs.dev/)
- [Tailwind CSS](https://tailwindcss.com/)
- [TypeScript](https://www.typescriptlang.org/)
