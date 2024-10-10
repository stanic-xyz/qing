package cn.chenyunlong.qing.domain.productcenter.goods.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.goods.Goods;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.creator.GoodsCreator;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.query.GoodsQuery;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.updater.GoodsUpdater;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.vo.GoodsVO;
import cn.chenyunlong.qing.domain.productcenter.goods.mapper.GoodsMapper;
import cn.chenyunlong.qing.domain.productcenter.goods.repository.GoodsRepository;
import cn.chenyunlong.qing.domain.productcenter.goods.service.IGoodsService;
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
public class GoodsServiceImpl implements IGoodsService {

    private final GoodsRepository goodsRepository;

    /**
     * createImpl
     */
    @Override
    public Long createGoods(GoodsCreator creator) {
        Optional<Goods> goods = EntityOperations.doCreate(goodsRepository)
            .create(() -> GoodsMapper.INSTANCE.dtoToEntity(creator))
            .update(Goods::init)
            .execute();
        return goods.isPresent() ? goods.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateGoods(GoodsUpdater updater) {
        EntityOperations.doUpdate(goodsRepository)
            .loadById(updater.getId())
            .update(updater::updateGoods)
            .execute();
    }

    @Override
    public void validGoods(Long id) {
        EntityOperations.doUpdate(goodsRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    @Override
    public void invalidGoods(Long id) {
        EntityOperations.doUpdate(goodsRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public GoodsVO findById(Long id) {
        Optional<Goods> goods = goodsRepository.findById(id);
        return new GoodsVO(goods.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<GoodsVO> findByPage(PageRequestWrapper<GoodsQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return goodsRepository.findAll(pageRequest).map(GoodsVO::new);
    }
}
