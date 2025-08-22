---
sidebar_position: 2
---

# API 文档

本文档详细介绍青（Qing）项目的 API 接口规范、使用方法和示例。

## 🎯 API 概述

### 基础信息

- **API 版本**: v1
- **基础 URL**: `http://localhost:8080/api/v1`
- **数据格式**: JSON
- **字符编码**: UTF-8
- **认证方式**: JWT Token

### 通用响应格式

所有 API 响应都遵循统一的格式：

```json
{
  "code": 200,
  "message": "Success",
  "data": {},
  "timestamp": "2024-01-01T12:00:00"
}
```

### HTTP 状态码

| 状态码 | 说明                    | 描述      |
|-----|-----------------------|---------|
| 200 | OK                    | 请求成功    |
| 201 | Created               | 资源创建成功  |
| 400 | Bad Request           | 请求参数错误  |
| 401 | Unauthorized          | 未授权访问   |
| 403 | Forbidden             | 禁止访问    |
| 404 | Not Found             | 资源不存在   |
| 500 | Internal Server Error | 服务器内部错误 |

## 🔐 认证授权

### JWT Token 认证

#### 获取 Token

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

**响应示例**：

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "username": "user@example.com",
      "roles": ["USER"]
    }
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

#### 使用 Token

在请求头中添加 Authorization 字段：

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 刷新 Token

```http
POST /api/v1/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## 📺 动漫服务 API

### 动漫管理

#### 获取动漫列表

```http
GET /api/v1/animes
```

**查询参数**：

| 参数       | 类型     | 必填 | 默认值             | 说明       |
|----------|--------|----|-----------------|----------|
| page     | int    | 否  | 0               | 页码（从0开始） |
| size     | int    | 否  | 20              | 每页大小     |
| sort     | string | 否  | createTime,desc | 排序字段和方向  |
| category | string | 否  | -               | 分类筛选     |
| status   | string | 否  | -               | 状态筛选     |
| keyword  | string | 否  | -               | 关键词搜索    |

**请求示例**：

```http
GET /api/v1/animes?page=0&size=10&category=动作&keyword=进击
Authorization: Bearer your-token
```

**响应示例**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "进击的巨人",
        "description": "人类与巨人的战斗故事",
        "category": {
          "id": 1,
          "name": "动作"
        },
        "status": "COMPLETED",
        "coverImage": "https://example.com/cover1.jpg",
        "rating": 9.5,
        "episodeCount": 87,
        "releaseDate": "2013-04-07",
        "createTime": "2024-01-01T12:00:00",
        "updateTime": "2024-01-01T12:00:00"
      }
    ],
    "pageable": {
      "sort": {
        "sorted": true,
        "unsorted": false
      },
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 100,
    "totalPages": 10,
    "first": true,
    "last": false,
    "numberOfElements": 10
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

#### 获取动漫详情

```http
GET /api/v1/animes/{id}
```

**路径参数**：

| 参数 | 类型   | 必填 | 说明   |
|----|------|----|------|
| id | long | 是  | 动漫ID |

**请求示例**：

```http
GET /api/v1/animes/1
Authorization: Bearer your-token
```

**响应示例**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "name": "进击的巨人",
    "description": "在这个世界里，人类居住在由三重巨大的城墙所围成的都市里...",
    "category": {
      "id": 1,
      "name": "动作",
      "description": "动作类动漫"
    },
    "status": "COMPLETED",
    "coverImage": "https://example.com/cover1.jpg",
    "bannerImage": "https://example.com/banner1.jpg",
    "rating": 9.5,
    "episodeCount": 87,
    "duration": 24,
    "releaseDate": "2013-04-07",
    "endDate": "2023-11-04",
    "studio": "WIT Studio / MAPPA",
    "director": "荒木哲郎",
    "tags": ["战斗", "悬疑", "黑暗"],
    "episodes": [
      {
        "id": 1,
        "number": 1,
        "title": "致两千年后的你",
        "duration": 24,
        "airDate": "2013-04-07"
      }
    ],
    "createTime": "2024-01-01T12:00:00",
    "updateTime": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

#### 创建动漫

```http
POST /api/v1/animes
Content-Type: application/json
Authorization: Bearer your-token
```

**请求体**：

```json
{
  "name": "新动漫名称",
  "description": "动漫描述",
  "categoryId": 1,
  "status": "ONGOING",
  "coverImage": "https://example.com/cover.jpg",
  "episodeCount": 12,
  "duration": 24,
  "releaseDate": "2024-01-01",
  "studio": "制作公司",
  "director": "导演名称",
  "tags": ["标签1", "标签2"]
}
```

**字段说明**：

| 字段           | 类型     | 必填 | 说明                             |
|--------------|--------|----|--------------------------------|
| name         | string | 是  | 动漫名称（最大255字符）                  |
| description  | string | 否  | 动漫描述（最大5000字符）                 |
| categoryId   | long   | 是  | 分类ID                           |
| status       | string | 是  | 状态：ONGOING/COMPLETED/SUSPENDED |
| coverImage   | string | 否  | 封面图片URL                        |
| episodeCount | int    | 否  | 集数                             |
| duration     | int    | 否  | 单集时长（分钟）                       |
| releaseDate  | string | 否  | 发布日期（YYYY-MM-DD）               |
| studio       | string | 否  | 制作公司                           |
| director     | string | 否  | 导演                             |
| tags         | array  | 否  | 标签列表                           |

**响应示例**：

```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "id": 101,
    "name": "新动漫名称",
    "description": "动漫描述",
    "category": {
      "id": 1,
      "name": "动作"
    },
    "status": "ONGOING",
    "createTime": "2024-01-01T12:00:00",
    "updateTime": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

#### 更新动漫

```http
PUT /api/v1/animes/{id}
Content-Type: application/json
Authorization: Bearer your-token
```

**请求体**：与创建动漫相同，但所有字段都是可选的。

#### 删除动漫

```http
DELETE /api/v1/animes/{id}
Authorization: Bearer your-token
```

**响应示例**：

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

### 动漫分类管理

#### 获取分类列表

```http
GET /api/v1/animes/categories
```

**响应示例**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "name": "动作",
      "description": "动作类动漫",
      "animeCount": 150,
      "createTime": "2024-01-01T12:00:00"
    },
    {
      "id": 2,
      "name": "冒险",
      "description": "冒险类动漫",
      "animeCount": 120,
      "createTime": "2024-01-01T12:00:00"
    }
  ],
  "timestamp": "2024-01-01T12:00:00"
}
```

#### 创建分类

```http
POST /api/v1/animes/categories
Content-Type: application/json
Authorization: Bearer your-token
```

**请求体**：

```json
{
  "name": "科幻",
  "description": "科幻类动漫"
}
```

### 动漫搜索

#### 高级搜索

```http
POST /api/v1/animes/search
Content-Type: application/json
```

**请求体**：

```json
{
  "keyword": "进击",
  "categories": [1, 2],
  "status": ["ONGOING", "COMPLETED"],
  "ratingRange": {
    "min": 8.0,
    "max": 10.0
  },
  "releaseDateRange": {
    "start": "2020-01-01",
    "end": "2024-12-31"
  },
  "tags": ["战斗", "悬疑"],
  "page": 0,
  "size": 20,
  "sort": "rating,desc"
}
```

## 👤 用户认证服务 API

### 用户管理

#### 用户注册

```http
POST /api/v1/auth/register
Content-Type: application/json
```

**请求体**：

```json
{
  "username": "newuser@example.com",
  "password": "password123",
  "confirmPassword": "password123",
  "nickname": "新用户",
  "email": "newuser@example.com",
  "phone": "13800138000"
}
```

**字段验证规则**：

| 字段       | 规则            |
|----------|---------------|
| username | 邮箱格式，唯一       |
| password | 8-20位，包含字母和数字 |
| nickname | 2-20位字符       |
| email    | 邮箱格式，唯一       |
| phone    | 11位手机号        |

**响应示例**：

```json
{
  "code": 201,
  "message": "注册成功",
  "data": {
    "id": 101,
    "username": "newuser@example.com",
    "nickname": "新用户",
    "email": "newuser@example.com",
    "status": "ACTIVE",
    "createTime": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

#### 获取用户信息

```http
GET /api/v1/auth/profile
Authorization: Bearer your-token
```

**响应示例**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "username": "user@example.com",
    "nickname": "用户昵称",
    "email": "user@example.com",
    "phone": "13800138000",
    "avatar": "https://example.com/avatar.jpg",
    "status": "ACTIVE",
    "roles": ["USER"],
    "lastLoginTime": "2024-01-01T11:00:00",
    "createTime": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

#### 更新用户信息

```http
PUT /api/v1/auth/profile
Content-Type: application/json
Authorization: Bearer your-token
```

**请求体**：

```json
{
  "nickname": "新昵称",
  "email": "newemail@example.com",
  "phone": "13900139000",
  "avatar": "https://example.com/new-avatar.jpg"
}
```

#### 修改密码

```http
PUT /api/v1/auth/password
Content-Type: application/json
Authorization: Bearer your-token
```

**请求体**：

```json
{
  "oldPassword": "oldpassword123",
  "newPassword": "newpassword123",
  "confirmPassword": "newpassword123"
}
```

### 权限管理

#### 获取用户权限

```http
GET /api/v1/auth/permissions
Authorization: Bearer your-token
```

**响应示例**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "roles": ["USER", "ADMIN"],
    "permissions": [
      "anime:read",
      "anime:write",
      "user:read",
      "user:write"
    ]
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

## 📊 系统管理 API

### 健康检查

#### 服务健康状态

```http
GET /actuator/health
```

**响应示例**：

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    },
    "redis": {
      "status": "UP",
      "details": {
        "version": "6.2.6"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 91943821312,
        "threshold": 10485760
      }
    }
  }
}
```

#### 应用信息

```http
GET /actuator/info
```

**响应示例**：

```json
{
  "app": {
    "name": "qing-service-anime",
    "version": "1.0.0",
    "description": "青动漫服务"
  },
  "build": {
    "time": "2024-01-01T12:00:00Z",
    "version": "1.0.0",
    "artifact": "qing-service-anime",
    "group": "com.stanic.qing"
  },
  "git": {
    "branch": "main",
    "commit": {
      "id": "abc123",
      "time": "2024-01-01T11:00:00Z"
    }
  }
}
```

### 监控指标

#### Prometheus 指标

```http
GET /actuator/prometheus
```

返回 Prometheus 格式的监控指标。

## 🔧 错误处理

### 错误响应格式

```json
{
  "code": 400,
  "message": "请求参数错误",
  "data": null,
  "errors": [
    {
      "field": "name",
      "message": "动漫名称不能为空",
      "rejectedValue": null
    }
  ],
  "timestamp": "2024-01-01T12:00:00",
  "path": "/api/v1/animes"
}
```

### 常见错误码

| 错误码  | 说明       | 解决方案            |
|------|----------|-----------------|
| 1001 | 参数验证失败   | 检查请求参数格式和必填项    |
| 1002 | 资源不存在    | 确认资源ID是否正确      |
| 1003 | 权限不足     | 检查用户权限或Token有效性 |
| 1004 | 重复操作     | 避免重复提交相同请求      |
| 1005 | 业务规则违反   | 检查业务逻辑约束        |
| 2001 | 数据库连接失败  | 联系系统管理员         |
| 2002 | 外部服务调用失败 | 稍后重试或联系技术支持     |

## 📝 使用示例

### JavaScript/Axios 示例

```javascript
// 配置 Axios 实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器 - 添加 Token
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// 响应拦截器 - 处理错误
api.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      // Token 过期，跳转到登录页
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 使用示例
async function getAnimeList(params) {
  try {
    const response = await api.get('/animes', { params });
    return response.data;
  } catch (error) {
    console.error('获取动漫列表失败:', error);
    throw error;
  }
}

async function createAnime(animeData) {
  try {
    const response = await api.post('/animes', animeData);
    return response.data;
  } catch (error) {
    console.error('创建动漫失败:', error);
    throw error;
  }
}
```

### Java/Spring 示例

```java
@Service
public class AnimeApiClient {
    
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api/v1";
    
    public AnimeApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public Page<AnimeVO> getAnimeList(int page, int size, String category) {
        String url = baseUrl + "/animes?page={page}&size={size}&category={category}";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getCurrentUserToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        ResponseEntity<ApiResponse<Page<AnimeVO>>> response = restTemplate.exchange(
            url, HttpMethod.GET, entity, 
            new ParameterizedTypeReference<ApiResponse<Page<AnimeVO>>>() {},
            page, size, category
        );
        
        return response.getBody().getData();
    }
    
    public AnimeVO createAnime(AnimeCreateDTO createDTO) {
        String url = baseUrl + "/animes";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getCurrentUserToken());
        HttpEntity<AnimeCreateDTO> entity = new HttpEntity<>(createDTO, headers);
        
        ResponseEntity<ApiResponse<AnimeVO>> response = restTemplate.exchange(
            url, HttpMethod.POST, entity,
            new ParameterizedTypeReference<ApiResponse<AnimeVO>>() {}
        );
        
        return response.getBody().getData();
    }
    
    private String getCurrentUserToken() {
        // 获取当前用户的 Token
        return "your-jwt-token";
    }
}
```

### Python/Requests 示例

```python
import requests
import json
from typing import Dict, List, Optional

class QingApiClient:
    def __init__(self, base_url: str = "http://localhost:8080/api/v1", token: str = None):
        self.base_url = base_url
        self.session = requests.Session()
        if token:
            self.session.headers.update({"Authorization": f"Bearer {token}"})
    
    def set_token(self, token: str):
        """设置认证 Token"""
        self.session.headers.update({"Authorization": f"Bearer {token}"})
    
    def get_anime_list(self, page: int = 0, size: int = 20, 
                      category: str = None, keyword: str = None) -> Dict:
        """获取动漫列表"""
        params = {"page": page, "size": size}
        if category:
            params["category"] = category
        if keyword:
            params["keyword"] = keyword
        
        response = self.session.get(f"{self.base_url}/animes", params=params)
        response.raise_for_status()
        return response.json()
    
    def get_anime_detail(self, anime_id: int) -> Dict:
        """获取动漫详情"""
        response = self.session.get(f"{self.base_url}/animes/{anime_id}")
        response.raise_for_status()
        return response.json()
    
    def create_anime(self, anime_data: Dict) -> Dict:
        """创建动漫"""
        response = self.session.post(
            f"{self.base_url}/animes",
            json=anime_data,
            headers={"Content-Type": "application/json"}
        )
        response.raise_for_status()
        return response.json()
    
    def login(self, username: str, password: str) -> Dict:
        """用户登录"""
        login_data = {"username": username, "password": password}
        response = self.session.post(
            f"{self.base_url}/auth/login",
            json=login_data,
            headers={"Content-Type": "application/json"}
        )
        response.raise_for_status()
        result = response.json()
        
        # 自动设置 Token
        if result.get("code") == 200:
            token = result["data"]["token"]
            self.set_token(token)
        
        return result

# 使用示例
if __name__ == "__main__":
    client = QingApiClient()
    
    # 登录
    login_result = client.login("user@example.com", "password123")
    print(f"登录结果: {login_result}")
    
    # 获取动漫列表
    anime_list = client.get_anime_list(page=0, size=10, category="动作")
    print(f"动漫列表: {anime_list}")
    
    # 创建动漫
    new_anime = {
        "name": "新动漫",
        "description": "这是一个新动漫",
        "categoryId": 1,
        "status": "ONGOING"
    }
    create_result = client.create_anime(new_anime)
    print(f"创建结果: {create_result}")
```

## 🔄 API 版本管理

### 版本策略

- **URL 版本控制**: `/api/v1/`, `/api/v2/`
- **向后兼容**: 新版本保持向后兼容性
- **废弃通知**: 提前通知 API 废弃计划
- **迁移指南**: 提供版本迁移文档

### 版本变更日志

#### v1.1.0 (计划中)

- 新增动漫评分功能
- 优化搜索性能
- 增加批量操作接口

#### v1.0.0 (当前版本)

- 基础动漫管理功能
- 用户认证和授权
- 分类管理功能

## 📚 相关资源

- [快速开始](../tutorial-basics/getting-started) - 项目安装和启动指南
- [用户指南](../tutorial-basics/user-guide) - 详细的功能使用说明
- [开发指南](./development) - 开发规范和最佳实践
- [部署指南](../tutorial-basics/deployment) - 生产环境部署指南

## 🆘 获取帮助

如果在使用 API 过程中遇到问题，可以通过以下方式获取帮助：

- 📖 查看完整文档
- 🐛 [提交 Issue](https://github.com/stanic-xyz/qing/issues)
- 💬 [参与讨论](https://github.com/stanic-xyz/qing/discussions)
- 📧 发送邮件：support@example.com

---

> 💡 **提示**：API 文档会随着功能更新持续完善，建议定期查看最新版本。所有示例代码仅供参考，实际使用时请根据具体需求进行调整。
