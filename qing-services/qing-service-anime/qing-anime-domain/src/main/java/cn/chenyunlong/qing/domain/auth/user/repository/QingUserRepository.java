package cn.chenyunlong.qing.domain.auth.user.repository;

import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

public interface QingUserRepository extends BaseRepository<QingUser, AggregateId> {

}
