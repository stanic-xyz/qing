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

package cn.chenyunlong.qing.controller.api.admin;

import cn.chenyunlong.qing.core.ApiResult;
import cn.chenyunlong.qing.model.entities.anime.AnimeInfo;
import cn.chenyunlong.qing.model.params.AnimeInfoQuery;
import cn.chenyunlong.qing.service.AnimeRecommendService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Stan
 */
@Validated
@Api(tags = "api/anime/update")
@RestController
public class AnimeUpdateController {
    private final AnimeRecommendService animeRecommendService;

    public AnimeUpdateController(AnimeRecommendService animeRecommendService) {
        this.animeRecommendService = animeRecommendService;
    }


    @GetMapping("list")
    public ApiResult<List<AnimeInfo>> getRecommendAnimeInfoList(AnimeInfoQuery animeInfoQuery) {
        Pageable pageable = PageRequest.of(1, 20);
        animeRecommendService.getRecommendAnimeInfoList(pageable, animeInfoQuery);
        return ApiResult.success();
    }
}
