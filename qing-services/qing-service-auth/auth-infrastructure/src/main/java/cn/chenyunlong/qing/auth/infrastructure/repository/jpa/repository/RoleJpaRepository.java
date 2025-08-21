package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.auth.domain.role.Role;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends BaseJpaRepository<RoleEntity, Long> {

    @Query("select r from RoleEntity r where r.role = ?1")
    Role findRoleByRole(String role);

    @Query("select r from RoleEntity r where r.name = ?1")
    Role findRoleByName(String roleName);
}
