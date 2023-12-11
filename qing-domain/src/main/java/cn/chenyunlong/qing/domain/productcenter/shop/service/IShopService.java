package cn.chenyunlong.qing.domain.productcenter.shop.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.creator.ShopCreator;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.query.ShopQuery;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.updater.ShopUpdater;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.vo.ShopVO;
import org.springframework.data.domain.Page;

public interface IShopService {
    /**
     * create
     */
    Long createShop(ShopCreator creator);

    /**
     * update
     */
    void updateShop(ShopUpdater updater);

    /**
     * valid
     */
    void validShop(Long id);

    /**
     * invalid
     */
    void invalidShop(Long id);

    /**
     * findById
     */
    ShopVO findById(Long id);

    /**
     * findByPage
     */
    Page<ShopVO> findByPage(PageRequestWrapper<ShopQuery> query);
}
