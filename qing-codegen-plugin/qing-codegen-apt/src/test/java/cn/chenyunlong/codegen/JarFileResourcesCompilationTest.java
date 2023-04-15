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

package cn.chenyunlong.codegen;

import cn.hutool.core.io.resource.ClassPathResource;
import com.google.common.io.Resources;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

@RunWith(JUnit4.class)
public class JarFileResourcesCompilationTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File jarFile;

    @Before
    public void createJarFile() throws IOException {
        this.jarFile = new ClassPathResource("lib/swagger-annotations-2.2.8.jar").getFile();
        JarOutputStream out = new JarOutputStream(new FileOutputStream(jarFile));
        JarEntry helloWorldEntry = new JarEntry("test/HelloWorld.java");
        out.putNextEntry(helloWorldEntry);
        out.write(Resources.toByteArray(Resources.getResource("test/HelloWorld.java")));
        out.close();
    }

    @Test
    public void compilesResourcesInJarFiles() throws IOException {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource(
                        new URL("jar:" + jarFile.toURI() + "!/test/HelloWorld.java")))
                .compilesWithoutError();
    }
}
