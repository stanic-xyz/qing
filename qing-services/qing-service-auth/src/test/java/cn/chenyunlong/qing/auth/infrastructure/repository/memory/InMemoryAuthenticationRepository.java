package cn.chenyunlong.qing.auth.infrastructure.repository.memory;

import cn.chenyunlong.qing.auth.domain.authentication.Authentication;
import cn.chenyunlong.qing.auth.domain.authentication.repository.AuthenticationRepository;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.AuthenticationId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 内存 Authentication 仓库实现，仅用于集成测试
 */
@Repository
public class InMemoryAuthenticationRepository implements AuthenticationRepository {

    private final Map<AuthenticationId, Authentication> store = new ConcurrentHashMap<>();

    @Override
    public List<Authentication> findByUserId(UserId userId) {
        return store.values().stream()
                .filter(a -> a.getUserId() != null && a.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Authentication> findByPrincipal(String principal) {
        return store.values().stream()
                .filter(a -> principal != null && principal.equals(a.getPrincipal()))
                .collect(Collectors.toList());
    }

    @Override
    public Authentication save(Authentication entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Authentication> findById(AuthenticationId id) {
        return Optional.ofNullable(store.get(id));
    }
}
