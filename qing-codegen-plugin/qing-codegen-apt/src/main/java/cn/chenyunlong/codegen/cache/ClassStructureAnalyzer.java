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

package cn.chenyunlong.codegen.cache;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类结构分析器。
 * 提供轻量级的类结构哈希计算，用于智能缓存策略。
 * 只提取影响代码生成的关键信息，确保高性能。
 *
 * @author 陈云龙
 * @since 2024-01-20
 */
public class ClassStructureAnalyzer {
    
    private static final String HASH_ALGORITHM = "MD5";
    
    /**
     * 计算类结构哈希值。
     * 只包含影响代码生成的关键信息：
     * 1. 类名
     * 2. 字段名称、类型和注解
     * 3. 类级别注解
     * 
     * @param typeElement 类型元素
     * @return 类结构哈希值
     */
    public static String calculateStructureHash(TypeElement typeElement) {
        try {
            StringBuilder structureInfo = new StringBuilder();
            
            // 1. 类名
            structureInfo.append("class:").append(typeElement.getQualifiedName()).append(";");
            
            // 2. 类级别注解（排序确保一致性）
            List<String> classAnnotations = typeElement.getAnnotationMirrors().stream()
                .map(mirror -> mirror.getAnnotationType().toString())
                .sorted()
                .collect(Collectors.toList());
            structureInfo.append("annotations:").append(String.join(",", classAnnotations)).append(";");
            
            // 3. 字段信息（排序确保一致性）
            List<VariableElement> fields = ElementFilter.fieldsIn(typeElement.getEnclosedElements());
            List<String> fieldInfos = fields.stream()
                .filter(field -> !field.getModifiers().contains(Modifier.STATIC)) // 排除静态字段
                .map(ClassStructureAnalyzer::getFieldSignature)
                .sorted()
                .collect(Collectors.toList());
            structureInfo.append("fields:").append(String.join(",", fieldInfos)).append(";");
            
            // 计算MD5哈希
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = md.digest(structureInfo.toString().getBytes());
            
            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            // 如果MD5不可用，使用简单的字符串哈希
            return String.valueOf(typeElement.getQualifiedName().toString().hashCode());
        }
    }
    
    /**
     * 获取字段签名信息。
     * 包含字段名、类型和关键注解。
     * 
     * @param field 字段元素
     * @return 字段签名字符串
     */
    private static String getFieldSignature(VariableElement field) {
        StringBuilder signature = new StringBuilder();
        
        // 字段名
        signature.append(field.getSimpleName());
        
        // 字段类型
        TypeMirror fieldType = field.asType();
        signature.append(":").append(fieldType.toString());
        
        // 字段注解（只包含影响代码生成的注解）
        List<String> fieldAnnotations = field.getAnnotationMirrors().stream()
            .map(mirror -> mirror.getAnnotationType().toString())
            .filter(ClassStructureAnalyzer::isRelevantAnnotation)
            .sorted()
            .collect(Collectors.toList());
        
        if (!fieldAnnotations.isEmpty()) {
            signature.append("@").append(String.join(",", fieldAnnotations));
        }
        
        return signature.toString();
    }
    
    /**
     * 判断注解是否影响代码生成。
     * 只关注会影响生成代码的注解，忽略格式化相关的注解。
     * 
     * @param annotationName 注解名称
     * @return 是否相关
     */
    private static boolean isRelevantAnnotation(String annotationName) {
        // 包含影响代码生成的注解
        return annotationName.contains("FieldDesc") ||
               annotationName.contains("Schema") ||
               annotationName.contains("TypeConverter") ||
               annotationName.contains("Id") ||
               annotationName.contains("Column") ||
               annotationName.contains("JoinColumn") ||
               annotationName.contains("OneToMany") ||
               annotationName.contains("ManyToOne") ||
               annotationName.contains("ManyToMany") ||
               annotationName.contains("OneToOne");
    }
    
    /**
     * 比较两个类结构哈希是否相同。
     * 
     * @param hash1 哈希1
     * @param hash2 哈希2
     * @return 是否相同
     */
    public static boolean isSameStructure(String hash1, String hash2) {
        return Objects.equals(hash1, hash2);
    }
}