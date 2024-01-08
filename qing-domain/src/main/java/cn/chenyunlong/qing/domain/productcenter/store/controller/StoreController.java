package cn.chenyunlong.qing.domain.productcenter.store.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.store.dto.creator.StoreCreator;
import cn.chenyunlong.qing.domain.productcenter.store.dto.query.StoreQuery;
import cn.chenyunlong.qing.domain.productcenter.store.dto.request.StoreCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.store.dto.request.StoreQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.store.dto.request.StoreUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.store.dto.response.StoreResponse;
import cn.chenyunlong.qing.domain.productcenter.store.dto.updater.StoreUpdater;
import cn.chenyunlong.qing.domain.productcenter.store.dto.vo.StoreVO;
import cn.chenyunlong.qing.domain.productcenter.store.mapper.StoreMapper;
import cn.chenyunlong.qing.domain.productcenter.store.service.IStoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "仓库管理")
@RestController
@Slf4j
@RequestMapping("api/v1/store")
@RequiredArgsConstructor
public class StoreController {
    private final IStoreService storeService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createStore(@RequestBody StoreCreateRequest request) {
        StoreCreator creator = StoreMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(storeService.createStore(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateStore")
    public JsonResult<String> updateStore(@RequestBody StoreUpdateRequest request) {
        StoreUpdater updater = StoreMapper.INSTANCE.request2Updater(request);
        storeService.updateStore(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validStore(@PathVariable Long id) {
        storeService.validStore(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidStore(@PathVariable Long id) {
        storeService.invalidStore(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<StoreResponse> findById(@PathVariable Long id) {
        StoreVO vo = storeService.findById(id);
        StoreResponse response = StoreMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<StoreResponse>> page(
            @RequestBody PageRequestWrapper<StoreQueryRequest> request) {
        PageRequestWrapper<StoreQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(StoreMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<StoreVO> page = storeService.findByPage(wrapper);
        return JsonResult.success(
                PageResult.of(
                        page.getContent().stream()
                                .map(StoreMapper.INSTANCE::vo2CustomResponse)
                                .collect(Collectors.toList()),
                        page.getTotalElements(),
                        page.getSize(),
                        page.getNumber())
        );
    }
}
