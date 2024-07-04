package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.auth.role.RoleResourceRel;
import cn.chenyunlong.qing.domain.auth.role.repository.RoleResourceRelRepository;
import cn.chenyunlong.qing.infrastructure.repository.jpa.RoleResourceRelJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleResourceRelRepositoryImpl extends JpaServiceImpl<RoleResourceRelJpaRepository, RoleResourceRel, Long> implements RoleResourceRelRepository {

}
