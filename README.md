# 留住TA的朋友圈

[![License](https://img.shields.io/badge/licence-MulanPSL2-blue)](http://license.coscl.org.cn/MulanPSL2)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/yunlongChen/qing/maven.yml?branch=main)

#### 介绍

该项目是我关于SpringCloud的一次实践 通过该项目我希望能够对SpringCloud更加了解的同时 不光是对我们本人技术的一点锻炼

还有一个目的就是系统通过自己的这点小小手一来做一些对自己有意义的事情

保住这美好的回忆。爱不爱了不说，至少回忆在这里。 还希望能够保管我自己的小秘密，留住自己喜欢的人的所有回忆

以后还能拿出来作为小小的谈资呢。（卑微.jpg）

#### 以下是关于项目的运行

目前的计划是集成到自动构建自动部署中，使用的构建工具是Coding的devops平台[CODING项目协同管理平台](https://stanic.coding.net/)

项目演示地址： [https://bangumi.chenyunlong.cn/index.html](https://bangumi.chenyunlong.cn/index.html)

该项目是本人在学习微服务中的一些实践，可以简单的作为参考（目前还没有完全实现，所以参考意义不大）

---

#### 一首诗给你听

哈哈哈 还不会写诗，就先放到这里了吧 你最美

---

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
- javax mail： 邮件发送 sdk
- thymeleaf 服务端页面渲染

#### 技术选型（待集成）

* [ ] querydsl &emsp;5.0
* [ ] javers (审计框架)
* [ ] flowable (流程引擎)
* [ ] spring-boot-starter-data-elasticsearch（elasticsearch 7.15.2)
* [ ] flink(1.13.2)，flinkcdc
* [ ] elasticjob (分布式任务调度)
* [ ] drools （规则引擎）
* [ ] thymeleaf 模板引擎，用于渲染首页

---

#### 安装教程

1. 先安装MySQL数据库
2. mvn install
3. java -jar qing-eureka-service.jar
4. 数据库使用MySQL数据库，数据库连接

#### 使用说明

- 目前该项目书私有项目，不打算公开出来
- eureka作为配置中心：
    - 设置注册中心的地址https://eureka.chenyunlong.cn/eureka/
    - 域名解析中添加两条TXT记录
        - txt.zhangli.chenyunlong.cn
        - txt.defaultZone.chenyunlong.cn
    - 使用自建DNS服务器
- 使用NACOS作为注册中心
    - 设置注册中心地址：http://nacos.chenyunlong.cn/nacos/
    - 域名解析设置了：

#### 参与贡献

1. 目前该项目书私有项目，不打算公开出来

---

#### 个人介绍

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. 码云官方博客 [chenyunlong.cn](https://www.chenyunlong.cn)，现在暂时没有东西，但是以后会完善起来的
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5. 码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. 码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)

