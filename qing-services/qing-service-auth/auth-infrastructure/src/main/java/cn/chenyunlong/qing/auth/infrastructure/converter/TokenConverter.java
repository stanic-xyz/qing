/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.TokenType;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.TokenEntity;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Component;

/**
 * 令牌转换器
 * 负责领域对象和实体对象之间的转换
 *
 * @author 陈云龙
 */
@Component
public class TokenConverter {

    /**
     * 将领域对象转换为实体对象
     *
     * @param token 令牌领域对象
     * @return 令牌实体对象
     */
    public TokenEntity toEntity(AuthenticationToken token) {
        if (token == null) {
            return null;
        }

        TokenEntity entity = new TokenEntity();
        entity.setId(token.getId().getId());
        entity.setTokenValue(token.getTokenValue());
        entity.setTokenType(token.getTokenType().name());

        if (token.getUserId() != null) {
            entity.setUserId(token.getUserId().getId());
        }

        entity.setExpiresAt(token.getExpiresAt());
        entity.setRevoked(token.isRevoked());
        entity.setRevokeReason(token.getRevocationReason());
        // BaseJpaAggregate会自动处理validStatus
        entity.setVersion(token.getVersion());

        return entity;
    }

    /**
     * 将实体对象转换为领域对象
     *
     * @param entity 令牌实体对象
     * @return 令牌领域对象
     */
    public AuthenticationToken toDomain(TokenEntity entity) {
        if (entity == null) {
            return null;
        }

        AuthenticationToken token = new AuthenticationToken();
        token.setId(new AggregateId(entity.getId()));
        token.setTokenValue(entity.getTokenValue());
        token.setTokenType(TokenType.valueOf(entity.getTokenType()));

        if (entity.getUserId() != null) {
            token.setUserId(new AggregateId(entity.getUserId()));
        }

        token.setExpiresAt(entity.getExpiresAt());
        token.setRevoked(entity.getRevoked());
        token.setRevocationReason(entity.getRevokeReason());
        // BaseJpaAggregate会自动处理validStatus
        token.setVersion(entity.getVersion());
        token.setCreatedAt(entity.getCreatedAt());
        return token;
    }
}
