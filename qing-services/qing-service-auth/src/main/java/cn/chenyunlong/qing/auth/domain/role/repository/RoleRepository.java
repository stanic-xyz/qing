package cn.chenyunlong.qing.auth.domain.role.repository;

import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role, RoleId> {

    List<Role> findByIds(List<RoleId> roleIds);

    boolean existsByCode(String code);

    boolean existsByName(String code);
}
