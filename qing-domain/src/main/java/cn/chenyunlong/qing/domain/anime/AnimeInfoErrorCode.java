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

package cn.chenyunlong.qing.domain.anime;

import cn.chenyunlong.common.constants.BaseEnum;
import java.util.Optional;

public enum AnimeInfoErrorCode implements BaseEnum<Integer> {

    ASSET_HAS_IN(10010026, "动漫已经添加"),
    ASSET_HAS_OUT(10010027, "资产已经删除"),
    ASSET_CODE_NOT_EXIST(10010028, "动漫编码不存在");

    private final Integer code;
    private final String name;

    AnimeInfoErrorCode(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<AnimeInfoErrorCode> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(code, AnimeInfoErrorCode.class));
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
