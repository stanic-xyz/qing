package cn.chenyunlong.qing.domain.anime.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.domain.anime.dto.request.AnimeQueryRequest;
import cn.chenyunlong.qing.domain.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.dto.response.AnimeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "animeClient",
    path = "anime/v1"
)
public interface AnimeFeignService {
    /**
     * 创建
     */
    @PostMapping("createAnime")
    JsonResult<Long> createAnime(@RequestBody AnimeCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateAnime")
    JsonResult<String> updateAnime(@RequestBody AnimeUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validAnime(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidAnime(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<AnimeResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<AnimeResponse>> page(
        @RequestBody PageRequestWrapper<AnimeQueryRequest> request);
}
