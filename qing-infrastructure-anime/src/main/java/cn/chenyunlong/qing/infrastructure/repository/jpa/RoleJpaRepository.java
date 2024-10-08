package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.role.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends BaseJpaRepository<Role, Long> {

    @Query("select r from Role r where r.role = ?1")
    Role findRoleByRole(String role);

    @Query("select r from Role r where r.name = ?1")
    Role findRoleByName(String roleName);
}
