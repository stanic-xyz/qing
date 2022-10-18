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
import lombok.Data;

/**
 * 番剧播放资源(AnimeRecommend)表实体类
 *
 * @author makejava
 * @since 2022-05-22 11:34:04
 */
@Data
public class AnimeRecommend extends BaseEntity<AnimeRecommend> {

    private Long id;
    //番剧ID
    private Long aid;
    //推荐理由
    private String reason;
    //视频排序
    private Integer orderNo;
}

