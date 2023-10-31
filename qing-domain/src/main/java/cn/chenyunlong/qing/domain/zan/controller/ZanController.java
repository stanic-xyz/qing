package cn.chenyunlong.qing.domain.zan.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.zan.dto.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.dto.query.ZanQuery;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanQueryRequest;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanUpdateRequest;
import cn.chenyunlong.qing.domain.zan.dto.response.ZanResponse;
import cn.chenyunlong.qing.domain.zan.dto.updater.ZanUpdater;
import cn.chenyunlong.qing.domain.zan.dto.vo.ZanVO;
import cn.chenyunlong.qing.domain.zan.mapper.ZanMapper;
import cn.chenyunlong.qing.domain.zan.service.IZanService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/v1/zan")
@RequiredArgsConstructor
public class ZanController {
    private final IZanService zanService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createZan(@RequestBody ZanCreateRequest request) {
        ZanCreator creator = ZanMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(zanService.createZan(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateZan")
    public JsonResult<String> updateZan(@RequestBody ZanUpdateRequest request) {
        ZanUpdater updater = ZanMapper.INSTANCE.request2Updater(request);
        zanService.updateZan(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validZan(@PathVariable Long id) {
        zanService.validZan(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidZan(@PathVariable Long id) {
        zanService.invalidZan(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<ZanResponse> findById(@PathVariable Long id) {
        ZanVO vo = zanService.findById(id);
        ZanResponse response = ZanMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<ZanResponse>> page(
        @RequestBody PageRequestWrapper<ZanQueryRequest> request) {
        PageRequestWrapper<ZanQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(ZanMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<ZanVO> page = zanService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(ZanMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
