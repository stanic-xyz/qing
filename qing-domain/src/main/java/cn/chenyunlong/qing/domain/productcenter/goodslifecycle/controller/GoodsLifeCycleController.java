package cn.chenyunlong.qing.domain.productcenter.goodslifecycle.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.creator.GoodsLifeCycleCreator;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.query.GoodsLifeCycleQuery;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.request.GoodsLifeCycleCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.request.GoodsLifeCycleQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.request.GoodsLifeCycleUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.response.GoodsLifeCycleResponse;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.updater.GoodsLifeCycleUpdater;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.vo.GoodsLifeCycleVO;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.mapper.GoodsLifeCycleMapper;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.service.IGoodsLifeCycleService;
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

@Tag(name = "商品生命周期管理")
@RestController
@Slf4j
@RequestMapping("api/v1/goods-life-cycle")
@RequiredArgsConstructor
public class GoodsLifeCycleController {

    private final IGoodsLifeCycleService goodsLifeCycleService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createGoodsLifeCycle(@RequestBody GoodsLifeCycleCreateRequest request) {
        GoodsLifeCycleCreator creator = GoodsLifeCycleMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(goodsLifeCycleService.createGoodsLifeCycle(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateGoodsLifeCycle")
    public JsonResult<String> updateGoodsLifeCycle(
        @RequestBody
        GoodsLifeCycleUpdateRequest request) {
        GoodsLifeCycleUpdater updater = GoodsLifeCycleMapper.INSTANCE.request2Updater(request);
        goodsLifeCycleService.updateGoodsLifeCycle(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validGoodsLifeCycle(@PathVariable Long id) {
        goodsLifeCycleService.validGoodsLifeCycle(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidGoodsLifeCycle(@PathVariable Long id) {
        goodsLifeCycleService.invalidGoodsLifeCycle(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<GoodsLifeCycleResponse> findById(@PathVariable Long id) {
        GoodsLifeCycleVO vo = goodsLifeCycleService.findById(id);
        GoodsLifeCycleResponse response = GoodsLifeCycleMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<GoodsLifeCycleResponse>> page(
        @RequestBody
        PageRequestWrapper<GoodsLifeCycleQueryRequest> request) {
        PageRequestWrapper<GoodsLifeCycleQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(GoodsLifeCycleMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<GoodsLifeCycleVO> page = goodsLifeCycleService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(GoodsLifeCycleMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
