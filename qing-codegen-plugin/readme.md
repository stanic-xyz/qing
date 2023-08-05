## 使用方法

### 导入依赖;

```xml


```

### 添加需要生成的代码的注解

```java

@GenVo
@GenCreator
@GenUpdater
@GenRepository
@GenService
@GenServiceImpl
@GenController
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenFeign
@Data
@GenMapper
public class TestDomain {

    private String username;

    private String password;

    private Integer order;
}
```

### 添加Maven编译插件

```xml

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
        <dependencies>
            <dependency>
                <groupId>cn.chenyunlong</groupId>
                <artifactId>qing-codegen-apt</artifactId>
                <version>1.0.0.RELEASE</version>
            </dependency>
        </dependencies>
        <configuration>
            <source>17</source>
            <target>17</target>
            <encoding>UTF-8</encoding>
            <verbose>true</verbose>
            <annotationProcessorPaths>
                <annotationProcessorPath>
                    <groupId>org.projectlombok</groupId>
                    <artifactId>lombok</artifactId>
                    <version>${lombok.version}</version>
                </annotationProcessorPath>
                <annotationProcessorPath>
                    <groupId>cn.chenyunlong</groupId>
                    <artifactId>qing-codegen-apt</artifactId>
                    <version>1.0.0.RELEASE</version>
                </annotationProcessorPath>
            </annotationProcessorPaths>
            <annotationProcessors>
                <processor>cn.chenyunlong.codegen.processor.QingCodeGenProcessor</processor>
            </annotationProcessors>
        </configuration>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-antrun-plugin</artifactId>
        <version>1.7</version>
        <executions>
            <execution>
                <id>clean</id>
                <phase>clean</phase>
                <configuration>
                    <target>
                        <echo message="remove test files from codegen"/>
                        <!--                                <delete dir="src/main/java/cn/chenyunlong/codegen/test/domain/creater/gen"/>-->
                        <!--                                <delete dir="src/main/java/cn/chenyunlong/codegen/test/domain/mapper/gen"/>-->
                        <!--                                <delete dir="src/main/java/cn/chenyunlong/codegen/test/domain/updater/gen"/>-->
                        <!--                                <delete dir="src/main/java/cn/chenyunlong/codegen/test/domain/vo/gen"/>-->
                        <!--                                <delete dir="src/main/java/cn/chenyunlong/codegen/test/domain/testobject/gen"/>-->
                    </target>
                </configuration>
                <goals>
                    <goal>run</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
```
