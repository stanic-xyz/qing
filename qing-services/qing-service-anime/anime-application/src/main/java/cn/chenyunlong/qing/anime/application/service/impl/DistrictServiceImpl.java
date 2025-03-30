package cn.chenyunlong.qing.anime.application.service.impl;

import cn.chenyunlong.qing.anime.application.service.IDistrictService;
import cn.chenyunlong.qing.anime.domain.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.anime.domain.district.dto.updater.DistrictUpdater;
import cn.chenyunlong.qing.anime.domain.district.dto.vo.DistrictVO;
import cn.chenyunlong.qing.anime.domain.district.repository.DistrictRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class DistrictServiceImpl implements IDistrictService {

    @Resource
    private final DistrictRepository districtRepository;

    /**
     * create
     */
    @Override
    public Long createDistrict(DistrictCreator creator) {
        return 0L;
    }

    /**
     * update
     */
    @Override
    public void updateDistrict(Long id, DistrictUpdater updater) {

    }

    @Override
    public void validDistrict(Long id) {

    }

    @Override
    public void invalidDistrict(Long id) {

    }

    /**
     * findById
     */
    @Override
    public DistrictVO findById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
