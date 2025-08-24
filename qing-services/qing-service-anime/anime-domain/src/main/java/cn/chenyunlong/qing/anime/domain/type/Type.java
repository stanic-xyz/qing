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

package cn.chenyunlong.qing.anime.domain.type;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.Getter;
import lombok.Setter;

/**
 * 类型信息
 *
 * @author 陈云龙
 */

@Getter
@Setter
public class Type extends BaseAggregate<TypeId> {

    @FieldDesc(description = "名称")
    private String name;

    @FieldDesc(description = "介绍")
    private String instruction;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setName(String name) {
        this.name = name;
    }
}
