package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.TokenType;
import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepositoryImpl implements TokenRepository {
    @Override
    public Optional<AuthenticationToken> findByTokenValue(String tokenValue) {
        return Optional.empty();
    }

    @Override
    public List<AuthenticationToken> findValidTokensByUserId(AggregateId userId) {
        return List.of();
    }

    @Override
    public List<AuthenticationToken> findValidTokensByUserIdAndType(AggregateId userId, TokenType tokenType) {
        return List.of();
    }

    @Override
    public int revokeAllTokensByUserId(AggregateId userId, String reason) {
        return 0;
    }

    @Override
    public AuthenticationToken save(AuthenticationToken entity) {
        return null;
    }

    @Override
    public Optional<AuthenticationToken> findById(AggregateId id) {
        return Optional.empty();
    }
}
