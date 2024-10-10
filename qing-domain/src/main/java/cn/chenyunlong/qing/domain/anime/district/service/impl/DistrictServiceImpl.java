package cn.chenyunlong.qing.domain.anime.district.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.anime.district.dto.query.DistrictQuery;
import cn.chenyunlong.qing.domain.anime.district.dto.updater.DistrictUpdater;
import cn.chenyunlong.qing.domain.anime.district.dto.vo.DistrictVO;
import cn.chenyunlong.qing.domain.anime.district.mapper.DistrictConverter;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
import cn.chenyunlong.qing.domain.anime.district.service.IDistrictService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class DistrictServiceImpl implements IDistrictService {

    @Resource
    private final DistrictRepository districtRepository;

    /**
     * createImpl
     */
    @Override
    public Long createDistrict(DistrictCreator creator) {
        Optional<District> district = EntityOperations.doCreate(districtRepository)
            .create(() -> DistrictConverter.INSTANCE.dtoToEntity(creator))
            .update(District::init)
            .execute();
        return district.isPresent() ? district.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateDistrict(Long id, DistrictUpdater updater) {
        EntityOperations.doUpdate(districtRepository)
            .loadById(id)
            .update(updater::updateDistrict)
            .execute();
    }

    @Override
    public void validDistrict(Long id) {
        EntityOperations.doUpdate(districtRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    @Override
    public void invalidDistrict(Long id) {
        EntityOperations.doUpdate(districtRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public DistrictVO findById(Long id) {
        Optional<District> district = districtRepository.findById(id);
        return district.map(DistrictConverter.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    /**
     * findByPage
     */
    @Override
    public Page<DistrictVO> findByPage(PageRequestWrapper<DistrictQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return districtRepository.findAll(pageRequest).map(DistrictConverter.INSTANCE::entityToVo);
    }

    @Override
    public void deleteById(Long id) {
        districtRepository.deleteById(id);
    }
}
