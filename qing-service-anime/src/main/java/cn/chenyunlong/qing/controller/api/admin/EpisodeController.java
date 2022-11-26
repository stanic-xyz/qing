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

import cn.chenyunlong.qing.domain.anime.service.AnimeEpisodeService;
import cn.chenyunlong.qing.infrastructure.model.ApiResult;
import cn.chenyunlong.qing.infrastructure.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.qing.infrastructure.model.params.AddEpisodeParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分集控制器
 *
 * @author Stan
 * @date 2022/11/05
 */
@Tag(name = "分集")
@RestController
@RequestMapping("api/episodes")
@RequiredArgsConstructor
public class EpisodeController {

    private final AnimeEpisodeService episodeService;

    @PostMapping("")
    public ApiResult<AnimeEpisodeDTO> add(@Validated @RequestBody AddEpisodeParam episodeParam) {
        return ApiResult.success(episodeService.add(episodeParam));
    }
}
