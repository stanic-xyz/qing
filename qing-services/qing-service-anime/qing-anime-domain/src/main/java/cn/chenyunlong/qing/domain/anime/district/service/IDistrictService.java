package cn.chenyunlong.qing.domain.anime.district.service;

import cn.chenyunlong.qing.domain.anime.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.anime.district.dto.updater.DistrictUpdater;
import cn.chenyunlong.qing.domain.anime.district.dto.vo.DistrictVO;

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
