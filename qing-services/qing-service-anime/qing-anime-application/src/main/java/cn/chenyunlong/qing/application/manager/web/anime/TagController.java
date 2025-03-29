package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.application.manager.web.anime.query.AnimeQueryService;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.TagCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.mapper.TagMapper;
import cn.chenyunlong.qing.domain.anime.anime.service.ITagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "标签管理")
@RestController
@Slf4j
@RequestMapping("api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;
    private final AnimeQueryService animeQueryService;

    @PostMapping
    public JsonResult<Long> createTag(
        @RequestBody
        TagCreateRequest request) {
        TagCreator creator = TagMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(tagService.createTag(creator));
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validTag(
        @PathVariable("id")
        Long id) {
        tagService.validTag(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidTag(
        @PathVariable("id")
        Long id) {
        tagService.invalidTag(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }
}
