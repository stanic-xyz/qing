package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.auth.RoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends BaseJpaRepository<RoleEntity, Long> {

    @Query("select r from RoleEntity r where r.role = ?1")
    Role findRoleByRole(String role);

    @Query("select r from RoleEntity r where r.name = ?1")
    Role findRoleByName(String roleName);
}
