package cn.chenyunlong.qing.domain.productcenter.productsku.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.productsku.ProductSku;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.creator.ProductSkuCreator;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.query.ProductSkuQuery;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.updater.ProductSkuUpdater;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.vo.ProductSkuVO;
import cn.chenyunlong.qing.domain.productcenter.productsku.mapper.ProductSkuMapper;
import cn.chenyunlong.qing.domain.productcenter.productsku.repository.ProductSkuRepository;
import cn.chenyunlong.qing.domain.productcenter.productsku.service.IProductSkuService;
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
public class ProductSkuServiceImpl implements IProductSkuService {
    private final ProductSkuRepository productSkuRepository;

    /**
     * createImpl
     */
    @Override
    public Long createProductSku(ProductSkuCreator creator) {
        Optional<ProductSku> productSku = EntityOperations.doCreate(productSkuRepository)
                .create(() -> ProductSkuMapper.INSTANCE.dtoToEntity(creator))
                .update(ProductSku::init)
                .execute();
        return productSku.isPresent() ? productSku.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateProductSku(ProductSkuUpdater updater) {
        EntityOperations.doUpdate(productSkuRepository)
                .loadById(updater.getId())
                .update(updater::updateProductSku)
                .execute();
    }

    /**
     * valid
     */
    @Override
    public void validProductSku(Long id) {
        EntityOperations.doUpdate(productSkuRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidProductSku(Long id) {
        EntityOperations.doUpdate(productSkuRepository)
                .loadById(id)
                .update(BaseJpaAggregate::invalid)
                .execute();
    }

    /**
     * findById
     */
    @Override
    public ProductSkuVO findById(Long id) {
        Optional<ProductSku> productSku = productSkuRepository.findById(id);
        return new ProductSkuVO(productSku.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<ProductSkuVO> findByPage(PageRequestWrapper<ProductSkuQuery> query) {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return productSkuRepository.findAll(pageRequest).map(ProductSkuVO::new);
    }
}
