### 工程模板自动生成

#### 变量

* groupId | 组名
* artifactId | 工程名字 qing-xxsrv
* package | 包名
* appName | appName

service 工程模板执行

```

mvn archetype:generate  -DgroupId=cn.chenyunlong -DartifactId=demosrv -Dversion=1.0.0-SNAPSHOT -Dpackage=cn.chenyunlong.demo -DappName=demo-srv -DarchetypeArtifactId=op-service-archetype -DarchetypeGroupId=cn.chenyunlong -DarchetypeVersion=1.0.0-SNAPSHOT
```

web 工程模板执行

```
mvn archetype:generate  -DgroupId=cn.chenyunlong -DartifactId=demoweb -Dversion=1.0.0-SNAPSHOT -Dpackage=cn.chenyunlong.demoweb -DappName=demo-web -DarchetypeArtifactId=op-web-archetype -DarchetypeGroupId=cn.chenyunlong -DarchetypeVersion=1.0.0-SNAPSHOT

```

api 工程模板执行

```

mvn archetype:generate  -DgroupId=com.cn.chenyunlong -DartifactId=demoapi -Dversion=1.0.0-SNAPSHOT -Dpackage=cn.chenyunlong.demoapi -DarchetypeArtifactId=op-api-archetype -DarchetypeGroupId=cn.chenyunlong -DarchetypeVersion=1.0.0-SNAPSHOT

```
