package cn.chenyunlong.qing.domain.entity.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.entity.dto.creator.EntityCreator;
import cn.chenyunlong.qing.domain.entity.dto.query.EntityQuery;
import cn.chenyunlong.qing.domain.entity.dto.updater.EntityUpdater;
import cn.chenyunlong.qing.domain.entity.dto.vo.EntityVO;
import cn.chenyunlong.qing.domain.entity.mapper.EntityMapper;
import cn.chenyunlong.qing.domain.entity.repository.EntityRepository;
import cn.chenyunlong.qing.domain.entity.service.IEntityService;
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
public class EntityServiceImpl implements IEntityService {

    private final EntityRepository entityRepository;

    /**
     * createImpl
     */
    @Override
    public Long createEntity(EntityCreator creator) {
        Optional<Entity> entity = EntityOperations.doCreate(entityRepository)
            .create(() -> EntityMapper.INSTANCE.dtoToEntity(creator))
            .update(Entity::init)
            .execute();
        return entity.isPresent() ? entity.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateEntity(EntityUpdater updater) {
        EntityOperations.doUpdate(entityRepository)
            .loadById(updater.getId())
            .update(updater::updateEntity)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validEntity(Long id) {
        EntityOperations.doUpdate(entityRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidEntity(Long id) {
        EntityOperations.doUpdate(entityRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public EntityVO findById(Long id) {
        Optional<Entity> entity = entityRepository.findById(id);
        return new EntityVO(entity.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<EntityVO> findByPage(PageRequestWrapper<EntityQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return entityRepository.findAll(pageRequest).map(EntityVO::new);
    }

    @Override
    public void updateZanCount(Long entityId) {

    }
}
