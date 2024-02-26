package cn.chenyunlong.qing.domain.anime.episode.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeCreateRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeQueryRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.response.EpisodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "episodeClient",
    path = "episode/v1"
)
public interface EpisodeFeignService {

    /**
     * 创建
     */
    @PostMapping("createEpisode")
    JsonResult<Long> createEpisode(@RequestBody EpisodeCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateEpisode")
    JsonResult<String> updateEpisode(@RequestBody EpisodeUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validEpisode(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidEpisode(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<EpisodeResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<EpisodeResponse>> page(
        @RequestBody PageRequestWrapper<EpisodeQueryRequest> request);
}
