package cn.chenyunlong.qing.domain.productcenter.product.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.product.dto.creator.ProductCreator;
import cn.chenyunlong.qing.domain.productcenter.product.dto.query.ProductQuery;
import cn.chenyunlong.qing.domain.productcenter.product.dto.updater.ProductUpdater;
import cn.chenyunlong.qing.domain.productcenter.product.dto.vo.ProductVO;
import org.springframework.data.domain.Page;

public interface IProductService {
    /**
     * create
     */
    Long createProduct(ProductCreator creator);

    /**
     * update
     */
    void updateProduct(ProductUpdater updater);

    /**
     * valid
     */
    void validProduct(Long id);

    /**
     * invalid
     */
    void invalidProduct(Long id);

    /**
     * findById
     */
    ProductVO findById(Long id);

    /**
     * findByPage
     */
    Page<ProductVO> findByPage(PageRequestWrapper<ProductQuery> query);
}
