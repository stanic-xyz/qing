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

package cn.chenyunlong.jpa.support.converter;


import cn.chenyunlong.common.constants.ValidStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;

/**
 * 校验状态转换器。
 *
 * @author Stan
 * @since 2022/10/24
 */
@Converter
public class ValidStatusConverter implements AttributeConverter<ValidStatus, Integer> {

    /**
     * 转换为数据库列。
     *
     * @param validStatus 有效状态
     * @return {@link Integer}
     */
    @Override
    public Integer convertToDatabaseColumn(ValidStatus validStatus) {
        return validStatus.getValue();
    }

    /**
     * 转换为实体属性。
     *
     * @param code 代码
     * @return {@link ValidStatus}
     */
    @Override
    public ValidStatus convertToEntityAttribute(Integer code) {
        return Arrays.stream(ValidStatus.values())
            .filter(validStatus -> validStatus.getValue().equals(code))
            .findFirst()
            .orElse(null);
    }
}
