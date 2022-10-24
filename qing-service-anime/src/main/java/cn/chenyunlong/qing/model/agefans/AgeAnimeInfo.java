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

package cn.chenyunlong.qing.model.agefans;

import lombok.Data;

import java.util.List;

@Data
public class AgeAnimeInfo {
    private List<AgePlayList> agePlayListList;
    private List<AgeRecommendInfo> recommendList;
    private AgeBaiduNet baiduNet;
    private Long animeId;
    private String name;
    private String district;
    private String instruction;
    private String coverUrl;
    private String detailCoverUrl;
    private String type;
    private String originalName;
    private String otherName;
    private String author;
    private String company;
    private String premiereDate;
    private String playStatus;
    private String plotType;
    private String tags;
    private String officialWebsite;
    private String playHeat;
}