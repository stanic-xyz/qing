发布指南
=========

1. 更新即将发布的 CHANGELOG.md。
2. 更新新版本的 README.md 。
3. 执行 `git commit -am "Update changelog for X.Y.Z."` (此处的 X.Y.Z 表示新版本).
4. 执行 `mvn-release`.

* `What is the release version for "JavaPoet"? (com.squareup.javapoet) X.Y.Z:` - 点击回车键.
* `What is SCM release tag or label for "JavaPoet"? (com.squareup.javapoet) javapoet-X.Y.Z:` - 点击回车键.
* `What is the new development version for "JavaPoet"? (com.squareup.javapoet) X.Y.(Z + 1)-SNAPSHOT:` -
  输入 `X.(Y + 1).0-SNAPSHOT`.
* 提示是输入你的 GPG 密码 。
5. 访问Sonatype Nexus，并 promote 该版本.

如果第4步或者第5步执行失败:

* Drop the Sonatype repo,
* Fix the problem,
* Manually revert the version change in `pom.xml` made by `mvn-release`,
* Commit代码,
* 从第4步重新开始.

仓库配置
-------------

修改你本地的 `~/.m2/settings.xml`, 具体参见如下:

## server项目结构

```shell
├── api
│   └── v1
├── config
├── core
├── docs
├── global
├── initialize
│   └── internal
├── middleware
├── model
│   ├── request
│   └── response
├── packfile
├── resource
│   ├── excel
│   ├── page
│   └── template
├── router
├── service
├── source
└── utils
    ├── timer
    └── upload
```

| 文件夹          | 说明            | 描述                                                |
|--------------|---------------|---------------------------------------------------|
| `api`        | api层          | api层                                              |
| `--v1`       | v1版本接口        | v1版本接口                                            |
| `config`     | 配置包           | config.yaml对应的配置结构体                               |
| `core`       | 核心文件          | 核心组件(zap, viper, server)的初始化                      |
| `docs`       | swagger文档目录   | swagger文档目录                                       |
| `global`     | 全局对象          | 全局对象                                              |
| `initialize` | 初始化           | router,redis,gorm,validator, timer的初始化            |
| `--internal` | 初始化内部函数       | gorm 的 longger 自定义,在此文件夹的函数只能由 `initialize` 层进行调用 |
| `middleware` | 中间件层          | 用于存放 `gin` 中间件代码                                  |
| `model`      | 模型层           | 模型对应数据表                                           |
| `--request`  | 入参结构体         | 接收前端发送到后端的数据。                                     |
| `--response` | 出参结构体         | 返回给前端的数据结构体                                       |
| `packfile`   | 静态文件打包        | 静态文件打包                                            |
| `resource`   | 静态资源文件夹       | 负责存放静态文件                                          |
| `--excel`    | excel导入导出默认路径 | excel导入导出默认路径                                     |
| `--page`     | 表单生成器         | 表单生成器 打包后的dist                                    |
| `--template` | 模板            | 模板文件夹,存放的是代码生成器的模板                                |
| `router`     | 路由层           | 路由层                                               |
| `service`    | service层      | 存放业务逻辑问题                                          |
| `source`     | source层       | 存放初始化数据的函数                                        |
| `utils`      | 工具包           | 工具函数封装                                            |
| `--timer`    | timer         | 定时器接口封装                                           |
| `--upload`   | oss           | oss接口封装                                           |

### 快速执行

#### 快速创建功能

可以通过创建一下带注解的类，快速创建一个功能。待注解类完成后，执行`编译`命令即可实现简单的 CRUD 功能。
注意功能创建好之后，尽量不要提交带注解的类到 Git 因为注解处理器会影响其他人的启动项目

```java

@GenVo
@GenCreator
@GenUpdater
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenRepository
@GenService
@GenServiceImpl
@GenController
@GenMapper
@GenFeign(serverName = "stanic")
@Getter
@Setter
@ToString
@Entity
@Table(name = "anime_recommend")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Recommend extends BaseJpaAggregate {

   @NotBlank(message = "名称不能为空")
   @FieldDesc(description = "名称")
   private String name;

   @FieldDesc(description = "介绍")
   private String instruction;

}
```
