/*
 * Copyright (c) 2019-2022 YunLong Chen
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

import cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder;
import cn.chenyunlong.codegen.processor.api.*;
import cn.chenyunlong.codegen.processor.controller.GenController;
import cn.chenyunlong.codegen.processor.controller.GenControllerProcessor;
import cn.chenyunlong.codegen.processor.creator.CreatorCodeGenProcessor;
import cn.chenyunlong.codegen.processor.creator.GenCreator;
import cn.chenyunlong.codegen.processor.mapper.GenMapper;
import cn.chenyunlong.codegen.processor.mapper.GenMapperProcessor;
import cn.chenyunlong.codegen.processor.query.GenQuery;
import cn.chenyunlong.codegen.processor.query.GenQueryProcessor;
import cn.chenyunlong.codegen.processor.repository.GenRepository;
import cn.chenyunlong.codegen.processor.repository.GenRepositoryProcessor;
import cn.chenyunlong.codegen.processor.service.GenService;
import cn.chenyunlong.codegen.processor.service.GenServiceImpl;
import cn.chenyunlong.codegen.processor.service.GenServiceImplProcessor;
import cn.chenyunlong.codegen.processor.service.GenServiceProcessor;
import cn.chenyunlong.codegen.processor.updater.GenUpdater;
import cn.chenyunlong.codegen.processor.updater.GenUpdaterProcessor;
import cn.chenyunlong.codegen.processor.vo.GenVo;
import cn.chenyunlong.codegen.processor.vo.VoCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.annotation.TypeConverter;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.squareup.javapoet.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

/**
 * 基础代码一代处理器
 *
 * @author gim
 * @date 2022/10/24
 */
public abstract class BaseCodeGenProcessor implements CodeGenProcessor {

    public static final String PREFIX = "Base";

    @Override
    public void generate(TypeElement typeElement, RoundEnvironment roundEnvironment)
            throws Exception {
        //添加其他逻辑扩展
        generateClass(typeElement, roundEnvironment);
    }

    /**
     * 生成类
     *
     * @param typeElement      类型元素
     * @param roundEnvironment 周围环境
     */
    protected abstract void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) throws Exception;

    /**
     * 获取字段信息
     *
     * @param typeElement 类型元素
     * @param predicate   谓词
     * @return {@link Set}<{@link VariableElement}>
     */
    public Set<VariableElement> findFields(TypeElement typeElement,
                                           Predicate<VariableElement> predicate) {
        List<? extends Element> fieldTypes = typeElement.getEnclosedElements();
        Set<VariableElement> variableElements = new LinkedHashSet<>();
        for (VariableElement e : ElementFilter.fieldsIn(fieldTypes)) {
            if (predicate.test(e)) {
                variableElements.add(e);
            }
        }
        return variableElements;
    }

    /**
     * 获取名称默认上下文
     *
     * @param typeElement 类型元素
     * @return {@link DefaultNameContext}
     */
    public DefaultNameContext getNameContext(TypeElement typeElement) {
        DefaultNameContext context = new DefaultNameContext();
        String serviceName = GenServiceProcessor.SERVICE_PREFIX + typeElement.getSimpleName()
                + GenServiceProcessor.SERVICE_SUFFIX;
        String implName = typeElement.getSimpleName() + GenServiceImplProcessor.IMPL_SUFFIX;
        String repositoryName = typeElement.getSimpleName() + GenRepositoryProcessor.REPOSITORY_SUFFIX;
        String mapperName = typeElement.getSimpleName() + GenMapperProcessor.SUFFIX;
        String voName = typeElement.getSimpleName() + VoCodeGenProcessor.SUFFIX;
        String queryName = typeElement.getSimpleName() + GenQueryProcessor.QUERY_SUFFIX;
        String creatorName = typeElement.getSimpleName() + CreatorCodeGenProcessor.SUFFIX;
        String updaterName = typeElement.getSimpleName() + GenUpdaterProcessor.SUFFIX;
        String createRequestName =
                typeElement.getSimpleName() + GenCreateRequestProcessor.CREATE_REQUEST_SUFFIX;
        String updateRequestName =
                typeElement.getSimpleName() + GenUpdateRequestProcessor.UPDATE_REQUEST_SUFFIX;
        String queryRequestName =
                typeElement.getSimpleName() + GenQueryRequestProcessor.QUERY_REQUEST_SUFFIX;
        String responseName = typeElement.getSimpleName() + GenResponseProcessor.RESPONSE_SUFFIX;
        String feignName = typeElement.getSimpleName() + GenFeignProcessor.FEIGN_SUFFIX;
        String controllerName = typeElement.getSimpleName() + GenControllerProcessor.CONTROLLER_SUFFIX;
        context.setServiceClassName(serviceName);
        context.setRepositoryClassName(repositoryName);
        context.setMapperClassName(mapperName);
        context.setVoClassName(voName);
        context.setQueryClassName(queryName);
        context.setCreatorClassName(creatorName);
        context.setUpdaterClassName(updaterName);
        context.setImplClassName(implName);
        context.setCreateClassName(createRequestName);
        context.setUpdateClassName(updateRequestName);
        context.setQueryRequestClassName(queryRequestName);
        context.setResponseClassName(responseName);
        context.setFeignClassName(feignName);
        context.setControllerClassName(controllerName);
        Optional.ofNullable(typeElement.getAnnotation(GenCreator.class)).ifPresent(anno
                -> context.setCreatorPackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenUpdater.class)).ifPresent(anno
                -> context.setUpdaterPackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenQuery.class)).ifPresent(anno
                -> context.setQueryPackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenVo.class)).ifPresent(anno
                -> context.setVoPackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenRepository.class)).ifPresent(anno
                -> context.setRepositoryPackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenMapper.class)).ifPresent(anno
                -> context.setMapperPackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenService.class)).ifPresent(anno
                -> context.setServicePackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenServiceImpl.class)).ifPresent(anno
                -> context.setImplPackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenCreateRequest.class)).ifPresent(anno
                -> context.setCreatePackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenUpdateRequest.class)).ifPresent(anno
                -> context.setUpdatePackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenQueryRequest.class)).ifPresent(anno
                -> context.setQueryRequestPackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenResponse.class)).ifPresent(anno
                -> context.setResponsePackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenFeign.class)).ifPresent(anno
                -> context.setFeignPackageName(anno.pkgName()));
        Optional.ofNullable(typeElement.getAnnotation(GenController.class)).ifPresent(anno
                -> context.setControllerPackageName(anno.pkgName()));
        return context;
    }

    /**
     * 获取父类
     *
     * @param element 元素
     * @return {@link TypeElement}
     */
    @SuppressWarnings("unused")
    public TypeElement getSuperClass(TypeElement element) {
        TypeMirror parent = element.getSuperclass();
        if (parent instanceof DeclaredType) {
            Element elt = ((DeclaredType) parent).asElement();
            if (elt instanceof TypeElement) {
                return (TypeElement) elt;
            }
        }
        return null;
    }

    /**
     * 添加setter和getter方法
     *
     * @param builder          构建器
     * @param variableElements 变量元素
     */
    public void addSetterAndGetterMethod(TypeSpec.Builder builder, Set<VariableElement> variableElements) {
        for (VariableElement variableElement : variableElements) {
            TypeName typeName = TypeName.get(variableElement.asType());
            getDescriptionInfoBuilder(builder, variableElement, typeName);
        }
    }

    /**
     * 得到描述信息构建器
     *
     * @param builder  构建器
     * @param ve       已经
     * @param typeName 类型名称
     */
    private void getDescriptionInfoBuilder(TypeSpec.Builder builder, VariableElement ve, TypeName typeName) {
        FieldSpec.Builder fieldSpec = FieldSpec
                .builder(typeName, ve.getSimpleName().toString(), Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(Schema.class)
                        .addMember("title", "$S", getFieldDesc(ve))
                        .build());
        builder.addField(fieldSpec.build());
        String fieldName = getFieldDefaultName(ve);
        MethodSpec.Builder getMethod = MethodSpec.methodBuilder("get" + fieldName)
                .returns(typeName)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $L", ve.getSimpleName().toString());
        MethodSpec.Builder setMethod = MethodSpec.methodBuilder("set" + fieldName)
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(typeName, ve.getSimpleName().toString())
                .addStatement("this.$L = $L", ve.getSimpleName().toString(),
                        ve.getSimpleName().toString());
        builder.addMethod(getMethod.build());
        builder.addMethod(setMethod.build());
    }

    /**
     * 应用转化器
     *
     * @param builder          构建器
     * @param variableElements 变量元素
     */
    public void addSetterAndGetterMethodWithConverter(TypeSpec.Builder builder,
                                                      Set<VariableElement> variableElements) {
        for (VariableElement ve : variableElements) {
            TypeName typeName;
            if (Objects.nonNull(ve.getAnnotation(TypeConverter.class))) {
                //这里处理下泛型的情况，比如List<String> 这种，TypeConverter FullName 用逗号分隔"java.lang.List
                String fullName = ve.getAnnotation(TypeConverter.class).toTypeFullName();
                Iterable<String> classes = Splitter.on(",").split(fullName);
                int size = Iterables.size(classes);
                if (size > 1) {
                    //泛型生成像这样
                    //ParameterizedTypeName.get(ClassName.get(JsonObject.class), ClassName.get(String.class))
                    typeName = ParameterizedTypeName.get(ClassName.bestGuess(Iterables.get(classes, 0)), ClassName.bestGuess(Iterables.get(classes, 1)));
                } else {
                    typeName = ClassName.bestGuess(ve.getAnnotation(TypeConverter.class).toTypeFullName());
                }
            } else {
                typeName = TypeName.get(ve.asType());
            }
            getDescriptionInfoBuilder(builder, ve, typeName);
        }
    }


    /**
     * 添加id setter和getter
     *
     * @param builder 构建器
     */
    protected void addIdSetterAndGetter(TypeSpec.Builder builder) {
        MethodSpec.Builder getMethod = MethodSpec.methodBuilder("getId")
                .returns(ClassName.get(Long.class))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $L", "id");
        MethodSpec.Builder setMethod = MethodSpec.methodBuilder("setId")
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.LONG, "id")
                .addStatement("this.$L = $L", "id", "id");
        builder.addMethod(getMethod.build());
        builder.addMethod(setMethod.build());
    }

    /**
     * 得到字段描述信息
     *
     * @param ve 已经
     * @return {@link String}
     */
    protected String getFieldDesc(VariableElement ve) {
        return Optional.ofNullable(ve.getAnnotation(FieldDesc.class))
                .map(FieldDesc::name).orElse(ve.getSimpleName().toString());
    }

    protected String getFieldDefaultName(VariableElement ve) {
        return ve.getSimpleName().toString().substring(0, 1).toUpperCase() + ve.getSimpleName()
                .toString().substring(1);
    }


    /**
     * 生成java源文件
     *
     * @param packageName     包名
     * @param pathStr         路径str
     * @param typeSpecBuilder 类型规范施工
     */
    public void genJavaSourceFile(String packageName, String pathStr,
                                  TypeSpec.Builder typeSpecBuilder) {
        TypeSpec typeSpec = typeSpecBuilder.build();
        JavaFile javaFile = JavaFile
                .builder(packageName, typeSpec)
                .addFileComment("---Auto Generated by Only4Play ---")
                .build();
//    System.out.println(javaFile);
        String packagePath =
                packageName.replace(".", File.separator) + File.separator + typeSpec.name + ".java";
        try {
            Path path = Paths.get(pathStr);
            File file = new File(path.toFile().getAbsolutePath());
            if (!file.exists()) {
                return;
            }
            String sourceFileName = path.toFile().getAbsolutePath() + File.separator + packagePath;
            File sourceFile = new File(sourceFileName);
            if (!sourceFile.exists()) {
                javaFile.writeTo(file);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取生成源的类型信息类型
     *
     * @param sourceName     源名称
     * @param packageName    包名
     * @param superClassName 超类名字
     * @return {@link TypeSpec.Builder}
     */
    public TypeSpec.Builder getSourceType(String sourceName, String packageName,
                                          String superClassName) {
        return TypeSpec.classBuilder(sourceName)
                .superclass(ClassName.get(packageName, superClassName))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Schema.class)
                .addAnnotation(Data.class);
    }

    /**
     * 源类型与构造
     *
     * @param e              e
     * @param sourceName     源名称
     * @param packageName    包名
     * @param superClassName 超类名字
     * @return {@link TypeSpec.Builder}
     */
    public TypeSpec.Builder getSourceTypeWithConstruct(TypeElement e, String sourceName,
                                                       String packageName, String superClassName) {
        MethodSpec.Builder constructorSpecBuilder = MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(e.asType()), "source")
                .addModifiers(Modifier.PUBLIC);
        constructorSpecBuilder.addStatement("super(source)");
        return TypeSpec.classBuilder(sourceName)
                .superclass(ClassName.get(packageName, superClassName))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .build())
                .addMethod(constructorSpecBuilder.build())
                .addAnnotation(Schema.class)
                .addAnnotation(Data.class);
    }


    /**
     * 生成java文件
     *
     * @param packageName     包名
     * @param typeSpecBuilder 类型规范施工
     */
    protected void genJavaFile(String packageName, TypeSpec.Builder typeSpecBuilder) {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpecBuilder.build())
                .addFileComment("---Auto Generated by Only4Play ---").build();
        try {
            javaFile.writeTo(ProcessingEnvironmentHolder.getEnvironment().getFiler());
        } catch (IOException e) {
            ProcessingEnvironmentHolder.getEnvironment().getMessager()
                    .printMessage(Kind.ERROR, e.getMessage());
        }
    }

}
