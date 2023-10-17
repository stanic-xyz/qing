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

package cn.chenyunlong.common.model.dto.base;


import cn.chenyunlong.common.utils.BeanUtils;
import cn.chenyunlong.common.utils.ReflectionUtils;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * 用于输入DTO的转换器接口。
 *
 * @author johnniang
 */
public interface InputConverter<DOMAIN> {

    /**
     * 转换到Domain领域对象。(浅拷贝)
     *
     * @return 具有相同值的新域 (非空)
     */
    default DOMAIN convertTo() {
        // Get parameterized type
        ParameterizedType currentType = parameterizedType();

        // Assert not equal
        Objects.requireNonNull(currentType,
            "Cannot fetch actual type because parameterized type is null");

        Class<DOMAIN> domainClass = (Class<DOMAIN>) currentType.getActualTypeArguments()[0];

        return BeanUtils.transformFrom(this, domainClass);
    }

    /**
     * 获取参数化类型。
     *
     * @return parameterized type or null
     */
    @Nullable
    default ParameterizedType parameterizedType() {
        return ReflectionUtils.getParameterizedType(InputConverter.class, this.getClass());
    }

    /**
     * 通过 dto 更新 Domain 对象。
     *
     * @param domain updated domain
     */
    default void update(DOMAIN domain) {
        BeanUtils.updateProperties(this, domain);
    }
}

