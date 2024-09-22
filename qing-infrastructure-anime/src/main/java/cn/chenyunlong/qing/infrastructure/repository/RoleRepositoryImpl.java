package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.domain.auth.role.repository.RoleRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.RoleJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleRepositoryImpl extends JpaServiceImpl<RoleJpaRepository, Role, Long> implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;

    /**
     * 根据角色名称查询角色
     */
    @Override
    public Role findRoleByRole(String role) {
        return roleJpaRepository.findRoleByRole(role);
    }

    /**
     * 根据角色名称查询角色
     */
    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleJpaRepository.findRoleByName(roleName);
    }

    /**
     * 根据用户ID查询角色
     */
    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return roleJpaRepository.findAll();
    }
}
