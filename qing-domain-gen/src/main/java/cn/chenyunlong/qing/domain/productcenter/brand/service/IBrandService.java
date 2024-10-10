package cn.chenyunlong.qing.domain.productcenter.brand.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.creator.BrandCreator;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.query.BrandQuery;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.updater.BrandUpdater;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.vo.BrandVO;
import org.springframework.data.domain.Page;

public interface IBrandService {

    /**
     * 录入品牌
     */
    Long createBrand(BrandCreator creator);

    /**
     * 更新品牌信息
     */
    void updateBrand(BrandUpdater updater);

    void validBrand(Long id);

    void invalidBrand(Long id);

    /**
     * findById
     */
    BrandVO findById(Long id);

    /**
     * findByPage
     */
    Page<BrandVO> findByPage(PageRequestWrapper<BrandQuery> query);
}
