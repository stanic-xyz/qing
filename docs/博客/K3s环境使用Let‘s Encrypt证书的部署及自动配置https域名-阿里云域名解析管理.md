# K3s环境使用Let's Encrypt证书的部署及自动配置HTTPS域名

## ——阿里云域名解析管理全流程实践指南

*最后更新：2025年02月26日 | 预计阅读时间：15分钟*

---

### 引言

在边缘计算和轻量化Kubernetes场景中，K3s凭借其轻量级特性广受者青睐。但如何在K3s集群中实现HTTPS证书的自动化管理，仍是许多团队面临的挑战。本文将以**阿里云DNS服务**为核心，结合**Cert-Manager**与**Let's Encrypt**，详细解析在K3s环境下实现泛域名证书自动申请、部署与续期的完整方案[7,9](@ref)。

---

### 一、技术架构解析

#### 1.1 核心组件协作流程

```mermaid  
graph TD
    subgraph K3s环境
        A[K3s集群] -->|触发| B[CERT-MANAGER]
        B -->|创建证书请求| C[LET'S ENCRYPT]
    end

    subgraph 阿里云服务
        C -->|发起DNS - 01挑战| D[阿里云DNS API]
        D -->|创建_acme - challenge TXT记录| E((域名验证))
        E -->|验证通过| C
    end

    subgraph 证书管理
        C -->|签发证书| F[[TLS证书]]
        F -->|存储到Secret| G[Kubernetes Secret]
    end

    subgraph 流量入口
        G -->|挂载证书| H[NGINX INGRESS]
        H -->|服务暴露| I{{HTTPS流量}}
    end

    style A fill: #326CE5, color: white
    style B fill: #00C4B3, color: white
    style C fill: #80C141, color: white
    style D fill: #FF6A00, color: white
    style F fill: #BD34EB, color: white
    style H fill: #CB3837, color: white
```

```mermaid
%% 甘特图生成工具推荐：网页2的draw.io或网页5的51AI工具集
gantt
    title 动漫管理网站甘特图（2025-03-09启动）
    dateFormat YYYY-MM-DD
    axisFormat %m-%d
    todayMarker stroke:#FF0000,stroke-width:5px
%% 关键路径标注（参考网页3的crit标记法）
    section 阶段1：基础功能完善
        用户模块: user_module, 2025-03-10, 7d
        上传接口: crit, vedio_upload, after user_module, 3d
        播放器集成: crit, a3, after vedio_upload, 5d
        后台系统对接: a4, after a3, 7d

    section 阶段2：社区功能
        评论系统: comment_system, after a4, 3d
        创作中心: crit, b2, after comment_system, 5d
        推荐算法: crit, b3, after b2, 2d
        三方登录: b4, after b3, 2d

    section 阶段3：系统优化
        压力测试: crit, c1, after b4, 2d
        CDN: c2, after c1, 1d
        灰度发布: crit, c3, after c2, 1d

```

```mermaid
journey
    title 用户购物旅程
    section 购物开始
        用户浏览商品: 5: 选择商品并查看详情
    section 添加购物车
        用户加入购物车: 4: 添加商品到购物车
    section 付款流程
        用户填写地址: 3: 填写收货地址
        用户付款: 5: 完成付款
    section 订单完成
        用户确认收货: 5: 完成收货并评价商品
```

```mermaid
gitGraph
    commit
    commit
    branch develop
    checkout develop
    commit
    commit
    checkout main
    merge develop
    commit
    commit
```


```mermaid

pie
    title 家庭预算
    "食品": 30
    "住房": 40
    "教育": 20
    "娱乐": 10
    "开发": 260
```


```mermaid
sequenceDiagram
    participant A as 爸爸
    participant B as 妈妈
    participant C as 孩子
    A ->> B: 提议周末家庭聚会
    B ->> C: 向孩子询问意见
    C -->> B: 孩子表示同意
    B ->> A: 妈妈确认计划
    A ->> B: 预定场地
    A ->> C: 告知孩子聚会详情
```
