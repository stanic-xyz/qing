package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PermissionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionJpaRepository extends BaseJpaRepository<PermissionEntity, Long> {

    boolean existsByCode(String code);

    boolean existsByName(String name);
}

