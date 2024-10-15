## 使用方法

### 编译插件

```shell
# 需要在项目根目录下执行install脚本
mvn clean install -pl qing-codegen-plugin/qing-codegen-apt -am -f pom.xml
```

### 导入依赖;

```xml

<dependency>
    <groupId>cn.chenyunlong</groupId>
    <artifactId>qing-codegen-apt</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>

```

### 添加需要生成的代码的注解

#### 编辑需要自动生成代码的实体类

```java

@Getter
@Setter
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
@GenFeign(serverName = "stanic-feign-client")
@GenController
@GenMapper
@Data
@Entity
public class TestDomain extends BaseEntity {

    private String username;

    private String password;
}
```

### 配置Maven编译插件（配置文件生成路径）

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <executions>
                <execution>
                    <id>compile</id>
                    <!-- 生成源代码期间执行注解处理器-->
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>
                            compile
                        </goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <source>${maven.compiler.source}</source>
                <target>${maven.compiler.source}</target>
                <encoding>UTF-8</encoding>
                <compilerArgs>
                    <!-- 指定源文件生成目录 ${project.basedir} 表示当前项目（子模块）的项目绝对地址，-A表示使用自定义编译选项-->
                    <arg>-AbaseDir=${project.basedir}</arg>
                    <!-- 打印注解处理器信息-->
                    <arg>-XprintProcessorInfo</arg>
                </compilerArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
```
