package cn.chenyunlong.qing.domain.productcenter.goods.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.creator.GoodsCreator;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.query.GoodsQuery;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.updater.GoodsUpdater;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.vo.GoodsVO;
import org.springframework.data.domain.Page;

public interface IGoodsService {

    /**
     * create
     */
    Long createGoods(GoodsCreator creator);

    /**
     * update
     */
    void updateGoods(GoodsUpdater updater);

    /**
     * valid
     */
    void validGoods(Long id);

    /**
     * invalid
     */
    void invalidGoods(Long id);

    /**
     * findById
     */
    GoodsVO findById(Long id);

    /**
     * findByPage
     */
    Page<GoodsVO> findByPage(PageRequestWrapper<GoodsQuery> query);
}
