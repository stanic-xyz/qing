package cn.chenyunlong.qing.auth.domain.user.repository;

import cn.chenyunlong.qing.auth.domain.user.UserToken;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

public interface UserTokenRepository extends BaseRepository<UserToken, AggregateId> {

}
