package cn.chenyunlong.qing.domain.auth.platform.repository;

import cn.chenyunlong.qing.domain.auth.platform.Platform;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

public interface PlatformRepository extends BaseRepository<Platform, AggregateId> {

    List<Platform> findByIds(List<Long> platformIds);
}
