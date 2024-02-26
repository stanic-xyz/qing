package cn.chenyunlong.qing.domain.anime.recommend.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.recommend.dto.creator.RecommendCreator;
import cn.chenyunlong.qing.domain.anime.recommend.dto.query.RecommendQuery;
import cn.chenyunlong.qing.domain.anime.recommend.dto.request.RecommendCreateRequest;
import cn.chenyunlong.qing.domain.anime.recommend.dto.request.RecommendQueryRequest;
import cn.chenyunlong.qing.domain.anime.recommend.dto.request.RecommendUpdateRequest;
import cn.chenyunlong.qing.domain.anime.recommend.dto.response.RecommendResponse;
import cn.chenyunlong.qing.domain.anime.recommend.dto.updater.RecommendUpdater;
import cn.chenyunlong.qing.domain.anime.recommend.dto.vo.RecommendDetailVO;
import cn.chenyunlong.qing.domain.anime.recommend.dto.vo.RecommendVO;
import cn.chenyunlong.qing.domain.anime.recommend.mapper.RecommendMapper;
import cn.chenyunlong.qing.domain.anime.recommend.service.IRecommendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "推荐管理")
@RestController
@Slf4j
@Validated
@RequestMapping("api/v1/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final IRecommendService recommendService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createRecommend(@Valid @RequestBody RecommendCreateRequest request) {
        RecommendCreator creator = RecommendMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(recommendService.createRecommend(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateRecommend")
    public JsonResult<String> updateRecommend(@RequestBody RecommendUpdateRequest request) {
        RecommendUpdater updater = RecommendMapper.INSTANCE.request2Updater(request);
        recommendService.updateRecommend(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validRecommend(@PathVariable Long id) {
        recommendService.validRecommend(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidRecommend(@PathVariable Long id) {
        recommendService.invalidRecommend(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<RecommendResponse> findById(@PathVariable Long id) {
        RecommendVO vo = recommendService.findById(id);
        RecommendResponse response = RecommendMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<RecommendResponse>> page(
        @RequestBody
        PageRequestWrapper<RecommendQueryRequest> request) {
        PageRequestWrapper<RecommendQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(RecommendMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<RecommendDetailVO> page = recommendService.findByPage(wrapper);
        List<RecommendDetailVO> recommendVOList = page.getContent();
        return JsonResult.success(
            PageResult.of(
                recommendVOList.stream()
                    .map(RecommendMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
