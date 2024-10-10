package cn.chenyunlong.qing.domain.productcenter.inoutrecord.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.creator.InOutRecordCreator;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.query.InOutRecordQuery;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.response.InOutRecordResponse;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater.InOutRecordUpdater;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo.InOutRecordVO;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.mapper.InOutRecordMapper;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.service.IInOutRecordService;
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

@Tag(name = "出入库记录")
@RestController
@Slf4j
@RequestMapping("api/v1/in-out-record")
@RequiredArgsConstructor
public class InOutRecordController {

    private final IInOutRecordService inOutRecordService;

    @PostMapping
    public JsonResult<Long> createInOutRecord(
        @RequestBody
        InOutRecordCreateRequest request) {
        InOutRecordCreator creator = InOutRecordMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(inOutRecordService.createInOutRecord(creator));
    }

    @PostMapping("updateInOutRecord")
    public JsonResult<String> updateInOutRecord(@RequestBody InOutRecordUpdateRequest request) {
        InOutRecordUpdater updater = InOutRecordMapper.INSTANCE.request2Updater(request);
        inOutRecordService.updateInOutRecord(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validInOutRecord(
        @PathVariable
        Long id) {
        inOutRecordService.validInOutRecord(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidInOutRecord(
        @PathVariable
        Long id) {
        inOutRecordService.invalidInOutRecord(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<InOutRecordResponse> findById(@PathVariable Long id) {
        InOutRecordVO vo = inOutRecordService.findById(id);
        InOutRecordResponse response = InOutRecordMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<PageResult<InOutRecordResponse>> page(
        @RequestBody
        PageRequestWrapper<InOutRecordQueryRequest> request) {
        PageRequestWrapper<InOutRecordQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(InOutRecordMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<InOutRecordVO> page = inOutRecordService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(InOutRecordMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
