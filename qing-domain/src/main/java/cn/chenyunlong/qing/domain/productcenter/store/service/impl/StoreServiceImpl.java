package cn.chenyunlong.qing.domain.productcenter.store.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.store.Store;
import cn.chenyunlong.qing.domain.productcenter.store.dto.creator.StoreCreator;
import cn.chenyunlong.qing.domain.productcenter.store.dto.query.StoreQuery;
import cn.chenyunlong.qing.domain.productcenter.store.dto.updater.StoreUpdater;
import cn.chenyunlong.qing.domain.productcenter.store.dto.vo.StoreVO;
import cn.chenyunlong.qing.domain.productcenter.store.mapper.StoreMapper;
import cn.chenyunlong.qing.domain.productcenter.store.repository.StoreRepository;
import cn.chenyunlong.qing.domain.productcenter.store.service.IStoreService;
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
public class StoreServiceImpl implements IStoreService {

    private final StoreRepository storeRepository;

    /**
     * createImpl
     */
    @Override
    public Long createStore(StoreCreator creator) {
        Optional<Store> store = EntityOperations.doCreate(storeRepository)
            .create(() -> StoreMapper.INSTANCE.dtoToEntity(creator))
            .update(Store::init)
            .execute();
        return store.isPresent() ? store.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateStore(StoreUpdater updater) {
        EntityOperations.doUpdate(storeRepository)
            .loadById(updater.getId())
            .update(updater::updateStore)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validStore(Long id) {
        EntityOperations.doUpdate(storeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidStore(Long id) {
        EntityOperations.doUpdate(storeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public StoreVO findById(Long id) {
        Optional<Store> store = storeRepository.findById(id);
        return new StoreVO(store.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<StoreVO> findByPage(PageRequestWrapper<StoreQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return storeRepository.findAll(pageRequest).map(StoreVO::new);
    }
}
