package cn.chenyunlong.qing.domain.anime.anime.controller;


import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeQueryRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.response.AnimeResponse;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeMapper;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("api/v1/anime")
@RequiredArgsConstructor
public class AnimeController {
    private final IAnimeService animeService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createAnime(@RequestBody AnimeCreateRequest request) {
        AnimeCreator creator = AnimeMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(animeService.createAnime(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateAnime")
    public JsonResult<String> updateAnime(@RequestBody AnimeUpdateRequest request) {
        AnimeUpdater updater = AnimeMapper.INSTANCE.request2Updater(request);
        animeService.updateAnime(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validAnime(@PathVariable Long id) {
        animeService.validAnime(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidAnime(@PathVariable Long id) {
        animeService.invalidAnime(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<AnimeResponse> findById(@PathVariable Long id) {
        AnimeVO vo = animeService.findById(id);
        AnimeResponse response = AnimeMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<AnimeResponse>> page(
        @RequestBody PageRequestWrapper<AnimeQueryRequest> request) {
        PageRequestWrapper<AnimeQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(AnimeMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<AnimeVO> page = animeService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(AnimeMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
