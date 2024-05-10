package cn.chenyunlong.qing.domain.productcenter.goodslifecycle.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.GoodsLifeCycle;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.creator.GoodsLifeCycleCreator;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.query.GoodsLifeCycleQuery;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.updater.GoodsLifeCycleUpdater;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.vo.GoodsLifeCycleVO;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.mapper.GoodsLifeCycleMapper;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.repository.GoodsLifeCycleRepository;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.service.IGoodsLifeCycleService;
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
public class GoodsLifeCycleServiceImpl implements IGoodsLifeCycleService {

    private final GoodsLifeCycleRepository goodsLifeCycleRepository;

    /**
     * createImpl
     */
    @Override
    public Long createGoodsLifeCycle(GoodsLifeCycleCreator creator) {
        Optional<GoodsLifeCycle> goodsLifeCycle =
            EntityOperations.doCreate(goodsLifeCycleRepository)
                .create(() -> GoodsLifeCycleMapper.INSTANCE.dtoToEntity(creator))
                .update(GoodsLifeCycle::init)
                .execute();
        return goodsLifeCycle.isPresent() ? goodsLifeCycle.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateGoodsLifeCycle(GoodsLifeCycleUpdater updater) {
        EntityOperations.doUpdate(goodsLifeCycleRepository)
            .loadById(updater.getId())
            .update(updater::updateGoodsLifeCycle)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validGoodsLifeCycle(Long id) {
        EntityOperations.doUpdate(goodsLifeCycleRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidGoodsLifeCycle(Long id) {
        EntityOperations.doUpdate(goodsLifeCycleRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public GoodsLifeCycleVO findById(Long id) {
        Optional<GoodsLifeCycle> goodsLifeCycle = goodsLifeCycleRepository.findById(id);
        return new GoodsLifeCycleVO(
            goodsLifeCycle.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<GoodsLifeCycleVO> findByPage(PageRequestWrapper<GoodsLifeCycleQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return goodsLifeCycleRepository.findAll(pageRequest).map(GoodsLifeCycleVO::new);
    }
}
