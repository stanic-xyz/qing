package cn.chenyunlong.qing.domain.anime.anime.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCategoryCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCategoryQueryRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCategoryUpdateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.response.AnimeCategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "animeCategoryClient",
    path = "animeCategory/v1"
)
public interface AnimeCategoryFeignService {
    /**
     * 创建
     */
    @PostMapping("createAnimeCategory")
    JsonResult<Long> createAnimeCategory(@RequestBody AnimeCategoryCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateAnimeCategory")
    JsonResult<String> updateAnimeCategory(@RequestBody AnimeCategoryUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validAnimeCategory(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidAnimeCategory(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<AnimeCategoryResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<AnimeCategoryResponse>> page(
        @RequestBody PageRequestWrapper<AnimeCategoryQueryRequest> request);
}
