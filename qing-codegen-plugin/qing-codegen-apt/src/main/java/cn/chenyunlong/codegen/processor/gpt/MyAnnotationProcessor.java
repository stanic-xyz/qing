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

package cn.chenyunlong.codegen.processor.gpt;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("cn.chenyunlong.codegen.processor.gpt.Generator")
public class MyAnnotationProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.processingEnv = processingEnv;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                if (element.getKind() == ElementKind.CLASS) {
                    String className = element.getSimpleName().toString();
                    String packageName =
                            processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
                    String source = generateSource(className, packageName);
                    writeSourceFile(className, packageName, source);
                }
            }
        }
        return true;
    }

    public String generateSource(String className, String packageName) {
        // generate the source code here
        return "public class " + className + "Generated {\n" +
                "    // generated code here\n" +
                "}";
    }

    private void writeSourceFile(String className, String packageName, String source) {
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(packageName + "." + className +
                    "Generated");
            Writer writer = sourceFile.openWriter();
            writer.write(source);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
