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

package cn.chenyunlong.qing.model.bilibili;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class BiliAnime {

    @Expose
    private String badge;
    @JsonProperty("badge_info")
    private BadgeInfo badgeInfo;
    @JsonProperty("badge_type")
    private Long badgeType;
    @Expose
    private String cover;
    @JsonProperty("index_show")
    private String indexShow;
    @JsonProperty("is_finish")
    private Long isFinish;
    @Expose
    private String link;
    @JsonProperty("media_id")
    private Long mediaId;
    @Expose
    private String order;
    @JsonProperty("order_type")
    private String orderType;
    @JsonProperty("season_id")
    private Long seasonId;
    @JsonProperty("season_type")
    private Long seasonType;
    @Expose
    private String title;
    @JsonProperty("title_icon")
    private String titleIcon;
}
