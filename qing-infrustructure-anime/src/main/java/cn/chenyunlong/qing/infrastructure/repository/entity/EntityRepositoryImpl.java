package cn.chenyunlong.qing.infrastructure.repository.entity;

import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.entity.repository.EntityRepository;
import cn.chenyunlong.qing.infrastructure.repository.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.EntityJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class EntityRepositoryImpl extends JpaServiceImpl<EntityJpaRepository, Entity, Long> implements EntityRepository {

}
