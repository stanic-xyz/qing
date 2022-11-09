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
import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.response.AnimeInfoMinimalDTO;
import cn.chenyunlong.qing.model.params.AnimeInfoParam;
import cn.chenyunlong.qing.model.params.AnimeInfoQuery;
import cn.chenyunlong.qing.model.vo.anime.AnimeInfoVo;
import cn.chenyunlong.qing.service.AnimeInfoService;
import cn.hutool.core.thread.NamedThreadFactory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Stan
 */
@Tag(name = "番剧")
@RestController
@RequestMapping("api/anime")
@RequiredArgsConstructor
public class AnimeApiController {

    private final AnimeInfoService animeInfoService;

    @PostMapping
    public ApiResult<AnimeInfoVo> addAnime(@Valid @RequestBody AnimeInfoParam animeInfo) {
        AnimeInfoVo animeInfoVo = animeInfoService.create(animeInfo.convertTo());
        return ApiResult.success(animeInfoVo);
    }

    @GetMapping("detail/{aid}")
    public ApiResult<AnimeInfo> movie(@PathVariable(value = "aid") Long animeId) {
        //可以修改查看数据了
        return ApiResult.success(animeInfoService.getById(animeId));
    }

    @PutMapping("{animeId:\\d+}")
    public ApiResult<AnimeInfoVo> updateBy(@PathVariable("animeId") Integer animeId,
                                           @Valid @RequestBody AnimeInfoParam animeInfoParam) {
        AnimeInfo animeInfoToUpdate = animeInfoService.getById(animeId);
        animeInfoParam.update(animeInfoToUpdate);
        return ApiResult.success(animeInfoService.updateBy(animeInfoToUpdate));
    }

    @GetMapping("listAnime")
    public ApiResult<IPage<AnimeInfoVo>> listAnime(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                                   AnimeInfoQuery animeInfo) {
        IPage<AnimeInfo> animeInfoPage = new Page<>(page, pageSize);
        IPage<AnimeInfoVo> animeInfos = animeInfoService.listByPage(animeInfoPage, animeInfo);
        return ApiResult.success(animeInfos);
    }

    @GetMapping("update/page")
    public ApiResult<IPage<AnimeInfoMinimalDTO>> getUpdateInfo(@RequestParam(defaultValue = "1") Integer page,
                                                               @RequestParam(defaultValue = "24") Integer pageSize) {
        IPage<AnimeInfoMinimalDTO> animeInfoPage = animeInfoService.getUpdateAnimeInfo(page, pageSize);
        return ApiResult.success(animeInfoPage);
    }

    @DeleteMapping("deleteAnime")
    public ApiResult<Void> deleteAnime(@RequestParam("aid") Long animeId) {
        animeInfoService.deleteAnime(animeId);
        return ApiResult.success();
    }


    @PostMapping("img/downloadImages")
    public ApiResult<Void> downloadImages() throws IOException {
        ExecutorService executor = new ScheduledThreadPoolExecutor(2, new NamedThreadFactory("zhangli-", false));
        executor.execute(() -> {
            try {
                animeInfoService.downloadImages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return ApiResult.success();
    }
}
