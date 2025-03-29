package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.auth.platform.Platform;
import cn.chenyunlong.qing.domain.auth.platform.repository.PlatformRepository;
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
