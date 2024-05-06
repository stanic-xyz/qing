package cn.chenyunlong.qing.domain.anime.district.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.anime.district.dto.query.DistrictQuery;
import cn.chenyunlong.qing.domain.anime.district.dto.updater.DistrictUpdater;
import cn.chenyunlong.qing.domain.anime.district.dto.vo.DistrictVO;
import org.springframework.data.domain.Page;

public interface IDistrictService {

    /**
     * create
     */
    Long createDistrict(DistrictCreator creator);

    /**
     * update
     */
    void updateDistrict(Long id, DistrictUpdater updater);

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

    void deleteById(Long id);
}
