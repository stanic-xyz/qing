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

import cn.chenyunlong.jpa.support.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @TableName bilibili_anime_score
 */
@EqualsAndHashCode(callSuper = true)
@Table(name = "bilibili_anime_score")
@Data
public class BilibiliAnimeScoreEntity extends BaseEntity {
    /**
     * 动漫ID
     */
    private Long animeId;

    /**
     *
     */
    private Double score;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;
}
