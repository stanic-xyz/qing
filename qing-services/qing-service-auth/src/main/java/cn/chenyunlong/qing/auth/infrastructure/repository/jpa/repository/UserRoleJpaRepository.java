package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.UserRoleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleJpaRepository extends BaseJpaRepository<UserRoleEntity, Long> {

    boolean existsByUserIdAndRoleIdAndRevokedFalse(Long userId, Long roleId);

    boolean existsByRevokedFalse();

    /**
     * 根据用户ID与角色ID查询未撤销的关联
     */
    Optional<UserRoleEntity> findByUserIdAndRoleIdAndRevokedFalse(Long userId, Long roleId);

    /**
     * 根据用户ID查询未撤销的角色关联
     */
    List<UserRoleEntity> findByUserIdAndRevokedFalse(Long userId);
}
