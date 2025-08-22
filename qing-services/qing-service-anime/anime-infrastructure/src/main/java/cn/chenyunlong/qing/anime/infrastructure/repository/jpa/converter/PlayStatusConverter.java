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

package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.converter;

import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

/**
 * 播放状态转换器。
 *
 * @author 陈云龙
 */
@Converter
public class PlayStatusConverter implements AttributeConverter<PlayStatus, Integer> {

    /**
     * 转换为数据库列。
     *
     * @param playStatus 播放状态
     * @return {@link Integer}
     */
    @Override
    public Integer convertToDatabaseColumn(PlayStatus playStatus) {
        return playStatus == null ? null : playStatus.getValue();
    }

    /**
     * 转换为实体属性。
     *
     * @param code 代码
     * @return {@link PlayStatus}
     */
    @Override
    public PlayStatus convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(PlayStatus.values())
            .filter(status -> status.getValue().equals(code))
            .findFirst()
            .orElse(null);
    }
}