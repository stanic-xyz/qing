package cn.chenyunlong.qing.auth.domain.role.repository;

import cn.chenyunlong.qing.auth.domain.role.Role;
import cn.chenyunlong.qing.auth.domain.role.RoleId;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role, RoleId> {

    List<Role> findByIds(List<Long> roleIds);
}
