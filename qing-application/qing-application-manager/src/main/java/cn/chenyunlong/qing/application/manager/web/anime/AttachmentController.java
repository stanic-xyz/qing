package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "附件管理")
@RestController
@Slf4j
@RequestMapping("api/v1/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final IAttachmentService attachmentService;

    @Operation(summary = "新建附件")
    @PostMapping
    public JsonResult<Long> createAttachment(
        @RequestBody
        AttachmentCreateRequest request) {
        AttachmentCreator creator = AttachmentMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(attachmentService.createAttachment(creator));
    }

    @PostMapping("{id}")
    public JsonResult<String> updateAttachment(
        @PathVariable("id")
        Long id,
        @RequestBody
        AttachmentUpdateRequest request) {
        AttachmentUpdater updater = AttachmentMapper.INSTANCE.request2Updater(request);
        attachmentService.updateAttachment(id, updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @GetMapping("{id}")
    public JsonResult<AttachmentResponse> findById(
        @PathVariable("id")
        Long id) {
        AttachmentVO vo = attachmentService.findById(id);
        AttachmentResponse response = AttachmentMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @DeleteMapping("{id}")
    public JsonResult<Void> deleteById(
        @PathVariable("id")
        Long id) {
        attachmentService.deleteById(id);
        return JsonResult.success();
    }

    @PostMapping("upload")
    public JsonResult<Void> upload(
        @RequestParam
        MultipartFile file) {
        long size = file.getSize();
        log.info("上传的文件大小：{}", size);
        return JsonResult.success();
    }

    @PostMapping("page")
    public JsonResult<Page<AttachmentResponse>> page(
        @RequestBody
        PageRequestWrapper<AttachmentQueryRequest> request) {
        PageRequestWrapper<AttachmentQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(AttachmentMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<AttachmentVO> page = attachmentService.findByPage(wrapper);
        return JsonResult.success(page.map(AttachmentMapper.INSTANCE::vo2CustomResponse));
    }
}
