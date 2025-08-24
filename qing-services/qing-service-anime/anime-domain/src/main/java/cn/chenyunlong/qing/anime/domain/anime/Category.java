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

package cn.chenyunlong.qing.anime.domain.anime;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.anime.domain.anime.models.CategoryId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.Getter;
import lombok.Setter;

/**
 * 动漫信息。
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class Category extends BaseAggregate<CategoryId> {

    public static final long ROOT_PID = -1L;

    @FieldDesc(description = "名称")
    private String name;

    @FieldDesc(description = "父级id")
    private Long pid;

    @FieldDesc(description = "排序号")
    private Integer orderNo;

    public static Category create(String name, Integer orderNo, Category parent) {
        Category category = new Category();
        category.setPid(parent != null ? parent.getId().getValue() : Long.valueOf(ROOT_PID));
        category.setName(name);
        category.setOrderNo(orderNo);
        return category;
    }
}
