/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.qing.web.web.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum ReleaseStatus {

    GENERAL_AVAILABILITY,
    MILESTONE,
    SNAPSHOT;

    @JsonCreator
    public static ReleaseStatus fromName(String name) {
        return Arrays
            .stream(ReleaseStatus.values())
            .filter(type -> type.name().equals(name))
            .findFirst()
            .orElse(ReleaseStatus.GENERAL_AVAILABILITY);
    }

}
