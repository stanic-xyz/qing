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

import cn.chenyunlong.codegen.processor.test.TestSchemaProcessor;
import cn.hutool.core.io.resource.ClassPathResource;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

@Schema
public class AbstractCodeGenProcessorTest extends TestCase {


    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public void testCompilesResourcesInJarFiles() throws IOException {
        File jarFile = new ClassPathResource("cn.chenyunlong.codegen.annotation.TestAnnotation").getFile();
        JavaFileObject javaFileObject = JavaFileObjects.forResource(new URL("jar:" + jarFile.toURI() + "!/test" +
                "/HelloWorld.java"));
        assert_()
                .about(javaSource())
                .that(javaFileObject)
                .compilesWithoutError();
    }

    public void testGenerateSource() {

    }

    /**
     * 测试代码生成器连接
     */
    public void testQingCodeGenProcessorRegistry() throws IOException {

        QingCodeGenProcessorRegistry qingCodeGenProcessorRegistry = new QingCodeGenProcessorRegistry();
        qingCodeGenProcessorRegistry.addProcessor(new TestSchemaProcessor());
        ArrayList<File> objects = new ArrayList<>();
        objects.add(new ClassPathResource("lib/swagger-annotations-2.2.8.jar").getFile());

//        JarOutputStream out = new JarOutputStream(new FileOutputStream(jarFile));
//        JarEntry helloWorldEntry = new JarEntry("test/HelloWorld.java");
//        out.putNextEntry(helloWorldEntry);
//        out.write(Resources.toByteArray(Resources.getResource("test/HelloWorld.java")));
//        out.close();


//        JavaFileObject fileObject = JavaFileObjects.forResource(
//                new URL("jar:" + jarFile.toURI() + "!/test/HelloWorld.java"));
        String fullyQualifiedName = "HelloWorld";
        JavaFileObject javaFileObject = JavaFileObjects.forSourceString(
                fullyQualifiedName,
                """
                        import io.swagger.v3.oas.annotations.media.Schema;
                                                        
                        final class HelloWorld {
                                       
                            @Schema
                            private String username;
                        }
                        """);
        Compilation compilation = javac()
                .withProcessors(qingCodeGenProcessorRegistry)
                .withClasspath(objects)
                .compile(javaFileObject);
        assertThat(compilation).succeededWithoutWarnings();
    }

}
