package cn.chenyunlong.qing.domain.productcenter.shop.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.creator.ShopCreator;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.query.ShopQuery;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.request.ShopCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.request.ShopQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.request.ShopUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.response.ShopResponse;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.updater.ShopUpdater;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.vo.ShopVO;
import cn.chenyunlong.qing.domain.productcenter.shop.mapper.ShopMapper;
import cn.chenyunlong.qing.domain.productcenter.shop.service.IShopService;
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

@Tag(name = "店铺管理")
@RestController
@Slf4j
@RequestMapping("api/v1/shop")
@RequiredArgsConstructor
public class ShopController {

    private final IShopService shopService;

    @PostMapping
    public JsonResult<Long> createShop(
        @RequestBody
        ShopCreateRequest request) {
        ShopCreator creator = ShopMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(shopService.createShop(creator));
    }

    @PostMapping("updateShop")
    public JsonResult<String> updateShop(@RequestBody ShopUpdateRequest request) {
        ShopUpdater updater = ShopMapper.INSTANCE.request2Updater(request);
        shopService.updateShop(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validShop(
        @PathVariable
        Long id) {
        shopService.validShop(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidShop(
        @PathVariable
        Long id) {
        shopService.invalidShop(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<ShopResponse> findById(@PathVariable Long id) {
        ShopVO vo = shopService.findById(id);
        ShopResponse response = ShopMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<PageResult<ShopResponse>> page(
        @RequestBody
        PageRequestWrapper<ShopQueryRequest> request) {
        PageRequestWrapper<ShopQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(ShopMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<ShopVO> page = shopService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(ShopMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
