package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.RoleResourceRelJpaRepository;
import cn.chenyunlong.qing.domain.auth.role.RoleResourceRel;
import cn.chenyunlong.qing.domain.auth.role.repository.RoleResourceRelRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleResourceRelRepositoryImpl extends JpaServiceImpl<RoleResourceRelJpaRepository, RoleResourceRel, Long> implements RoleResourceRelRepository {

}
