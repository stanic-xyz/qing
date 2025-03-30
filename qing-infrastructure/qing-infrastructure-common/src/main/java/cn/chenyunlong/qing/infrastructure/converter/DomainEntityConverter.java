package cn.chenyunlong.qing.infrastructure.converter;

import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.chenyunlong.qing.infrastructure.entity.BaseEntity;

public interface DomainEntityConverter<D extends BaseAggregate, E extends BaseEntity> {
    E toEntity(D domain);

    D toDomain(E entity);
}
