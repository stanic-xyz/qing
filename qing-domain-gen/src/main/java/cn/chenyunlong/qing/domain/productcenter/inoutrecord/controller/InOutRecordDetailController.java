package cn.chenyunlong.qing.domain.productcenter.inoutrecord.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.creator.InOutRecordDetailCreator;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.query.InOutRecordDetailQuery;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordDetailCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordDetailQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordDetailUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.response.InOutRecordDetailResponse;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater.InOutRecordDetailUpdater;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo.InOutRecordDetailVO;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.mapper.InOutRecordDetailMapper;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.service.IInOutRecordDetailService;
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

@Tag(name = "出入库记录详情")
@RestController
@Slf4j
@RequestMapping("api/v1/in-out-record-detail")
@RequiredArgsConstructor
public class InOutRecordDetailController {

    private final IInOutRecordDetailService inOutRecordDetailService;

    @PostMapping
    public JsonResult<Long> createInOutRecordDetail(
        @RequestBody
        InOutRecordDetailCreateRequest request) {
        InOutRecordDetailCreator creator = InOutRecordDetailMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(inOutRecordDetailService.createInOutRecordDetail(creator));
    }

    @PostMapping("updateInOutRecordDetail")
    public JsonResult<String> updateInOutRecordDetail(
            @RequestBody
            InOutRecordDetailUpdateRequest request) {
        InOutRecordDetailUpdater updater =
                InOutRecordDetailMapper.INSTANCE.request2Updater(request);
        inOutRecordDetailService.updateInOutRecordDetail(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validInOutRecordDetail(
        @PathVariable
        Long id) {
        inOutRecordDetailService.validInOutRecordDetail(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidInOutRecordDetail(
        @PathVariable
        Long id) {
        inOutRecordDetailService.invalidInOutRecordDetail(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<InOutRecordDetailResponse> findById(@PathVariable Long id) {
        InOutRecordDetailVO vo = inOutRecordDetailService.findById(id);
        InOutRecordDetailResponse response = InOutRecordDetailMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<PageResult<InOutRecordDetailResponse>> page(
        @RequestBody
        PageRequestWrapper<InOutRecordDetailQueryRequest> request) {
        PageRequestWrapper<InOutRecordDetailQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(InOutRecordDetailMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<InOutRecordDetailVO> page = inOutRecordDetailService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(InOutRecordDetailMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
