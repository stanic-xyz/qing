package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
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
import cn.chenyunlong.qing.security.util.SpringSecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "推荐管理")
@RestController
@Slf4j
@Validated
@RequestMapping("api/v1/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final IRecommendService recommendService;
    private final IAnimeService animeService;

    @PostMapping
    public JsonResult<Long> createRecommend(
        @Valid
        @RequestBody
        RecommendCreateRequest request) {
        RecommendCreator creator = RecommendMapper.INSTANCE.request2Dto(request);
        creator.setRecommendUserId(SpringSecurityUtils.getCurrentUsername());
        return JsonResult.success(recommendService.createRecommend(creator));
    }

    @PostMapping("updateRecommend")
    public JsonResult<String> updateRecommend(
        @RequestBody
        RecommendUpdateRequest request) {
        RecommendUpdater updater = RecommendMapper.INSTANCE.request2Updater(request);
        recommendService.updateRecommend(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validRecommend(
        @PathVariable("id")
        Long id) {
        recommendService.validRecommend(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidRecommend(
        @PathVariable("id")
        Long id) {
        recommendService.invalidRecommend(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @GetMapping("findById/{id}")
    public JsonResult<RecommendResponse> findById(
        @PathVariable("id")
        Long id) {
        RecommendVO recommendVO = recommendService.findById(id);
        return JsonResult.success(RecommendMapper.INSTANCE.vo2CustomResponse(recommendVO));
    }

    @PostMapping("page")
    public JsonResult<Page<RecommendResponse>> page(
        @RequestBody
        PageRequestWrapper<RecommendQueryRequest> request) {
        PageRequestWrapper<RecommendQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(RecommendMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<RecommendDetailVO> page = recommendService.findByPage(wrapper);
        return JsonResult.success(page.map(RecommendMapper.INSTANCE::vo2CustomResponse));
    }
}
