package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.domain.auth.role.repository.RoleRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.RoleJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleRepositoryImpl extends JpaServiceImpl<RoleJpaRepository, Role, Long> implements RoleRepository {

}
