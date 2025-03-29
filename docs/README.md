# Project-青

[![License](https://img.shields.io/badge/Licence-MulanPSL2-blue)](http://license.coscl.org.cn/MulanPSL2)
[![star](https://gitee.com/stanChen/qing/badge/star.svg?theme=dark)](https://gitee.com/stanChen/qing)
[![Build](https://github.com/stanic-xyz/qing/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/stanic-xyz/qing/actions/workflows/build.yml)

### 🌐 代码仓库

| 后端源码                                                                                     | 前端源码                                                                                                       |
|------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| [Gitee](https://gitee.com/stanChen/qing) \| [GitHub](https://github.com/stanic-xyz/qing) | [Gitee](https://gitee.com/stanChen/qing-frontend) \| [GitHub](https://github.com/stanic-xyz/qing-frontend) |

---

#### 📚项目简介

最终目标：`DDD`（`领域驱动思想`）的落地实践！

该项目是本人在学习微服务中的一些实践，可以简单的作为参考（目前还没有完全实现，所以参考意义不大）

该项目是我关于SpringCloud的一次实践 通过该项目我希望能够对SpringCloud更加了解的同时 不光是对我们本人技术的一点锻炼

还有一个目的就是系统通过自己的这点小小手一来做一些对自己有意义的事情

~~希望能够保管我自己的小秘密，留住自己喜欢的人的所有回忆。~~
已经是公开项目了，再来做这种事情不太合适 吧~ （保留可能性）

#### ✈️安装运行

- 演示地址： [https://bangumi.chenyunlong.cn/index.html](https://bangumi.chenyunlong.cn/index.html)
- eureka作为配置中心：
    - 设置注册中心的地址https://eureka.chenyunlong.cn/eureka/
    - 域名解析中添加两条TXT记录
        - txt.zhangli.chenyunlong.cn
        - txt.defaultZone.chenyunlong.cn
    - 使用自建DNS服务器
- 使用NACOS作为注册中心
    - 设置注册中心地址：http://nacos.chenyunlong.cn/nacos/
    - 域名解析设置：

#### 🔁 CI/CD

- Coding 流水线
    - 地址地址：[CODING项目协同管理平台](https://stanic.coding.net/p/qing)
- GitHub Actions
    - 设置注册中心地址：http://nacos.chenyunlong.cn/nacos/
- Gitee GO
    - 流水线地址：[链接](https://gitee.com/stanChen/qing/gitee_go/pipelines)

---

#### 项目结构

```shell
Qing
├── .github               GitHub流水线
├── .workflow             Gitee流水线
├── README.md             README文件
├── Roadmap.md            项目路线图
├── doc                   项目文档
├── checkstyle            代码风格约束
├── charts                Helm部署相关
├── qing-bom              依赖管理项目
├── qing-commons          公共包
├── qing-app              应用能力层
├── qing-bootstrap        服务层
├── qing-domain           领域能力层
├── qing-starters-samples 各种能力的starter
├── qing-starters-samples 各种能力的starter
├── qing-ui               前端工程集成
└── ...                   其他工程文件
```

#### 软件选型

- Spring Boot ：应用层容器
- Spring Cloud &emsp;2021.0.4
- Spring Cloud Alibaba &emsp;2021.0.4.0
- MySQL ：数据库
- mybatis-plus ：持久层框架
- spring-data &emsp;2021.1.4 ：持久层框架
- Spring Security ：权限控制
- Springdoc OpenApi ui&emsp;1.6.8 在线文档工具
- spring cloud gateway 微服务网关
- Spring Batch ：批处理框架
- Fastjson ：json序列化工具
- HuTools：非常好用的Java工具集，**强烈推荐**
- javax mail： 邮件发送 sdk

#### 技术选型（待集成）

* [ ] querydsl &emsp;5.0
* [ ] javers (审计框架)
* [ ] flowable (流程引擎)
* [ ] spring-boot-starter-data-elasticsearch（elasticsearch 7.15.2)
* [ ] flink(1.13.2)，flinkcdc
* [ ] elasticjob (分布式任务调度)
* [ ] drools （规则引擎）

---

#### 🏗️参与贡献

项目路线图请见文档：[ROADMAP.MD](Roadmap.md)，如果您感觉我们的代码有需要优化的地方或者有更好的方案欢迎随时提pr，步骤如下：

1. 在`Gitee`或者`Github`上`fork`项目到自己的`repo`
2. 把`fork`过去的项目也就是你的项目`clone`到你的本地
3. 修改代码
4. `commit`后`push`到自己的库
5. 登录`Gitee`或`Github`在你仓库首页可以看到一个 `pull request` 按钮，点击它，填写一些说明信息，然后提交即可。
   等待维护者合并

#### 🐞提供bug反馈或建议

提交问题反馈请说明遇到的问题、如果可以请尽量详细或加图片以便于我们去复现

#### 📏PR准则

欢迎任何人为本项目添砖加瓦，规范如下：

- **注释完备**：尤其每个新增的方法应按照Java文档规范标明方法说明、参数说明、返回值说明等信息，必要时请添加单元测试，如果愿意，也可以加上你的大名。
- **风格统一**：本项目使用 checkstyle
  插件，来保证每位提交者代码的风格保持一致，减少无效代码的修改。详细部署文档请查阅：[HELP.MD](doc/checkstyle.md)
- **最小依赖**：新加的方法尽可能不要使用额外的第三方库方法
- **友好讨论**：我们如果关闭了你的`issue`或`pr`，请不要诧异，这是我们保持问题处理整洁的一种方式，你依旧可以继续讨论，当有讨论结果时我们会重新打开。

#### 🚚许可证

本项目在 【木兰宽松许可证, 第2版】 协议下开源，请遵守开源协议。

#### 💯虽然道路曲折，但你仍在最优道路上！

青，取之于蓝而青于蓝——荀子《劝学》
