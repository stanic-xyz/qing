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

package cn.chenyunlong.codegen.processor.api;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import cn.chenyunlong.codegen.processor.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.processor.QingCodeGenProcessorRegistry;
import cn.hutool.core.io.resource.ClassPathResource;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import java.io.File;
import java.util.ArrayList;
import java.util.ServiceLoader;
import javax.tools.JavaFileObject;
import junit.framework.TestCase;

public class GenCreateRequestProcessorTest extends TestCase {

    private static final JavaFileObject HELLO_WORLD_RESOURCE =
        JavaFileObjects.forResource("test/HelloWorld.java");
    private static final JavaFileObject HELLO_WORLD_BROKEN_RESOURCE =
        JavaFileObjects.forResource("test/HelloWorld-broken.java");

    public void testGenerateClass() {

        ClassPathResource test = new ClassPathResource("test");
        GenUpdateRequestProcessor updateRequestProcessor = new GenUpdateRequestProcessor();
        QingCodeGenProcessorRegistry qingCodeGenProcessorRegistry =
            new QingCodeGenProcessorRegistry();
        ArrayList<File> objects = new ArrayList<>();
        objects.add(test.getFile());
        JavaFileObject javaFileObject = JavaFileObjects.forSourceString("HelloWorld",
            """
                final class HelloWorld
                {
                    @Schema(title = "password", description = "用户名")
                    private String username;
                }
                """
        );
        Compilation compilation = javac()
            .withProcessors(qingCodeGenProcessorRegistry)
            .withClasspath(objects)
            .compile(javaFileObject);

        assertThat(compilation)
            .succeededWithoutWarnings();
    }

    public void testClassLoader() {
        ServiceLoader<QingCodeGenProcessorRegistry> loaded =
            ServiceLoader.load(QingCodeGenProcessorRegistry.class,
                AbstractCodeGenProcessor.class.getClassLoader());
        loaded.forEach(baseCodeGenProcessor -> {
            System.out.println("baseCodeGenProcessor = " + baseCodeGenProcessor);
        });
    }

    /**
     * 测试代码生成器连接
     */
    public void testQingCodeGenProcessorRegistry() {
        ClassPathResource pathResource = new ClassPathResource("test");
        QingCodeGenProcessorRegistry qingCodeGenProcessorRegistry =
            new QingCodeGenProcessorRegistry();
        ArrayList<File> objects = new ArrayList<>();
        objects.add(pathResource.getFile());
        JavaFileObject javaFileObject = JavaFileObjects.forSourceString("HelloWorld",
            """
                final class HelloWorld {
                                        
                    @TestAnnotation
                    private String username;
                    
                    @interface TestAnnotation {}
                }
                """);
        Compilation compilation = javac()
            .withProcessors(qingCodeGenProcessorRegistry)
            .withClasspath(objects)
            .compile(javaFileObject);
        assertThat(compilation).succeededWithoutWarnings();
    }

    public void testHadErrorContainMatchPattern() {
        assertThat(compilerWithError().compile(HELLO_WORLD_BROKEN_RESOURCE))
            .hadErrorContainingMatch("not+ +a? statement")
            .inFile(HELLO_WORLD_BROKEN_RESOURCE)
            .onLine(23)
            .atColumn(5);
        assertThat(compilerWithError().compile(HELLO_WORLD_RESOURCE))
            .hadErrorContainingMatch("(wanted|expected) error!")
            .inFile(HELLO_WORLD_RESOURCE)
            .onLine(18)
            .atColumn(8);
    }

    private static Compiler compilerWithError() {
        return javac().withProcessors(new ErrorProcessor());
    }


}
