package cn.chenyunlong.qing.auth.domain.authentication.repository;

import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;

import java.util.Optional;

/**
 * 认证令牌仓库
 */
public interface AuthenticationTokenRepository {

    AuthenticationToken save(AuthenticationToken token);

    Optional<AuthenticationToken> findById(TokenId tokenId);

    Optional<AuthenticationToken> findByTokenValue(String tokenValue);

    void revokeByUserId(UserId userId);

    void cleanExpiredTokens();
}
