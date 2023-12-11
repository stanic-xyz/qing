package cn.chenyunlong.qing.domain.productcenter.store.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.store.dto.creator.StoreCreator;
import cn.chenyunlong.qing.domain.productcenter.store.dto.query.StoreQuery;
import cn.chenyunlong.qing.domain.productcenter.store.dto.updater.StoreUpdater;
import cn.chenyunlong.qing.domain.productcenter.store.dto.vo.StoreVO;
import org.springframework.data.domain.Page;

public interface IStoreService {
    /**
     * create
     */
    Long createStore(StoreCreator creator);

    /**
     * update
     */
    void updateStore(StoreUpdater updater);

    /**
     * valid
     */
    void validStore(Long id);

    /**
     * invalid
     */
    void invalidStore(Long id);

    /**
     * findById
     */
    StoreVO findById(Long id);

    /**
     * findByPage
     */
    Page<StoreVO> findByPage(PageRequestWrapper<StoreQuery> query);
}
