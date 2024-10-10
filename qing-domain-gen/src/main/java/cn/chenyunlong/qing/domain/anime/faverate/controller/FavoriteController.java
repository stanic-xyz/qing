package cn.chenyunlong.qing.domain.anime.faverate.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.faverate.dto.creator.FavoriteCreator;
import cn.chenyunlong.qing.domain.anime.faverate.dto.query.FavoriteQuery;
import cn.chenyunlong.qing.domain.anime.faverate.dto.request.FavoriteCreateRequest;
import cn.chenyunlong.qing.domain.anime.faverate.dto.request.FavoriteQueryRequest;
import cn.chenyunlong.qing.domain.anime.faverate.dto.request.FavoriteUpdateRequest;
import cn.chenyunlong.qing.domain.anime.faverate.dto.response.FavoriteResponse;
import cn.chenyunlong.qing.domain.anime.faverate.dto.updater.FavoriteUpdater;
import cn.chenyunlong.qing.domain.anime.faverate.dto.vo.FavoriteVO;
import cn.chenyunlong.qing.domain.anime.faverate.mapper.FavoriteMapper;
import cn.chenyunlong.qing.domain.anime.faverate.service.IFavoriteService;

import java.lang.Long;
import java.lang.String;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final IFavoriteService favoriteService;

    @PostMapping
    public JsonResult<Long> createFavorite(
        @RequestBody
        FavoriteCreateRequest request) {
        FavoriteCreator creator = FavoriteMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(favoriteService.createFavorite(creator));
    }

    @PostMapping("updateFavorite")
    public JsonResult<String> updateFavorite(@RequestBody FavoriteUpdateRequest request) {
        FavoriteUpdater updater = FavoriteMapper.INSTANCE.request2Updater(request);
        favoriteService.updateFavorite(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validFavorite(
        @PathVariable
        Long id) {
        favoriteService.validFavorite(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidFavorite(
        @PathVariable
        Long id) {
        favoriteService.invalidFavorite(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<FavoriteResponse> findById(@PathVariable Long id) {
        FavoriteVO vo = favoriteService.findById(id);
        FavoriteResponse response = FavoriteMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<PageResult<FavoriteResponse>> page(
        @RequestBody
        PageRequestWrapper<FavoriteQueryRequest> request) {
        PageRequestWrapper<FavoriteQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(FavoriteMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<FavoriteVO> page = favoriteService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(FavoriteMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
