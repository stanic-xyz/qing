package cn.chenyunlong.qing.application.manager.web.anime;


import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.application.manager.web.anime.query.AnimeQueryService;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateCommand;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.response.AnimeResponse;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeDetailVO;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeMapper;
import cn.chenyunlong.qing.domain.anime.anime.models.Anime;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import cn.chenyunlong.qing.security.util.SpringSecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "番剧管理", description = "番剧管理")
@RestController
@Slf4j
@Validated
@RequestMapping("api/v1/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final IAnimeService animeService;
    private final AnimeQueryService animeQueryService;

    @Operation(summary = "创建动漫信息")
    @PostMapping
    public JsonResult<Long> createAnime(
        @Validated
        @RequestBody
        AnimeCreateCommand request) {
        String currentUsername = SpringSecurityUtils.getCurrentUsername();
        AnimeCreator creator = AnimeMapper.INSTANCE.requestToCreator(request);
        creator.setOperateUserId(currentUsername);
        Anime anime = animeService.createAnime(creator);
        return JsonResult.success(anime.getAggregateId().getId());
    }

    @Operation(summary = "更新动漫信息")
    @PostMapping("updateAnime")
    public JsonResult<String> updateAnime(
        @RequestBody
        AnimeUpdateRequest request) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AnimeUpdater updater = AnimeMapper.INSTANCE.request2Updater(request);
        animeService.updateAnime(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @Operation(summary = "根据id移除动漫信息")
    @DeleteMapping("removeById/{id}")
    public JsonResult<Void> removeById(
        @PathVariable("id")
        Long id) {
        animeService.removeById(id);
        return JsonResult.success();
    }

    @GetMapping("findById/{id}")
    public JsonResult<AnimeResponse> findById(
        @PathVariable("id")
        Long id) {
        AnimeVO vo = animeService.findById(id);
        AnimeResponse response = AnimeMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }


    @GetMapping("findDetailById/{id}")
    public JsonResult<AnimeDetailVO> findDetailById(
        @PathVariable("id")
        Long id) {
        AnimeDetailVO detailVO = animeQueryService.findDetailById(id);
        return JsonResult.success(detailVO);
    }

}
