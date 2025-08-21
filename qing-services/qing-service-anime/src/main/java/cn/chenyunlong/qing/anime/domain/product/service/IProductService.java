package cn.chenyunlong.qing.anime.domain.product.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.anime.domain.product.dto.creator.ProductCreator;
import cn.chenyunlong.qing.anime.domain.product.dto.query.ProductQuery;
import cn.chenyunlong.qing.anime.domain.product.dto.updater.ProductUpdater;
import cn.chenyunlong.qing.anime.domain.product.dto.vo.ProductVO;
import cn.chenyunlong.qing.domain.common.AggregateId;
import java.lang.Long;
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
    void validProduct(AggregateId id);

    /**
     * invalid
     */
    void invalidProduct(AggregateId id);

    /**
     * findById
     */
    ProductVO findById(AggregateId id);

    /**
     * findByPage
     */
    Page<ProductVO> findByPage(PageRequestWrapper<ProductQuery> query);
}
