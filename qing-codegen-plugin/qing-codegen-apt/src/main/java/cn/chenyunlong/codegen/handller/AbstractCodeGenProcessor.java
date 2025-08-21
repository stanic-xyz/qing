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

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.codegen.cache.CacheManager;
import cn.chenyunlong.codegen.cache.CacheStrategy;
import cn.chenyunlong.codegen.context.CodeGenContext;
import cn.chenyunlong.codegen.context.NameContext;
import cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder;
import cn.chenyunlong.codegen.metrics.PerformanceMetrics;
import cn.chenyunlong.codegen.handller.api.*;
import cn.hutool.core.util.StrUtil;
import cn.chenyunlong.codegen.handller.controller.GenControllerProcessor;
import cn.chenyunlong.codegen.handller.dto.GenCreatorProcessor;
import cn.chenyunlong.codegen.handller.dto.GenQueryProcessor;
import cn.chenyunlong.codegen.handller.dto.GenUpdaterProcessor;
import cn.chenyunlong.codegen.handller.dto.GenVoCodeProcessor;
import cn.chenyunlong.codegen.handller.mapper.GenMapperProcessor;
import cn.chenyunlong.codegen.handller.repository.GenRepositoryProcessor;
import cn.chenyunlong.codegen.handller.service.GenServiceImplProcessor;
import cn.chenyunlong.codegen.handller.service.GenServiceProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.annotation.TypeConverter;
import cn.hutool.core.annotation.AnnotationUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.javapoet.*;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

import static cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder.fatalError;

/**
 * 基础代码一代处理器。
 *
 * @author 陈云龙
 * @since 2022/11/27
 */
@SuppressWarnings("unused")
public abstract class AbstractCodeGenProcessor implements CodeGenProcessor {


    protected ProcessingEnvironment processingEnvironment;
    private Filer filer;
    private Elements elementUtils;


    /**
     * 初始化注解处理器。
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
     * 判断该注解处理器是否支持处理该对象。
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
        Class<? extends Annotation> types = supportedGenTypes.types();
        // 该插件支持的注解类型为空，不支持处理
        if (types != null) {
            Elements elementUtils = processingEnvironment.getElementUtils();
            return elementUtils
                .getAllAnnotationMirrors(typeElement)
                .stream()
                .anyMatch(annotationMirror -> types
                    .getTypeName()
                    .equals(annotationMirror.getAnnotationType().toString()));
        }
        return false;
    }

    /**
     * 生成 Class 文件。
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
        }
        return override;
    }
    
    /**
      * 获取缓存策略。
      *
      * @return 缓存策略
      */
     protected CacheStrategy getCacheStrategy() {
         if (AnnotationUtil.hasAnnotation(this.getClass(), SupportedGenTypes.class)) {
             SupportedGenTypes supportedGenTypes =
                 this.getClass().getAnnotation(SupportedGenTypes.class);
             return supportedGenTypes.cacheStrategy();
         }
         return CacheStrategy.NONE;
     }
     
     /**
      * 判断是否应该生成文件。
      * 根据缓存策略决定是否需要生成文件。
      *
      * @param typeElement 类型元素
      * @param targetFile 目标文件
      * @param strategy 缓存策略
      * @return 是否应该生成文件
      */
     protected boolean shouldGenerateFile(TypeElement typeElement, File targetFile, CacheStrategy strategy) {
         String processorName = this.getClass().getSimpleName();
         
         switch (strategy) {
             case NONE:
                 // 无缓存策略，总是生成
                 ProcessingEnvironmentHolder.log(StrUtil.format(
                     "[{}] 无缓存策略，生成文件: {}", processorName, targetFile.getName()));
                 return true;
                 
             case SMART:
                 // 智能缓存策略，基于类结构哈希
                 if (!targetFile.exists()) {
                     ProcessingEnvironmentHolder.log(StrUtil.format(
                         "[{}] 文件不存在，需要生成: {}", processorName, targetFile.getName()));
                     return true;
                 }
                 
                 boolean structureChanged = CacheManager.isStructureChanged(typeElement);
                 if (structureChanged) {
                     ProcessingEnvironmentHolder.log(StrUtil.format(
                         "[{}] 类结构发生变化，重新生成: {}", processorName, targetFile.getName()));
                     return true;
                 } else {
                     ProcessingEnvironmentHolder.log(StrUtil.format(
                         "[{}] 类结构未变化，跳过生成: {}", processorName, targetFile.getName()));
                     return false;
                 }
                 
             case SKIP_IF_EXISTS:
                 // 跳过策略，文件存在时不生成
                 if (targetFile.exists()) {
                     ProcessingEnvironmentHolder.log(StrUtil.format(
                         "[{}] 文件已存在，跳过生成: {}", processorName, targetFile.getName()));
                     return false;
                 } else {
                     ProcessingEnvironmentHolder.log(StrUtil.format(
                         "[{}] 文件不存在，需要生成: {}", processorName, targetFile.getName()));
                     return true;
                 }
                 
             default:
                 // 默认行为，检查覆盖设置
                 if (targetFile.exists() && !overwrite()) {
                     ProcessingEnvironmentHolder.log(StrUtil.format(
                         "[{}] 文件已存在且不允许覆盖，跳过生成: {}", processorName, targetFile.getName()));
                     return false;
                 }
                 return true;
         }
     }

    /**
     * 获取字段信息。
     *
     * @param typeElement 类型元素
     * @param predicate   谓词
     * @return {@link Set}<{@link VariableElement}>
     */
    public Set<VariableElement> findFields(TypeElement typeElement,
                                           Predicate<VariableElement> predicate) {
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

        // System.out.println("文件生成根路径： " + context.getBasePackage());

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
     */
    public void addSetterAndGetterMethod(TypeSpec.Builder builder,
                                         Set<VariableElement> variableElements) {
        for (VariableElement variableElement : variableElements) {
            TypeName typeName = TypeName.get(variableElement.asType());
            getDescriptionInfoBuilder(builder, variableElement, typeName);
        }
    }

    /**
     * 获取描述信息生成器，得到描述信息构建器。
     *
     * @param builder  构建器
     * @param element  已经
     * @param typeName 类型名称
     */
    private void getDescriptionInfoBuilder(TypeSpec.Builder builder, VariableElement element,
                                           TypeName typeName) {
        String fieldDescription = getFieldDesc(element);
        String fieldName = element.getSimpleName().toString();
        
        // 构建Schema注解
        AnnotationSpec.Builder schemaAnnotationBuilder = AnnotationSpec.builder(Schema.class);
        
        // 如果有描述信息，使用描述信息作为title，否则使用字段名
        if (StringUtils.isNotBlank(fieldDescription)) {
            schemaAnnotationBuilder.addMember("description", "$S", fieldDescription);
        } else {
            schemaAnnotationBuilder.addMember("description", "$S", fieldName);
        }
        
        // 构建字段规范
        FieldSpec.Builder fieldSpec = FieldSpec
            .builder(typeName, fieldName, Modifier.PRIVATE)
            .addAnnotation(schemaAnnotationBuilder.build());
            
        builder.addField(fieldSpec.build());
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
     */
    public void addSetterAndGetterMethodWithConverter(TypeSpec.Builder builder,
                                                      Set<VariableElement> variableElements) {
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
            getDescriptionInfoBuilder(builder, variableElement, typeName);
        }
    }

    /**
     * 添加id setter和getter。
     *
     * @param builder 构建器
     */
    protected void addIdField(TypeSpec.Builder builder) {
        builder.addField(
            FieldSpec.builder(Long.class, "id", Modifier.PRIVATE).build());

        // 如果没有使用lombok，需要添加getter setter方法
        MethodSpec.Builder getMethod = MethodSpec
            .methodBuilder("getId")
            .returns(Long.class)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return $L", "id");
        MethodSpec.Builder setMethod = MethodSpec
            .methodBuilder("setId")
            .returns(void.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Long.class, "id")
            .addStatement("this.$L = $L", "id", "id");
        builder.addMethod(getMethod.build());
        builder.addMethod(setMethod.build());
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
            .indent("    ")  // 使用4个空格缩进
            .skipJavaLangImports(true)  // 跳过java.lang包的导入
            .addFileComment("Auto Generated by Qing Code Generator\n" +
                           "Do not modify this file manually\n" +
                           "Generated at: " + java.time.LocalDateTime.now())
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
        long startTime = System.currentTimeMillis();
        String processorName = this.getClass().getSimpleName();

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
            .indent("    ")  // 使用4个空格缩进
            .skipJavaLangImports(true)  // 跳过java.lang包的导入
            .addFileComment("Auto Generated by Qing Code Generator\n" +
                           "Do not modify this file manually\n" +
                           "Generated at: " + java.time.LocalDateTime.now())
            .build();

        File baseDir = CodeGenContext.getBaseDir();
        String sourcePath =
            baseDir != null ? baseDir.getAbsolutePath() : getSourcePath(typeElement);

        // 生成Java文件
        try {
            File pathFile = Paths.get(sourcePath).toFile();
            
            // 确保目录存在
            File packageDir = new File(pathFile, packageName.replace(".", File.separator));
            if (!packageDir.exists() && !packageDir.mkdirs()) {
                fatalError("无法创建目录: " + packageDir.getAbsolutePath());
                return;
            }
            
            File file = new File(packageDir, typeSpec.name + ".java");
            String fileName = file.getName();
            
            // 智能缓存策略检查
            long cacheCheckStart = System.currentTimeMillis();
            CacheStrategy strategy = getCacheStrategy();
            boolean shouldGenerate = shouldGenerateFile(typeElement, file, strategy);
            long cacheCheckTime = System.currentTimeMillis() - cacheCheckStart;
            
            // 记录缓存检查性能
            PerformanceMetrics.recordCacheCheck(processorName, cacheCheckTime, !shouldGenerate);
            
            if (!shouldGenerate) {
                PerformanceMetrics.recordFileSkipped(processorName, fileName, "缓存命中或文件已存在");
                return;
            }
            
            // 记录文件生成信息
            ProcessingEnvironmentHolder.log("正在生成文件: " + file.getAbsolutePath());
            
            javaFile.writeToFile(pathFile);
            
            // 标记文件已生成
            CacheManager.markFileGenerated(file.getAbsolutePath());
            
            long generationTime = System.currentTimeMillis() - startTime;
            PerformanceMetrics.recordFileGenerated(processorName, fileName, generationTime);
            
            ProcessingEnvironmentHolder.log("成功生成文件: " + file.getAbsolutePath());
            
        } catch (FileNotFoundException e) {
            ProcessingEnvironmentHolder.log("文件路径不存在: " + sourcePath + ", 错误: " + e.getMessage());
            fatalError("文件路径不存在: " + sourcePath);
        } catch (IOException exception) {
            ProcessingEnvironmentHolder.log("生成文件时发生IO异常: " + exception.getMessage());
            fatalError("生成文件失败: " + exception.getMessage());
        } catch (Exception e) {
            ProcessingEnvironmentHolder.log("生成文件时发生未知异常: " + e.getMessage());
            fatalError("生成文件时发生未知异常: " + e.getMessage());
        }
    }
}
