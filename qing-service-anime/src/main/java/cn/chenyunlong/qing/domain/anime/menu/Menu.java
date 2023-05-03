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

package cn.chenyunlong.qing.domain.anime.menu;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "menu")
public class Menu extends BaseEntity {

    @FieldDesc(name = "菜单名称")
    private String name;

    @FieldDesc(name = "菜单路径")
    private String path;

    @FieldDesc(name = "父级菜单名称")
    private String parentId;

    @FieldDesc(name = "完整的路径", description = "完整的路径，通过:进行分隔，便于查询")
    private String qualifyPath;
}
