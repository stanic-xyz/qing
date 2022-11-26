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

package cn.chenyunlong.qing.infrastructure.utils;

import cn.chenyunlong.qing.infrastructure.exception.BeanUtilsException;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Bean utilities.
 *
 * @author johnniang
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * Bean方法名中属性名开始的下标
     */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /**
     * 匹配getter方法的正则表达式
     */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /**
     * 匹配setter方法的正则表达式
     */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    private BeanUtils() {
    }

    /**
     * Transforms from the source object. (copy same properties only)
     *
     * @param source      source data
     * @param targetClass target class must not be null
     * @param <T>         target class type
     * @return instance with specified type copying from source data; or null if source data is null
     * @throws BeanUtilsException if newing target instance failed or copying failed
     */
    @Nullable
    public static <T> T transformFrom(@Nullable Object source, @NonNull Class<T> targetClass) {
        Assert.notNull(targetClass, "Target class must not be null");

        if (source == null) {
            return null;
        }

        // Init the instance
        try {
            // New instance for the target class
            T targetInstance = targetClass.newInstance();
            // Copy properties
            org.springframework.beans.BeanUtils.copyProperties(source, targetInstance, getNullPropertyNames(source));
            // Return the target instance
            return targetInstance;
        } catch (Exception e) {
            throw new BeanUtilsException("Failed to new " + targetClass.getName() + " instance or copy properties", e);
        }
    }

    /**
     * Transforms from source data collection in batch.
     *
     * @param sources     source data collection
     * @param targetClass target class must not be null
     * @param <T>         target class type
     * @return target collection transforming from source data collection.
     * @throws BeanUtilsException if newing target instance failed or copying failed
     */
    @NonNull
    public static <T> List<T> transformFromInBatch(Collection<?> sources, @NonNull Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }

        // Transform in batch
        return sources.stream()
                .map(source -> transformFrom(source, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * Update properties (non null).
     *
     * @param source source data must not be null
     * @param target target data must not be null
     * @throws BeanUtilsException if copying failed
     */
    public static void updateProperties(@NonNull Object source, @NonNull Object target) {
        Assert.notNull(source, "source object must not be null");
        Assert.notNull(target, "target object must not be null");

        // Set non null properties from source properties to target properties
        try {
            org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } catch (BeansException e) {
            throw new BeanUtilsException("Failed to copy properties", e);
        }
    }

    /**
     * Gets null names array of property.
     *
     * @param source object data must not be null
     * @return null name array of property
     */
    @NonNull
    private static String[] getNullPropertyNames(@NonNull Object source) {
        return getNullPropertyNameSet(source).toArray(new String[0]);
    }

    /**
     * Gets null names set of property.
     *
     * @param source object data must not be null
     * @return null name set of property
     */
    @NonNull
    private static Set<String> getNullPropertyNameSet(@NonNull Object source) {

        Assert.notNull(source, "source object must not be null");
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = beanWrapper.getPropertyValue(propertyName);

            // if property value is equal to null, add it to empty name set
            if (propertyValue == null) {
                emptyNames.add(propertyName);
            }
        }
        return emptyNames;
    }

    /**
     * Bean属性复制工具方法。
     *
     * @param dest 目标对象
     * @param src  源对象
     */
    public static void copyBeanProp(Object dest, Object src) {
        try {
            copyProperties(src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象的setter方法。
     *
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static List<Method> getSetterMethods(Object obj) {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<Method>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods) {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1)) {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }

    /**
     * 获取对象的getter方法。
     *
     * @param obj 对象
     * @return 对象的getter方法列表
     */

    public static List<Method> getGetterMethods(Object obj) {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<Method>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods) {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0)) {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

    /**
     * 检查Bean方法名中的属性名是否相等。<br>
     * 如getName()和setName()属性名一样，getName()和setAge()属性名不一样。
     *
     * @param method1 方法名1
     * @param method2 方法名2
     * @return 属性名一样返回true，否则返回false
     */

    public static boolean isMethodPropEquals(String method1, String method2) {
        return method1.substring(BEAN_METHOD_PROP_INDEX).equals(method2.substring(BEAN_METHOD_PROP_INDEX));
    }
}
