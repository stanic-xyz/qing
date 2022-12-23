// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.anime.episode.service;

import cn.chenyunlong.common.model.JsonObject;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.episode.request.EpisodeCreateRequest;
import cn.chenyunlong.qing.domain.anime.episode.request.EpisodeQueryRequest;
import cn.chenyunlong.qing.domain.anime.episode.request.EpisodeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.episode.response.EpisodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "xxxSrv",
        contextId = "episodeClient",
        path = "episode/v1"
)
public interface EpisodeFeignService {
    /**
     * createRequest
     */
    @PostMapping("createEpisode")
    JsonObject<Long> createEpisode(@RequestBody EpisodeCreateRequest request);

    /**
     * update request
     */
    @PostMapping("updateEpisode")
    JsonObject<String> updateEpisode(@RequestBody EpisodeUpdateRequest request);

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    JsonObject<String> validEpisode(@PathVariable("id") Long id);

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    JsonObject<String> invalidEpisode(@PathVariable("id") Long id);

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    JsonObject<EpisodeResponse> findById(@PathVariable("id") Long id);

    /**
     * findByPage request
     */
    @PostMapping("findByPage")
    JsonObject<PageResult<EpisodeResponse>> findByPage(
            @RequestBody PageRequestWrapper<EpisodeQueryRequest> request);
}