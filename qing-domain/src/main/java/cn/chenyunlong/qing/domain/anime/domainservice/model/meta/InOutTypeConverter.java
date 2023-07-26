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

package cn.chenyunlong.qing.domain.anime.domainservice.model.meta;


import jakarta.persistence.AttributeConverter;

public class InOutTypeConverter implements AttributeConverter<InOutType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(InOutType inOutType) {
        return inOutType.getValue();
    }

    @Override
    public InOutType convertToEntityAttribute(Integer code) {
        return InOutType.of(code).orElse(null);
    }
}
