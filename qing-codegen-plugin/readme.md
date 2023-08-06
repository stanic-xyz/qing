## 使用方法

### 导入依赖;

```xml

<dependency>
    <groupId>cn.chenyunlong</groupId>
    <artifactId>qing-codegen-apt</artifactId>
    <version>latest-version</version>
</dependency>

```

### 添加需要生成的代码的注解

#### 配置文件生成路径

```java
// 静态常量
public interface Constants {

    // 测试中当前测试实体类所在的项目Java源文件目录作为生成地址，当前直接生成到示例项目的根目录
    String GEN_API_SOURCE =
        "D:\\workspace\\qing\\qing-codegen-plugin\\qing-codegen-samples\\src\\main\\java";

}
```

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
@GenRepository(sourcePath = Constants.GEN_API_SOURCE)
@GenService(sourcePath = Constants.GEN_API_SOURCE)
@GenServiceImpl(sourcePath = Constants.GEN_API_SOURCE)
@GenFeign(sourcePath = Constants.GEN_API_SOURCE, serverName = "stanic")
// Constants.GEN_API_SOURCE 表示源代码生成的目录
@GenController(sourcePath = Constants.GEN_API_SOURCE)
@GenMapper
@Data
@Entity
public class TestDomain extends BaseEntity {

    private String username;

    private String password;
}
```

### 配置Maven编译插件

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
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>
                            compile
                        </goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <source>17</source>
                <target>17</target>
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
    </plugins>
</build>
```
