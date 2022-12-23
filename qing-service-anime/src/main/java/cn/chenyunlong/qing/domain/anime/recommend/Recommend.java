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

package cn.chenyunlong.qing.domain.anime.recommend;

import cn.chenyunlong.qing.infrastructure.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Stan
 */
@Data
@Entity
@Table(name = "anime_recommend")
public class Recommend extends BaseEntity {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 动漫ID
     */
    private Long aid;

    /**
     * 推荐理由
     */
    private String reason;

    /**
     * 推荐时间
     */
    private LocalDateTime date;

}
