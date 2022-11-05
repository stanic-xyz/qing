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
import cn.chenyunlong.qing.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.qing.model.params.AddEpisodeParam;
import cn.chenyunlong.qing.service.AnimeEpisodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "api/anime/episode")
@RestController
public class EpisodeController {

    private final AnimeEpisodeService episodeService;

    public EpisodeController(AnimeEpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @PostMapping("")
    public ApiResult<AnimeEpisodeDTO> add(@Validated @RequestBody AddEpisodeParam episodeParam) {
        return ApiResult.success(episodeService.add(episodeParam));
    }
}
