package cn.chenyunlong.qing.domain.productcenter.product.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.product.Product;
import cn.chenyunlong.qing.domain.productcenter.product.dto.creator.ProductCreator;
import cn.chenyunlong.qing.domain.productcenter.product.dto.query.ProductQuery;
import cn.chenyunlong.qing.domain.productcenter.product.dto.updater.ProductUpdater;
import cn.chenyunlong.qing.domain.productcenter.product.dto.vo.ProductVO;
import cn.chenyunlong.qing.domain.productcenter.product.mapper.ProductMapper;
import cn.chenyunlong.qing.domain.productcenter.product.repository.ProductRepository;
import cn.chenyunlong.qing.domain.productcenter.product.service.IProductService;
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
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    /**
     * createImpl
     */
    @Override
    public Long createProduct(ProductCreator creator) {
        Optional<Product> product = EntityOperations.doCreate(productRepository)
            .create(() -> ProductMapper.INSTANCE.dtoToEntity(creator))
            .update(Product::init)
            .execute();
        return product.isPresent() ? product.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateProduct(ProductUpdater updater) {
        EntityOperations.doUpdate(productRepository)
            .loadById(updater.getId())
            .update(updater::updateProduct)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validProduct(Long id) {
        EntityOperations.doUpdate(productRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidProduct(Long id) {
        EntityOperations.doUpdate(productRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public ProductVO findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return new ProductVO(
            product.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<ProductVO> findByPage(PageRequestWrapper<ProductQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return productRepository.findAll(pageRequest).map(ProductVO::new);
    }
}
