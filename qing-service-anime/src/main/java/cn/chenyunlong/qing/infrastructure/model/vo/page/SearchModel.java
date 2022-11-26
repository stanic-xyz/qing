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

package cn.chenyunlong.qing.infrastructure.model.vo.page;

import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.infrastructure.model.params.AnimeInfoQuery;
import cn.chenyunlong.qing.infrastructure.model.vo.OptionsModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchModel extends BaseModel {
    private AnimeInfoQuery query;
    private List<AnimeInfo> animeInfos;
    private OptionsModel options;
    private Long totalCount;
    private Long currentIndex;
    private Long totalPage;
}
