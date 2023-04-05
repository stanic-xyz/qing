/*
 * Copyright (c) 2019-2023  YunLong Chen
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

package cn.chenyunlong.nat.tools;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 类拷贝，解放深度克隆
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-06-29 15:29:06
 */
public class CopyPropertiesUtils {

    /**
     * 获取null属性名称
     * 获取为null的参数，进行忽略
     *
     * @param source 来源
     * @return {@link String[]}
     * @author wangmin1994@qq.com
     * @since 2019-06-29 15:33:35
     */
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptorArray = beanWrapper.getPropertyDescriptors();
        Set<String> nullNames = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptorArray) {
            Object sourceValue = beanWrapper.getPropertyValue(propertyDescriptor.getName());
            if (sourceValue == null) {
                nullNames.add(propertyDescriptor.getName());
            }
        }
        String[] result = new String[nullNames.size()];
        return nullNames.toArray(result);
    }

    /**
     * 复制财产忽略null
     * 拷贝属性
     *
     * @param source 来源
     * @param target 目标
     * @author wangmin1994@qq.com
     * @since 2019-06-29 15:33:55
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
}
