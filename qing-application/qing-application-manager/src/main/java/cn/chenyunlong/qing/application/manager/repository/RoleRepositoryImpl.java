package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.RoleJpaRepository;
import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.domain.auth.role.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleRepositoryImpl extends JpaServiceImpl<RoleJpaRepository, Role, Long> implements RoleRepository {

}
