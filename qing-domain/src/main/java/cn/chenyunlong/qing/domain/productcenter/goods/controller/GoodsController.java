package cn.chenyunlong.qing.domain.productcenter.goods.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.creator.GoodsCreator;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.query.GoodsQuery;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.request.GoodsCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.request.GoodsQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.request.GoodsUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.response.GoodsResponse;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.updater.GoodsUpdater;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.vo.GoodsVO;
import cn.chenyunlong.qing.domain.productcenter.goods.mapper.GoodsMapper;
import cn.chenyunlong.qing.domain.productcenter.goods.service.IGoodsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "商品管理", description = "商品管理接口")
@RestController
@Slf4j
@RequestMapping("api/v1/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final IGoodsService goodsService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createGoods(@RequestBody GoodsCreateRequest request) {
        GoodsCreator creator = GoodsMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(goodsService.createGoods(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateGoods")
    public JsonResult<String> updateGoods(@RequestBody GoodsUpdateRequest request) {
        GoodsUpdater updater = GoodsMapper.INSTANCE.request2Updater(request);
        goodsService.updateGoods(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validGoods(@PathVariable Long id) {
        goodsService.validGoods(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidGoods(@PathVariable Long id) {
        goodsService.invalidGoods(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<GoodsResponse> findById(@PathVariable Long id) {
        GoodsVO vo = goodsService.findById(id);
        GoodsResponse response = GoodsMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<GoodsResponse>> page(
            @RequestBody PageRequestWrapper<GoodsQueryRequest> request) {
        PageRequestWrapper<GoodsQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(GoodsMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<GoodsVO> page = goodsService.findByPage(wrapper);
        return JsonResult.success(
                PageResult.of(
                        page.getContent().stream()
                                .map(GoodsMapper.INSTANCE::vo2CustomResponse)
                                .collect(Collectors.toList()),
                        page.getTotalElements(),
                        page.getSize(),
                        page.getNumber())
        );
    }
}
