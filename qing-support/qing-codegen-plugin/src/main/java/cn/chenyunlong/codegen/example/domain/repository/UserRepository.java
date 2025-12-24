package cn.chenyunlong.codegen.example.domain.repository;

import cn.chenyunlong.codegen.example.domain.User;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

public interface UserRepository extends BaseRepository<User, AggregateId> {
}
