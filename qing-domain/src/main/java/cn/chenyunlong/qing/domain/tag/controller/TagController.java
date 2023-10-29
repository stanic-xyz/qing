package cn.chenyunlong.qing.domain.tag.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.tag.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.tag.dto.query.TagQuery;
import cn.chenyunlong.qing.domain.tag.dto.request.TagCreateRequest;
import cn.chenyunlong.qing.domain.tag.dto.request.TagQueryRequest;
import cn.chenyunlong.qing.domain.tag.dto.request.TagUpdateRequest;
import cn.chenyunlong.qing.domain.tag.dto.response.TagResponse;
import cn.chenyunlong.qing.domain.tag.dto.updater.TagUpdater;
import cn.chenyunlong.qing.domain.tag.dto.vo.TagVO;
import cn.chenyunlong.qing.domain.tag.mapper.TagMapper;
import cn.chenyunlong.qing.domain.tag.service.ITagService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/v1/tag")
@RequiredArgsConstructor
public class TagController {
    private final ITagService tagService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createTag(@RequestBody TagCreateRequest request) {
        TagCreator creator = TagMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(tagService.createTag(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateTag")
    public JsonResult<String> updateTag(@RequestBody TagUpdateRequest request) {
        TagUpdater updater = TagMapper.INSTANCE.request2Updater(request);
        tagService.updateTag(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validTag(@PathVariable Long id) {
        tagService.validTag(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidTag(@PathVariable Long id) {
        tagService.invalidTag(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<TagResponse> findById(@PathVariable Long id) {
        TagVO vo = tagService.findById(id);
        TagResponse response = TagMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<TagResponse>> page(
        @RequestBody PageRequestWrapper<TagQueryRequest> request) {
        PageRequestWrapper<TagQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(TagMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<TagVO> page = tagService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(TagMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
