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

package cn.chenyunlong.qing.model.entities.anime;

import cn.chenyunlong.qing.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * anime_episode
 *
 * @author Stan
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("anime_episode")
public class AnimeEpisodeEntity extends BaseEntity<AnimeEpisodeEntity> {
    /**
     * 视频ID
     */
    private Long id;

    private Long animeId;

    private Long listId;

    /**
     * 视频标题名称
     */
    private String name;

    /**
     * 视频状态，0正常
     */
    private Integer status;

    /**
     * 上传者名称
     */
    private String uploaderName;

    /**
     * 上传用户ID
     */
    private Long uploaderId;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 视频排序
     */
    private Integer orderNo = 0;
}
