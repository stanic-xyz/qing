package cn.chenyunlong.qing.domain.productcenter.goodslifecycle.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.creator.GoodsLifeCycleCreator;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.query.GoodsLifeCycleQuery;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.updater.GoodsLifeCycleUpdater;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.vo.GoodsLifeCycleVO;
import org.springframework.data.domain.Page;

public interface IGoodsLifeCycleService {
    /**
     * create
     */
    Long createGoodsLifeCycle(GoodsLifeCycleCreator creator);

    /**
     * update
     */
    void updateGoodsLifeCycle(GoodsLifeCycleUpdater updater);

    /**
     * valid
     */
    void validGoodsLifeCycle(Long id);

    /**
     * invalid
     */
    void invalidGoodsLifeCycle(Long id);

    /**
     * findById
     */
    GoodsLifeCycleVO findById(Long id);

    /**
     * findByPage
     */
    Page<GoodsLifeCycleVO> findByPage(PageRequestWrapper<GoodsLifeCycleQuery> query);
}
