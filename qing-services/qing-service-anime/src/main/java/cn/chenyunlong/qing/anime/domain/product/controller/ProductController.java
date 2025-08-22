package cn.chenyunlong.qing.anime.domain.product.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.anime.domain.product.service.IProductService;
import cn.chenyunlong.qing.domain.common.AggregateId;

import java.lang.String;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validProduct(@PathVariable AggregateId id) {
        productService.validProduct(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidProduct(@PathVariable AggregateId id) {
        productService.invalidProduct(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }
}
