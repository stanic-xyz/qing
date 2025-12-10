package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenRepository;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenId;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenType;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.infrastructure.converter.UserTokenMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.TokenEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.TokenJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepositoryImpl implements TokenRepository {
    private final TokenJpaRepository tokenJpaRepository;
    private final UserTokenMapper userTokenMapper;

    public TokenRepositoryImpl(TokenJpaRepository tokenJpaRepository, UserTokenMapper userTokenMapper) {
        this.tokenJpaRepository = tokenJpaRepository;
        this.userTokenMapper = userTokenMapper;
    }

    @Override
    public Optional<AuthenticationToken> findByTokenValue(String tokenValue) {
        TokenEntity byTokenValue = tokenJpaRepository.findByTokenValue(tokenValue);
        AuthenticationToken authenticationToken = userTokenMapper.entityToDomain(byTokenValue);
        return Optional.ofNullable(authenticationToken);
    }

    @Override
    public Optional<AuthenticationToken> findByRefreshTokenValue(String refreshToken) {
        return tokenJpaRepository.findByRefreshTokenValue(refreshToken).map(userTokenMapper::entityToDomain);
    }

    @Override
    public List<AuthenticationToken> findValidTokensByUserId(UserId userId) {
        List<TokenEntity> entities = tokenJpaRepository.findValidTokensByUserId(userId.id(), Instant.now(), false);
        return entities.stream().map(userTokenMapper::entityToDomain).toList();
    }

    @Override
    public List<AuthenticationToken> findValidTokensByUserIdAndType(UserId userId, TokenType tokenType) {
        List<TokenEntity> entities = tokenJpaRepository.findValidTokensByUserIdAndType(userId.id(), tokenType.name(), Instant.now(), false);
        return entities.stream().map(userTokenMapper::entityToDomain).toList();
    }

    @Override
    public int revokeAllTokensByUserId(UserId userId, String reason) {
        return tokenJpaRepository.revokeAllTokensByUserId(userId.id(), Instant.now(), true, reason);
    }

    @Override
    public AuthenticationToken save(AuthenticationToken entity) {
        // 内存实现：直接返回入参以保证领域逻辑不发生空指针
        TokenEntity tokenEntity = userTokenMapper.domainToEntity(entity);
        tokenJpaRepository.save(tokenEntity);
        return entity;
    }

    @Override
    public Optional<AuthenticationToken> findById(TokenId id) {
        return tokenJpaRepository.findById(id.id()).map(userTokenMapper::entityToDomain);
    }
}
