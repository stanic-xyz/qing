package cn.chenyunlong.qing.infrastructure.jpa.memory;

import cn.chenyunlong.qing.auth.domain.platform.Platform;
import cn.chenyunlong.qing.auth.domain.platform.PlatformId;
import cn.chenyunlong.qing.auth.domain.platform.repository.PlatformRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryPlatformRepository implements PlatformRepository {

    private final ConcurrentHashMap<PlatformId, Platform> store = new ConcurrentHashMap<>();

    @Override
    public List<Platform> findByIds(List<Long> platformIds) {
        return store.values().stream()
                .filter(platform -> platformIds.contains(platform.getId().id()))
                .collect(Collectors.toList());
    }

    @Override
    public Platform save(Platform entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Platform> findById(PlatformId platformId) {
        return Optional.ofNullable(store.get(platformId));
    }
}
