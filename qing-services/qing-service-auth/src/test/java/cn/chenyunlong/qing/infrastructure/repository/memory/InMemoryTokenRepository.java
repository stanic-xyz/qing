package cn.chenyunlong.qing.infrastructure.repository.memory;

import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenRepository;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenId;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenType;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InMemoryTokenRepository implements TokenRepository {

    private final ConcurrentMap<TokenId, AuthenticationToken> store = new ConcurrentHashMap<>();

    @Override
    public Optional<AuthenticationToken> findByTokenValue(String tokenValue) {
        return store.values().stream()
                .filter(t -> t.getTokenValue().equals(tokenValue))
                .findFirst();
    }

    @Override
    public Optional<AuthenticationToken> findByRefreshTokenValue(String refreshToken) {
        return store.values().stream().filter(authenticationToken ->
                StrUtil.equals(authenticationToken.getRefreshTokenValue(), refreshToken)).findFirst();
    }

    @Override
    public List<AuthenticationToken> findValidTokensByUserId(UserId userId) {
        return store.values().stream()
                .filter(token -> token.getUserId().equals(userId))
                .filter(AuthenticationToken::isValid)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthenticationToken> findValidTokensByUserIdAndType(UserId userId, TokenType tokenType) {
        return store.values().stream()
                .filter(t -> t.getUserId().equals(userId))
                .filter(t -> t.getTokenType() == tokenType)
                .filter(AuthenticationToken::isValid)
                .collect(Collectors.toList());
    }

    @Override
    public int revokeAllTokensByUserId(UserId userId, String reason) {
        List<AuthenticationToken> toRevoke = store.values().stream()
                .filter(t -> t.getUserId().equals(userId))
                .toList();
        toRevoke.forEach(t -> {
            if (!t.isRevoked()) {
                t.revoke(reason);
                store.put(t.getId(), t);
            }
        });
        return toRevoke.size();
    }

    @Override
    public AuthenticationToken save(AuthenticationToken entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<AuthenticationToken> findById(TokenId tokenId) {
        return Optional.ofNullable(store.get(tokenId));
    }
}
