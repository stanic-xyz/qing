package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.qing.anime.domain.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.anime.domain.district.dto.updater.DistrictUpdater;
import cn.chenyunlong.qing.anime.domain.district.dto.vo.DistrictVO;

public interface IDistrictService {

    /**
     * create
     */
    Long createDistrict(DistrictCreator creator);

    /**
     * update
     */
    void updateDistrict(Long id, DistrictUpdater updater);

    void validDistrict(Long id);

    void invalidDistrict(Long id);

    /**
     * findById
     */
    DistrictVO findById(Long id);


    void deleteById(Long id);
}
