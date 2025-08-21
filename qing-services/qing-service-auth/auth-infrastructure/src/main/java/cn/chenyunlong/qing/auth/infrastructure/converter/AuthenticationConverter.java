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

import cn.chenyunlong.qing.auth.domain.authentication.Authentication;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.AuthenticationEntity;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Component;

/**
 * 认证转换器
 * 负责领域对象和实体对象之间的转换
 *
 * @author 陈云龙
 */
@Component
public class AuthenticationConverter {

    /**
     * 将领域对象转换为实体对象
     *
     * @param authentication 认证领域对象
     * @return 认证实体对象
     */
    public AuthenticationEntity toEntity(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        
        AuthenticationEntity entity = new AuthenticationEntity();
        entity.setPrincipal(authentication.getPrincipal());
        entity.setCredentials(authentication.getCredentials());
        entity.setStatus(authentication.getStatus().name());
        entity.setIpAddress(authentication.getIpAddress());
        entity.setUserAgent(authentication.getUserAgent());
        
        // BaseJpaAggregate会自动处理validStatus
        entity.setVersion(authentication.getVersion());

        return entity;
    }

    /**
     * 将实体对象转换为领域对象
     *
     * @param entity 认证实体对象
     * @return 认证领域对象
     */
    public Authentication toDomain(AuthenticationEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Authentication authentication = new Authentication();
        authentication.setId(new AggregateId(entity.getId()));
        authentication.setPrincipal(entity.getPrincipal());
        authentication.setCredentials(entity.getCredentials());
        authentication.setStatus(Enum.valueOf(cn.chenyunlong.qing.auth.domain.authentication.AuthenticationStatus.class, entity.getStatus()));
        authentication.setIpAddress(entity.getIpAddress());
        authentication.setUserAgent(entity.getUserAgent());
        
        if (entity.getUserId() != null) {
            authentication.setUserId(new AggregateId(entity.getUserId()));
        }
        
        // BaseJpaAggregate会自动处理validStatus
        authentication.setVersion(entity.getVersion());
        authentication.setCreatedAt(entity.getCreatedAt());

        return authentication;
    }
}
