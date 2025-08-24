package cn.chenyunlong.qing.auth.domain.platform.repository;

import cn.chenyunlong.qing.auth.domain.platform.Platform;
import cn.chenyunlong.qing.auth.domain.platform.PlatformId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

public interface PlatformRepository extends BaseRepository<Platform, PlatformId> {

    List<Platform> findByIds(List<Long> platformIds);
}
