package cn.chenyunlong.codegen.example.domain.service.impl;

import cn.chenyunlong.codegen.example.domain.Product;
import cn.chenyunlong.codegen.example.domain.creator.ProductCreator;
import cn.chenyunlong.codegen.example.domain.mapper.ProductMapper;
import cn.chenyunlong.codegen.example.domain.query.ProductQuery;
import cn.chenyunlong.codegen.example.domain.repository.ProductRepository;
import cn.chenyunlong.codegen.example.domain.service.IProductService;
import cn.chenyunlong.codegen.example.domain.updater.ProductUpdater;
import cn.chenyunlong.codegen.example.domain.vo.ProductVO;
import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import java.lang.Long;
import java.lang.Override;
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
    public void validProduct(AggregateId id) {
        EntityOperations.doUpdate(productRepository)
        .loadById(id)
        .update(Product::valid)
        .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidProduct(AggregateId id) {
        EntityOperations.doUpdate(productRepository)
        .loadById(id)
        .update(Product::invalid)
        .execute();
    }

    /**
     * findById
     */
    @Override
    public ProductVO findById(AggregateId id) {
        Optional<Product> product =  productRepository.findById(id);
        return new ProductVO(product.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<ProductVO> findByPage(PageRequestWrapper<ProductQuery> query) {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return productRepository.findAll(pageRequest).map(ProductVO::new);
    }
}
