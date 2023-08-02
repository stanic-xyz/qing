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

package cn.chenyunlong.common.mapper;

import cn.chenyunlong.common.constants.ValidStatus;
import java.util.Objects;

public class GenericEnumMapper {

    public Integer asInteger(ValidStatus status) {
        return status.getValue();
    }

    public ValidStatus asValidStatus(Integer code) {
        for (ValidStatus status : ValidStatus.values()) {
            if (Objects.equals(status.getValue(), code)) {
                return status;
            }
        }
        return ValidStatus.INVALID;
    }
}
