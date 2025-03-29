package cn.chenyunlong.qing.domain.auth.role.repository;

import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role, AggregateId> {

    List<Role> findByIds(List<Long> roleIds);
}
