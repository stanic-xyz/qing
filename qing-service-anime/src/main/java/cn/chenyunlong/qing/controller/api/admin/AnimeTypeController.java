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
import cn.chenyunlong.qing.model.entities.AnimeType;
import cn.chenyunlong.qing.model.params.AnimeTypeParam;
import cn.chenyunlong.qing.service.AnimeTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/types")
public class AnimeTypeController {

    private final AnimeTypeService typeService;

    public AnimeTypeController(AnimeTypeService typeService) {
        this.typeService = typeService;
    }

    @ApiOperation("Gets animeType detail by id")
    @GetMapping("{typeId:\\d+}")
    public ApiResult<AnimeType> getAnimeTypeInfo(@PathVariable("typeId") Long typeId) {
        return ApiResult.success(typeService.getById(typeId));
    }

    @GetMapping("page")
    public ApiResult<IPage<AnimeType>> pageBy(AnimeTypeParam typeParam, Pageable pageable) {
        return ApiResult.success(typeService.pageBy(typeParam, pageable));
    }

    @GetMapping()
    public ApiResult<List<AnimeType>> getAnimeInfoService() {
        return ApiResult.success(typeService.getAllTypeInfo());
    }

    @PostMapping()
    public ApiResult<AnimeType> addAnimeType(@Valid @RequestBody AnimeTypeParam typeParam) {
        AnimeType animeType = typeService.addAnimeType(typeParam.convertTo());
        return ApiResult.success(animeType);
    }


    @PutMapping("{typeId:\\d+")
    public ApiResult<AnimeType> modifyAnimeType(@PathVariable("typeId") Long typeId,
                                                @Valid @RequestBody AnimeTypeParam typeParam) {
        //查询动漫信息
        AnimeType typeInfo = typeService.getById(typeId);

        //更新类型信息
        typeParam.update(typeInfo);

        return ApiResult.success(typeService.update(typeInfo));
    }
}