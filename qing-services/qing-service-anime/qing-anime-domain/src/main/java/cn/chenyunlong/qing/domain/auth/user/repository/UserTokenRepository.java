package cn.chenyunlong.qing.domain.auth.user.repository;

import cn.chenyunlong.qing.domain.auth.user.UserToken;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

public interface UserTokenRepository extends BaseRepository<UserToken, AggregateId> {

}
