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

import cn.chenyunlong.qing.domain.anime.AnimeRecommendEntity;
import cn.chenyunlong.qing.domain.anime.service.AnimeRecommendService;
import cn.chenyunlong.qing.infrastructure.model.ApiResult;
import cn.chenyunlong.qing.infrastructure.model.params.AnimeInfoQuery;
import cn.chenyunlong.qing.infrastructure.model.params.AnimeRecommendParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "推荐")
@RestController
@RequestMapping("api/recommend")
@RequiredArgsConstructor
public class AnimeRecommendController {

    private final AnimeRecommendService recommendService;

    @Operation(summary = "Gets animeType detail by id", description = "描述信息")
    @GetMapping("{recommendId:\\d+}")
    public ApiResult<AnimeRecommendEntity> getAnimeTypeInfo(@PathVariable("recommendId") Long recommendId) {
        return ApiResult.success(recommendService.getById(recommendId));
    }

    @GetMapping("page")
    public ApiResult<IPage<AnimeRecommendEntity>> pageBy(AnimeRecommendParam typeParam, Pageable pageable) {
        return ApiResult.success(recommendService.pageBy(typeParam, pageable));
    }

    @GetMapping()
    public ApiResult<List<AnimeRecommendEntity>> getAnimeInfoService(AnimeInfoQuery typeParam, Pageable pageable) {
        return ApiResult.success(recommendService.getRecommendAnimeInfoList(pageable, typeParam));
    }

    @PostMapping()
    public ApiResult<AnimeRecommendEntity> addAnimeType(@Valid @RequestBody AnimeRecommendParam recommendParam) {
        AnimeRecommendEntity animeType = recommendService.addRecommend(recommendParam.convertTo());
        return ApiResult.success(animeType);
    }


    @PutMapping("{typeId:\\d+}")
    public ApiResult<AnimeRecommendEntity> modifyAnimeType(@PathVariable("typeId") Long typeId,
                                                           @Valid @RequestBody AnimeRecommendParam typeParam) {
        //查询动漫信息
        AnimeRecommendEntity animeRecommendEntity = recommendService.getById(typeId);
        //更新类型信息
        typeParam.update(animeRecommendEntity);

        return ApiResult.success(recommendService.update(animeRecommendEntity));
    }
}
