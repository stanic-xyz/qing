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

package cn.chenyunlong.qing.anime.infrastructure.converter.base;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * DateMapper
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public class DateMapper {

    /**
     * 时间转时间戳
     */
    public Long asLong(Instant date) {
        if (Objects.nonNull(date)) {
            return date.toEpochMilli();
        }
        return null;
    }

    /**
     * 时间戳转时间
     */
    public Instant asInstant(Long date) {
        if (Objects.nonNull(date)) {
            return Instant.ofEpochMilli(date);
        }
        return null;
    }

    public Instant map(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        ZoneId zoneId = ZoneOffset.systemDefault();
        // todo 这里似乎不应该这样子搞
        ZoneOffset zoneOffset = ZoneOffset.ofHours(0);
        return value.toInstant(zoneOffset);
    }
}
