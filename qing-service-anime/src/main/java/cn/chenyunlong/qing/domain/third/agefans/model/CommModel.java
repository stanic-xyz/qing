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

package cn.chenyunlong.qing.domain.third.agefans.model;

import cn.chenyunlong.qing.domain.anime.AnimeMenu;
import lombok.Data;

import java.util.List;

/**
 * @author Stan
 */
@Data
public class CommModel {
    private String index;
    private String indexName;
    private String indexImg;
    private List<AnimeMenu> animeMenus;
    private String notice;
    private List<FriendLink> friendLinks;
}