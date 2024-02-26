package cn.chenyunlong.qing.domain.anime.recommend.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.recommend.dto.request.RecommendCreateRequest;
import cn.chenyunlong.qing.domain.anime.recommend.dto.request.RecommendQueryRequest;
import cn.chenyunlong.qing.domain.anime.recommend.dto.request.RecommendUpdateRequest;
import cn.chenyunlong.qing.domain.anime.recommend.dto.response.RecommendResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "anime",
    contextId = "recommendClient",
    path = "recommend/v1"
)
public interface RecommendFeignService {

    /**
     * 创建
     */
    @PostMapping("createRecommend")
    JsonResult<Long> createRecommend(@RequestBody RecommendCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateRecommend")
    JsonResult<String> updateRecommend(@RequestBody RecommendUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validRecommend(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidRecommend(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<RecommendResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<RecommendResponse>> page(
        @RequestBody
        PageRequestWrapper<RecommendQueryRequest> request);
}
