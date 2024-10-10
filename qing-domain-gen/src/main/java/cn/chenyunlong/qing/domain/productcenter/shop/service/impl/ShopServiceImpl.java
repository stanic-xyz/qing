package cn.chenyunlong.qing.domain.productcenter.shop.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.shop.Shop;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.creator.ShopCreator;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.query.ShopQuery;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.updater.ShopUpdater;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.vo.ShopVO;
import cn.chenyunlong.qing.domain.productcenter.shop.mapper.ShopMapper;
import cn.chenyunlong.qing.domain.productcenter.shop.repository.ShopRepository;
import cn.chenyunlong.qing.domain.productcenter.shop.service.IShopService;
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
public class ShopServiceImpl implements IShopService {

    private final ShopRepository shopRepository;

    /**
     * createImpl
     */
    @Override
    public Long createShop(ShopCreator creator) {
        Optional<Shop> shop = EntityOperations.doCreate(shopRepository)
            .create(() -> ShopMapper.INSTANCE.dtoToEntity(creator))
            .update(Shop::init)
            .execute();
        return shop.isPresent() ? shop.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateShop(ShopUpdater updater) {
        EntityOperations.doUpdate(shopRepository)
            .loadById(updater.getId())
            .update(updater::updateShop)
            .execute();
    }

    @Override
    public void validShop(Long id) {
        EntityOperations.doUpdate(shopRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    @Override
    public void invalidShop(Long id) {
        EntityOperations.doUpdate(shopRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public ShopVO findById(Long id) {
        Optional<Shop> shop = shopRepository.findById(id);
        return new ShopVO(shop.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<ShopVO> findByPage(PageRequestWrapper<ShopQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return shopRepository.findAll(pageRequest).map(ShopVO::new);
    }
}
