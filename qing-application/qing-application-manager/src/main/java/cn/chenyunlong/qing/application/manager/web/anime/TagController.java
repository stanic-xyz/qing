package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.TagQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.TagCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.TagQueryRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.TagUpdateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.response.TagResponse;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.TagUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.TagVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.TagMapper;
import cn.chenyunlong.qing.domain.anime.anime.service.ITagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "标签管理")
@RestController
@Slf4j
@RequestMapping("api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @PostMapping
    public JsonResult<Long> createTag(
        @RequestBody
        TagCreateRequest request) {
        TagCreator creator = TagMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(tagService.createTag(creator));
    }

    @PostMapping("updateTag")
    public JsonResult<String> updateTag(
        @RequestBody
        TagUpdateRequest request) {
        TagUpdater updater = TagMapper.INSTANCE.request2Updater(request);
        tagService.updateTag(updater);
        return JsonResult.success(CodeEnum.Success.getName());
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

    @GetMapping("findById/{id}")
    public JsonResult<TagResponse> findById(
        @PathVariable("id")
        Long id) {
        TagVO vo = tagService.findById(id);
        TagResponse response = TagMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<Page<TagResponse>> page(
        @RequestBody
        PageRequestWrapper<TagQueryRequest> request) {
        PageRequestWrapper<TagQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(TagMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<TagVO> page = tagService.findByPage(wrapper);
        return JsonResult.success(page.map(TagMapper.INSTANCE::vo2CustomResponse));
    }
}
