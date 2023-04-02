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

package cn.chenyunlong.qing.domain.system.link;

import cn.chenyunlong.jpa.support.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 陈云龙
 * @date 2021/02/27
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "link")
@Entity
public class Link extends BaseEntity {
    /**
     * Link name.
     */
    private String name;

    /**
     * Link website address.
     */
    private String url;

    /**
     * Website logo.
     */
    private String logo;

    /**
     * Website description.
     */
    private String description;

    /**
     * Link team name.
     */
    private String team;

    /**
     * Sort.
     */
    private Integer priority;
}
