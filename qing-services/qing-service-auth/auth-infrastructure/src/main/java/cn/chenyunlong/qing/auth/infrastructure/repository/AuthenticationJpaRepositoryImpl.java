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

package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.authentication.Authentication;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationId;
import cn.chenyunlong.qing.auth.domain.authentication.repository.AuthenticationRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 认证JPA仓库接口
 *
 * @author 陈云龙
 */
@Repository
public class AuthenticationJpaRepositoryImpl implements AuthenticationRepository {


    @Override
    public List<Authentication> findByUserId(AggregateId userId) {
        return List.of();
    }

    @Override
    public List<Authentication> findByPrincipal(String principal) {
        return List.of();
    }

    @Override
    public Authentication save(Authentication entity) {
        return null;
    }

    @Override
    public Optional<Authentication> findById(AuthenticationId id) {
        return Optional.empty();
    }
}
