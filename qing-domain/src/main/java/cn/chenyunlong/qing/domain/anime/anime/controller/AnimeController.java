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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "AnimeController", description = "番剧管理")
@RestController
@Slf4j
@RequestMapping("api/v1/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final IAnimeService animeService;

    @Operation(summary = "创建动漫信息")
    @PostMapping
    public JsonResult<Long> createAnime(
        @RequestBody
        AnimeCreateRequest request) {
        AnimeCreator creator = AnimeMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(animeService.createAnime(creator));
    }

    @Operation(summary = "更新动漫信息")
    @PostMapping("updateAnime")
    public JsonResult<String> updateAnime(
        @RequestBody
        AnimeUpdateRequest request) {
        AnimeUpdater updater = AnimeMapper.INSTANCE.request2Updater(request);
        animeService.updateAnime(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @Operation(summary = "根据id查询动漫信息")
    @GetMapping("findById/{id}")
    public JsonResult<AnimeResponse> findById(
        @PathVariable
        Long id) {
        AnimeVO vo = animeService.findById(id);
        AnimeResponse response = AnimeMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @Operation(summary = "分页查询动漫信息")
    @PostMapping("page")
    public JsonResult<PageResult<AnimeResponse>> page(
        @RequestBody
        PageRequestWrapper<AnimeQueryRequest> request) {
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
