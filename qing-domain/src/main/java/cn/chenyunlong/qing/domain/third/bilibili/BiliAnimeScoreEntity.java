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

package cn.chenyunlong.qing.domain.third.bilibili;

import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 哔哩哔哩动漫评分系统
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bili_anime_score")
@Data
public class BiliAnimeScoreEntity extends BaseJpaAggregate {

    /**
     * 动漫ID
     */
    private Long animeId;

    /**
     * 评分信息
     */
    private Double score;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;
}
