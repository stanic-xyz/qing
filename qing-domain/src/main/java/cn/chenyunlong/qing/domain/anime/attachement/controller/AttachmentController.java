package cn.chenyunlong.qing.domain.anime.attachement.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.anime.attachement.dto.query.AttachmentQuery;
import cn.chenyunlong.qing.domain.anime.attachement.dto.request.AttachmentCreateRequest;
import cn.chenyunlong.qing.domain.anime.attachement.dto.request.AttachmentQueryRequest;
import cn.chenyunlong.qing.domain.anime.attachement.dto.request.AttachmentUpdateRequest;
import cn.chenyunlong.qing.domain.anime.attachement.dto.response.AttachmentResponse;
import cn.chenyunlong.qing.domain.anime.attachement.dto.updater.AttachmentUpdater;
import cn.chenyunlong.qing.domain.anime.attachement.dto.vo.AttachmentVO;
import cn.chenyunlong.qing.domain.anime.attachement.mapper.AttachmentMapper;
import cn.chenyunlong.qing.domain.anime.attachement.service.IAttachmentService;
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

@Tag(name = "附件管理")
@RestController
@Slf4j
@RequestMapping("api/v1/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final IAttachmentService attachmentService;

    /**
     * createRequest
     */
    @Operation(summary = "新建附件")
    @PostMapping
    public JsonResult<Long> createAttachment(
        @RequestBody
        AttachmentCreateRequest request) {
        AttachmentCreator creator = AttachmentMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(attachmentService.createAttachment(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateAttachment")
    public JsonResult<String> updateAttachment(
        @RequestBody
        AttachmentUpdateRequest request) {
        AttachmentUpdater updater = AttachmentMapper.INSTANCE.request2Updater(request);
        attachmentService.updateAttachment(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validAttachment(
        @PathVariable
        Long id) {
        attachmentService.validAttachment(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidAttachment(
        @PathVariable
        Long id) {
        attachmentService.invalidAttachment(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<AttachmentResponse> findById(
        @PathVariable
        Long id) {
        AttachmentVO vo = attachmentService.findById(id);
        AttachmentResponse response = AttachmentMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<AttachmentResponse>> page(
        @RequestBody
        PageRequestWrapper<AttachmentQueryRequest> request) {
        PageRequestWrapper<AttachmentQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(AttachmentMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<AttachmentVO> page = attachmentService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(AttachmentMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
