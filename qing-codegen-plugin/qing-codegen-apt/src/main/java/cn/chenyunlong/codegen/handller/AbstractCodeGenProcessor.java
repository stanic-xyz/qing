/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.codegen.handller;

import static cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder.fatalError;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.codegen.context.CodeGenContext;
import cn.chenyunlong.codegen.context.NameContext;
import cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder;
import cn.chenyunlong.codegen.handller.api.*;
import cn.chenyunlong.codegen.handller.controller.GenControllerProcessor;
import cn.chenyunlong.codegen.handller.creator.GenCreatorProcessor;
import cn.chenyunlong.codegen.handller.mapper.GenMapperProcessor;
import cn.chenyunlong.codegen.handller.query.GenQueryProcessor;
import cn.chenyunlong.codegen.handller.repository.GenRepositoryProcessor;
import cn.chenyunlong.codegen.handller.service.GenServiceImplProcessor;
import cn.chenyunlong.codegen.handller.service.GenServiceProcessor;
import cn.chenyunlong.codegen.handller.updater.GenUpdaterProcessor;
import cn.chenyunlong.codegen.handller.vo.GenVoCodeProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.annotation.TypeConverter;
import cn.hutool.core.annotation.AnnotationUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.squareup.javapoet.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import lombok.Data;

/**
 * 基础代码一代处理器。
 *
 * @author Stan
 * @since 2022/11/27
 */
public abstract class AbstractCodeGenProcessor implements CodeGenProcessor {


    protected ProcessingEnvironment processingEnvironment;
    private Filer filer;
    private Elements elementUtils;


    /**
     * 初始化
     *
     * @param processingEnvironment 处理环境
     */
    public void init(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
        filer = processingEnvironment.getFiler();
        // 获取当前类的模块相对路径
        elementUtils = processingEnvironment.getElementUtils();
    }


    /**
     * 支持方法
     *
     * @param typeElement      支持方法
     * @param roundEnvironment 周围环境
     * @return 是否支持处理该方法
     */
    @Override
    public boolean support(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        // 1、判断当前插件是否支持该对象的处理
        SupportedGenTypes supportedGenTypes =
            this.getClass().getAnnotation(SupportedGenTypes.class);
        // 判断插件支持注解
        if (supportedGenTypes == null) {
            return false;
        }
        // 获取当前插件主持的注解类型
        Class<? extends Annotation> aClass = supportedGenTypes.types();
        // 该插件支持的注解类型为空，不支持处理
        if (aClass != null) {
            Elements elementUtils = processingEnvironment.getElementUtils();
            return elementUtils
                .getAllAnnotationMirrors(typeElement)
                .stream()
                .anyMatch(annotationMirror -> aClass
                    .getTypeName()
                    .equals(annotationMirror.getAnnotationType().toString()));
        }
        return false;
    }

    /**
     * 生成类
     * 生成Class
     *
     * @param typeElement 顶层元素
     * @param roundEnv    周围环境
     * @param useLombok   是否使用lombok
     */
    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                              boolean useLombok) {

    }

    @Override
    public Name getDomainName(TypeElement typeElement) {
        return typeElement.getSimpleName();
    }

    /**
     * 获取生成的文件package。
     *
     * @return 生成的文件package
     */
    @Override
    public String getBasePackageName(TypeElement typeElement) {
        return elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }

    /**
     * 获取支持的注解。
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        SupportedGenTypes supportedGenTypes =
            this.getClass().getAnnotation(SupportedGenTypes.class);
        if (supportedGenTypes != null) {
            return supportedGenTypes.types();
        }
        return null;
    }


    /**
     * 是否重写文件。
     *
     * @return true，重写文件，false不支持重写
     */
    @Override
    public boolean overwrite() {
        boolean override = false;
        if (AnnotationUtil.hasAnnotation(this.getClass(), SupportedGenTypes.class)) {
            SupportedGenTypes supportedGenTypes =
                this.getClass().getAnnotation(SupportedGenTypes.class);
            override = supportedGenTypes.override();
            Class<? extends Annotation> types = supportedGenTypes.types();
        }
        return override;
    }

    /**
     * 获取字段信息。
     *
     * @param typeElement 类型元素
     * @param predicate   谓词
     * @return {@link Set}<{@link VariableElement}>
     */
    public Set<VariableElement> findFields(TypeElement typeElement, Predicate<VariableElement> predicate) {
        List<? extends Element> fieldTypes = typeElement.getEnclosedElements();
        Set<VariableElement> variableElements = new LinkedHashSet<>();
        for (VariableElement element : ElementFilter.fieldsIn(fieldTypes)) {
            if (predicate.test(element)) {
                variableElements.add(element);
            }
        }
        return variableElements;
    }

    /**
     * 获取名称默认上下文。
     *
     * @param typeElement 类型元素
     * @return {@link NameContext}
     */
    public NameContext getNameContext(TypeElement typeElement) {
        NameContext context = new NameContext();
        Name domainName = typeElement.getSimpleName();

        ProcessingEnvironment processingEnvironment =
            ProcessingEnvironmentHolder.getProcessingEnvironment();
        PackageElement packageElement =
            processingEnvironment.getElementUtils().getPackageOf(typeElement);

        Name qualifiedName = packageElement.getQualifiedName();
        String packageName = qualifiedName.toString();
        context.setBasePackage(packageName);
        Optional.ofNullable(typeElement.getAnnotation(GenBase.class)).ifPresent(anno -> {
            if (StringUtils.isNotBlank(anno.basePackage())) {
                context.setBasePackage(anno.basePackage());
            }
        });

//        System.out.println("文件生成根路径： " + context.getBasePackage());

        String serviceName =
            GenServiceProcessor.SERVICE_PREFIX + domainName + GenServiceProcessor.SERVICE_SUFFIX;
        String implName = domainName + GenServiceImplProcessor.IMPL_SUFFIX;
        String repositoryName = domainName + GenRepositoryProcessor.REPOSITORY_SUFFIX;
        String mapperName = domainName + GenMapperProcessor.SUFFIX;
        String voName = domainName + GenVoCodeProcessor.SUFFIX;
        String queryName = domainName + GenQueryProcessor.QUERY_SUFFIX;
        String creatorName = domainName + GenCreatorProcessor.SUFFIX;
        String updaterName = domainName + GenUpdaterProcessor.SUFFIX;
        String createRequestName = domainName + GenCreateRequestProcessor.CREATE_REQUEST_SUFFIX;
        String updateRequestName = domainName + GenUpdateRequestProcessor.UPDATE_REQUEST_SUFFIX;
        String queryRequestName = domainName + GenQueryRequestProcessor.QUERY_REQUEST_SUFFIX;
        String responseName = domainName + GenResponseProcessor.RESPONSE_SUFFIX;
        String feignName = domainName + GenFeignProcessor.FEIGN_SUFFIX;
        String controllerName = domainName + GenControllerProcessor.CONTROLLER_SUFFIX;

        // 设置上下文环境
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

        //  生成代码
        Optional
            .ofNullable(typeElement.getAnnotation(GenCreator.class))
            .ifPresent(creator -> context.setCreatorPackageName(creator.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenUpdater.class))
            .ifPresent(updater -> context.setUpdaterPackageName(updater.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenQuery.class))
            .ifPresent(query -> context.setQueryPackageName(query.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenVo.class))
            .ifPresent(genVo -> context.setVoPackageName(genVo.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenRepository.class))
            .ifPresent(repository -> context.setRepositoryPackageName(repository.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenMapper.class))
            .ifPresent(mapper -> context.setMapperPackageName(mapper.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenService.class))
            .ifPresent(service -> context.setServicePackageName(service.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenServiceImpl.class))
            .ifPresent(service -> context.setImplPackageName(service.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenCreateRequest.class))
            .ifPresent(createRequest -> context.setCreatePackageName(createRequest.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenUpdateRequest.class))
            .ifPresent(updateRequest -> context.setUpdatePackageName(updateRequest.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenQueryRequest.class))
            .ifPresent(queryRequest -> context.setQueryRequestPackageName(queryRequest.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenResponse.class))
            .ifPresent(response -> context.setResponsePackageName(response.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenFeign.class))
            .ifPresent(feign -> context.setFeignPackageName(feign.pkgName()));
        Optional
            .ofNullable(typeElement.getAnnotation(GenController.class))
            .ifPresent(controller -> context.setControllerPackageName(controller.pkgName()));
        return context;
    }

    /**
     * 获取父类。
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
     * 添加setter和getter方法，这个和上面的addSetterAndGetterMethodWithConverter只能调用一个。
     *
     * @param builder          构建器
     * @param variableElements 变量元素
     * @param useLombok        使用lombok
     */
    public void addSetterAndGetterMethod(TypeSpec.Builder builder,
                                         Set<VariableElement> variableElements, boolean useLombok) {
        for (VariableElement variableElement : variableElements) {
            TypeName typeName = TypeName.get(variableElement.asType());
            getDescriptionInfoBuilder(builder, variableElement, typeName, useLombok);
        }
    }

    /**
     * 获取描述信息生成器，得到描述信息构建器。
     *
     * @param builder   构建器
     * @param element   已经
     * @param typeName  类型名称
     * @param useLombok 使用lombok
     */
    private void getDescriptionInfoBuilder(TypeSpec.Builder builder, VariableElement element,
                                           TypeName typeName, boolean useLombok) {
        String fieldDescription = getFieldDesc(element);
        AnnotationSpec.Builder schemaAnnotationBuilder =
            AnnotationSpec.builder(Schema.class)
                .addMember("title", "$S", element.getSimpleName().toString());
        if (StringUtils.isNotBlank(fieldDescription)) {
            schemaAnnotationBuilder.addMember("description", "$S", fieldDescription);
        }
        FieldSpec.Builder fieldSpec = FieldSpec
            .builder(typeName, element.getSimpleName().toString(), Modifier.PRIVATE)
            .addAnnotation(schemaAnnotationBuilder.build());


        builder.addField(fieldSpec.build());

        // 不使用lombok
        String fieldName = getFieldDefaultName(element);
        MethodSpec.Builder getMethod = MethodSpec
            .methodBuilder("get" + fieldName)
            .returns(typeName)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return $L", element.getSimpleName().toString());
        MethodSpec.Builder setMethod = MethodSpec
            .methodBuilder("set" + fieldName)
            .returns(void.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(typeName, element.getSimpleName().toString())
            .addStatement("this.$L = $L", element.getSimpleName().toString(), element
                .getSimpleName()
                .toString());
        builder.addMethod(getMethod.build());
        builder.addMethod(setMethod.build());
    }

    /**
     * 得到字段描述信息。
     *
     * @param element 已经
     * @return {@link String}
     */
    protected String getFieldDesc(VariableElement element) {
        return Optional
            .ofNullable(element.getAnnotation(FieldDesc.class))
            .map(FieldDesc::name)
            .orElse(element.getSimpleName().toString());
    }

    /**
     * 获取字段默认名称。
     *
     * @param variableElement 变量元素
     * @return {@link String}
     */
    protected String getFieldDefaultName(VariableElement variableElement) {
        return StringUtils.bigCamel(variableElement.getSimpleName().toString());
    }

    /**
     * 使用转换器添加setter和getter方法
     * 应用转化器。
     *
     * @param builder          构建器
     * @param variableElements 变量元素
     * @param useLombok        是否使用lombok
     */
    public void addSetterAndGetterMethodWithConverter(TypeSpec.Builder builder,
                                                      Set<VariableElement> variableElements,
                                                      boolean useLombok) {
        for (VariableElement variableElement : variableElements) {
            TypeName typeName;
            if (Objects.nonNull(variableElement.getAnnotation(TypeConverter.class))) {
                //这里处理下泛型的情况，比如List<String> 这种，TypeConverter FullName 用逗号分隔"java.lang.List
                String fullName =
                    variableElement.getAnnotation(TypeConverter.class).toTypeFullName();
                Iterable<String> classes = Splitter.on(",").split(fullName);
                int size = Iterables.size(classes);
                if (size > 1) {
                    //泛型生成像这样
                    //ParameterizedTypeName.get(ClassName.get(JsonObject.class), ClassName.get(String.class))
                    typeName =
                        ParameterizedTypeName.get(ClassName.bestGuess(Iterables.get(classes, 0)),
                            ClassName.bestGuess(Iterables.get(classes, 1)));
                } else {
                    typeName = ClassName.bestGuess(
                        variableElement.getAnnotation(TypeConverter.class).toTypeFullName());
                }
            } else {
                typeName = TypeName.get(variableElement.asType());
            }
            getDescriptionInfoBuilder(builder, variableElement, typeName, useLombok);
        }
    }

    /**
     * 添加id setter和getter。
     *
     * @param builder   构建器
     * @param useLombok 使用启用lombok
     */
    protected void addIdField(TypeSpec.Builder builder, boolean useLombok) {
        builder.addField(
            FieldSpec.builder(ClassName.get(Long.class), "id", Modifier.PRIVATE).build());

        // 如果没有使用lombok，需要添加getter setter方法
        MethodSpec.Builder getMethod = MethodSpec
            .methodBuilder("getId")
            .returns(ClassName.get(Long.class))
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return $L", "id");
        MethodSpec.Builder setMethod = MethodSpec
            .methodBuilder("setId")
            .returns(void.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.LONG, "id")
            .addStatement("this.$L = $L", "id", "id");
        builder.addMethod(getMethod.build());
        builder.addMethod(setMethod.build());
    }

    /**
     * 获取生成源的类型信息类型。
     *
     * @param sourceName     源名称
     * @param packageName    包名
     * @param superClassName 超类名字
     * @return {@link TypeSpec.Builder}
     */
    public TypeSpec.Builder getSourceType(String sourceName, String packageName,
                                          String superClassName) {
        return TypeSpec
            .classBuilder(sourceName)
            .superclass(ClassName.get(packageName, superClassName))
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Schema.class);
    }

    /**
     * 源类型与构造。
     *
     * @param typeElement    typeElement
     * @param sourceName     源名称
     * @param packageName    包名
     * @param superClassName 父类名称
     * @return {@link TypeSpec.Builder}
     */
    public TypeSpec.Builder getSourceTypeWithConstruct(TypeElement typeElement, String sourceName,
                                                       String packageName, String superClassName) {
        MethodSpec.Builder constructorSpecBuilder = MethodSpec
            .constructorBuilder()
            .addParameter(TypeName.get(typeElement.asType()), "source")
            .addModifiers(Modifier.PUBLIC);
        constructorSpecBuilder.addStatement("super(source)");
        return TypeSpec
            .classBuilder(sourceName)
            .superclass(ClassName.get(packageName, superClassName))
            .addModifiers(Modifier.PUBLIC)
            .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build())
            .addMethod(constructorSpecBuilder.build())
            .addAnnotation(Schema.class)
            .addAnnotation(Data.class);
    }


    /**
     * 生成Java源文件。
     *
     * @param typeElement 包名
     * @param builder     类型规范创建器
     */
    public void genJavaFile(TypeElement typeElement, TypeSpec.Builder builder) {
        String subPackageName = getSubPackageName(typeElement);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getBasePackageName(typeElement));
        if (StringUtils.isNotBlank(subPackageName)) {
            stringBuilder.append(".").append(subPackageName);
        }
        // 生成.java文件
        TypeSpec typeSpec = builder.build();
        JavaFile javaFile = JavaFile
            .builder(stringBuilder.toString(), typeSpec)
            .indent("    ")
            .addFileComment("---Auto Generated by Qing-Generator --")
            .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException exception) {
            fatalError(exception.getMessage());
        }
    }

    /**
     * 生成Java源文件。
     *
     * @param typeElement 类型元素
     * @param builder     类型规范创建器
     */
    public void genJavaSourceFile(TypeElement typeElement, TypeSpec.Builder builder) {

        StringBuilder packageNameBuilder = new StringBuilder(getBasePackageName(typeElement));

        String subPackageName = getSubPackageName(typeElement);
        if (StringUtils.isNotBlank(subPackageName)) {
            packageNameBuilder.append(".").append(subPackageName);
        }

        // 生成.java文件
        TypeSpec typeSpec = builder.build();
        String packageName = packageNameBuilder.toString();
        JavaFile javaFile = JavaFile
            .builder(packageName, typeSpec)
            .indent("    ")
            .addFileComment("---Auto Generated by Qing-Generator --")
            .build();
        packageNameBuilder.append(".");

        File baseDir = CodeGenContext.getBaseDir();
        String sourcePath =
            baseDir != null ? baseDir.getAbsolutePath() : getSourcePath(typeElement);

        // 生成Java文件
        try {
            File pathFile = Paths.get(sourcePath).toFile();
            File file = new File(pathFile, packageName.replace(".", File.separator)
                                           + File.separator + typeSpec.name + ".java");
            if (!file.exists() || overwrite()) {
                javaFile.writeToFile(pathFile);
            }
        } catch (FileNotFoundException ignored) {
            System.out.println("文件不存在，需要重新生成");
        } catch (IOException exception) {
            fatalError(exception.getMessage());
        }
    }
}
