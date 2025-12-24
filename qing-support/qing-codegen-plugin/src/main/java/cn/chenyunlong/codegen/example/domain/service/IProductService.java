package cn.chenyunlong.codegen.example.domain.service;

import cn.chenyunlong.codegen.example.domain.creator.ProductCreator;
import cn.chenyunlong.codegen.example.domain.query.ProductQuery;
import cn.chenyunlong.codegen.example.domain.updater.ProductUpdater;
import cn.chenyunlong.codegen.example.domain.vo.ProductVO;
import cn.chenyunlong.common.model.PageRequestWrapper;
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
