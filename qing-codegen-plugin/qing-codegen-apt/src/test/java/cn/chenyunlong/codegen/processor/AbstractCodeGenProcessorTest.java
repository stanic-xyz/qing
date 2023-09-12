/*
 * Copyright (c) 2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.codegen.processor;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.tools.JavaFileObject;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

@Schema
public class AbstractCodeGenProcessorTest extends TestCase {


    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public void testCompilesResourcesInJarFiles() {
        JavaFileObject fileObject = JavaFileObjects.forResource("test/TestAnnotation.java");
        assert_().about(javaSource()).that(fileObject).compilesWithoutError();
    }

    public void testGenerateSource() {

    }

    /**
     * 测试代码生成器连接
     */
    public void testQingCodeGenProcessorRegistry() throws IOException {

        QingCodeGenProcessor qingCodeGenProcessorRegistry =
            new QingCodeGenProcessor();

        List<File> classpath = new ArrayList<>();
        File swaggerAnnotationJar =
            new ClassPathResource("lib/swagger-annotations-2.2.8.jar").getFile();

        // 创建classpath目录
        folder.create();
        folder.newFolder("META-INF/services");
        File serviceFile = folder.newFile("cn.chenyunlong.codegen.handller.spi.CodeGenProcessor");
        FileUtil.writeBytes("cn.chenyunlong.codegen.processor.test.TestSchemaProcessor\n".getBytes(
            StandardCharsets.UTF_8), serviceFile);

        classpath.add(folder.getRoot());
        classpath.add(swaggerAnnotationJar);

        String fullyQualifiedName = "HelloWorld";
        JavaFileObject javaFileObject = JavaFileObjects.forSourceString(fullyQualifiedName, """
                            
            package cn.chenyunlong.coded;
                            
            import io.swagger.v3.oas.annotations.media.Schema;
            import cn.chenyunlong.codegen.annotation.GenDto;
            import cn.chenyunlong.codegen.annotation.GenCreator;
                            
            final class HelloWorld {
                           
                @Schema
                private String username;
            }
            """);
        ClassLoader classLoader = this.getClass().getClassLoader();
        Compiler compiler = javac().withClasspath(classpath);
        Compiler classpathFrom = compiler.withClasspathFrom(classLoader);
        Compiler withProcessors = classpathFrom.withProcessors(qingCodeGenProcessorRegistry);
        Compilation compilation = withProcessors.compile(javaFileObject);
        assertThat(compilation).succeededWithoutWarnings();
    }

}
