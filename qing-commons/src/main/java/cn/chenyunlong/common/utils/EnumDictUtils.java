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

package cn.chenyunlong.common.utils;

import cn.chenyunlong.common.constants.BaseEnum;
import cn.chenyunlong.common.model.EnumVo;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gim
 */
public class EnumDictUtils {

    private EnumDictUtils() {
    }

    public static <T extends Enum<T> & BaseEnum<T>> List<EnumVo> getEnumDictVo(Class<T> cls) {
        return EnumSet.allOf(cls).stream().map(et -> {
                    EnumVo vo = new EnumVo();
                    vo.setCode(et.getCode());
                    vo.setName(et.getName());
                    vo.setText(et.name());
                    return vo;
                }
        ).collect(
                Collectors.toList());
    }
}
