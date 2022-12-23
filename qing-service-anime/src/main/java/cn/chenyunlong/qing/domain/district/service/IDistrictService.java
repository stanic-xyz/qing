// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.district.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.district.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.district.query.DistrictQuery;
import cn.chenyunlong.qing.domain.district.updater.DistrictUpdater;
import cn.chenyunlong.qing.domain.district.vo.DistrictVO;
import org.springframework.data.domain.Page;

public interface IDistrictService {
    /**
     * create
     */
    Long createDistrict(DistrictCreator creator);

    /**
     * update
     */
    void updateDistrict(DistrictUpdater updater);

    /**
     * valid
     */
    void validDistrict(Long id);

    /**
     * invalid
     */
    void invalidDistrict(Long id);

    /**
     * findById
     */
    DistrictVO findById(Long id);

    /**
     * findByPage
     */
    Page<DistrictVO> findByPage(PageRequestWrapper<DistrictQuery> query);
}