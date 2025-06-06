package cn.chenyunlong.qing.infrastructure.auth.repository;

import cn.chenyunlong.qing.auth.domain.platform.Platform;
import cn.chenyunlong.qing.auth.domain.platform.repository.PlatformRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatformRepositoryImpl implements PlatformRepository {

    @Override
    public List<Platform> findByIds(List<Long> platformIds) {
        return List.of();
    }

    @Override
    public Platform save(Platform entity) {
        return null;
    }

    public Optional<Platform> findById(AggregateId id) {
        return Optional.empty();
    }
}
