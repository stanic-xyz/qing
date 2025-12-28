package cn.chenyunlong.qing.infrastructure.jpa.memory;

import cn.chenyunlong.qing.auth.domain.platform.Platform;
import cn.chenyunlong.qing.auth.domain.platform.PlatformId;
import cn.chenyunlong.qing.auth.domain.platform.repository.PlatformRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryPlatformRepository implements PlatformRepository {
    @Override
    public List<Platform> findByIds(List<Long> platformIds) {
        return List.of();
    }

    @Override
    public Platform save(Platform entity) {
        return null;
    }

    @Override
    public Optional<Platform> findById(PlatformId platformId) {
        return Optional.empty();
    }
}
