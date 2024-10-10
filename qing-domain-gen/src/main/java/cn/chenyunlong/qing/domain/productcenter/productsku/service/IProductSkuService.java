package cn.chenyunlong.qing.domain.productcenter.productsku.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.creator.ProductSkuCreator;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.query.ProductSkuQuery;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.updater.ProductSkuUpdater;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.vo.ProductSkuVO;
import org.springframework.data.domain.Page;

public interface IProductSkuService {

    /**
     * create
     */
    Long createProductSku(ProductSkuCreator creator);

    /**
     * update
     */
    void updateProductSku(ProductSkuUpdater updater);

    void validProductSku(Long id);

    void invalidProductSku(Long id);

    /**
     * findById
     */
    ProductSkuVO findById(Long id);

    /**
     * findByPage
     */
    Page<ProductSkuVO> findByPage(PageRequestWrapper<ProductSkuQuery> query);
}
