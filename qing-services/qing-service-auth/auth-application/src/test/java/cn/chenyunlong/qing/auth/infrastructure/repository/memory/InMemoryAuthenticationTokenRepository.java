package cn.chenyunlong.qing.auth.infrastructure.repository.memory;

import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.repository.AuthenticationTokenRepository;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存 Token 仓库实现，仅用于集成测试
 */
@Repository
public class InMemoryAuthenticationTokenRepository implements AuthenticationTokenRepository {

    private final Map<TokenId, AuthenticationToken> store = new ConcurrentHashMap<>();

    @Override
    public AuthenticationToken save(AuthenticationToken token) {
        store.put(token.getId(), token);
        return token;
    }

    @Override
    public Optional<AuthenticationToken> findById(TokenId tokenId) {
        return Optional.ofNullable(store.get(tokenId));
    }

    @Override
    public Optional<AuthenticationToken> findByTokenValue(String tokenValue) {
        return store.values().stream()
                .filter(t -> t.getTokenValue().equals(tokenValue))
                .findFirst();
    }

    @Override
    public void revokeByUserId(UserId userId) {
        store.values().removeIf(t -> t.getUserId().equals(userId));
    }

    @Override
    public void cleanExpiredTokens() {
        store.values().removeIf(t -> t.getExpiresAt().isBefore(Instant.now()));
    }
}
