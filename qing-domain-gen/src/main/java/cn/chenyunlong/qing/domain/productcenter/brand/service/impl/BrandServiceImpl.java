package cn.chenyunlong.qing.domain.productcenter.brand.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.brand.Brand;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.creator.BrandCreator;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.query.BrandQuery;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.updater.BrandUpdater;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.vo.BrandVO;
import cn.chenyunlong.qing.domain.productcenter.brand.mapper.BrandMapper;
import cn.chenyunlong.qing.domain.productcenter.brand.repository.BrandRepository;
import cn.chenyunlong.qing.domain.productcenter.brand.service.IBrandService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class BrandServiceImpl implements IBrandService {

    private final BrandRepository brandRepository;

    /**
     * createImpl
     */
    @Override
    public Long createBrand(BrandCreator creator) {
        Optional<Brand> brand = EntityOperations.doCreate(brandRepository)
            .create(() -> BrandMapper.INSTANCE.dtoToEntity(creator))
            .update(Brand::init)
            .execute();
        return brand.isPresent() ? brand.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateBrand(BrandUpdater updater) {
        EntityOperations.doUpdate(brandRepository)
            .loadById(updater.getId())
            .update(updater::updateBrand)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validBrand(Long id) {
        EntityOperations.doUpdate(brandRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidBrand(Long id) {
        EntityOperations.doUpdate(brandRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public BrandVO findById(Long id) {
        Optional<Brand> brand = brandRepository.findById(id);
        return new BrandVO(brand.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<BrandVO> findByPage(PageRequestWrapper<BrandQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return brandRepository.findAll(pageRequest).map(BrandVO::new);
    }
}
