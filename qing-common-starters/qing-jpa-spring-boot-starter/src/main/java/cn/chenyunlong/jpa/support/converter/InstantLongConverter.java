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

package cn.chenyunlong.jpa.support.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;

/**
 * 时间转换器
 *
 * @author Stan
 * @date 2022/10/24
 */
@Converter
public class InstantLongConverter implements AttributeConverter<Instant, Long> {

    /**
     * 转换为数据库列
     *
     * @param date 日期
     * @return {@link Long}
     */
    @Override
    public Long convertToDatabaseColumn(Instant date) {
        return date == null ? null : date.toEpochMilli();
    }

    /**
     * 转换为实体属性
     *
     * @param date 日期
     * @return {@link Instant}
     */
    @Override
    public Instant convertToEntityAttribute(Long date) {
        return date == null ? null : Instant.ofEpochMilli(date);
    }
}
