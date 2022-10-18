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

package cn.chenyunlong.qing.model.entities.bilibili;

import cn.chenyunlong.qing.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @TableName bilibili_anime_score
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bilibili_anime_score")
@Data
public class BilibiliAnimeScoreEntity extends BaseEntity<BilibiliAnimeScoreEntity> {
    /**
     * 主键ID
     */
    private Long id;
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
