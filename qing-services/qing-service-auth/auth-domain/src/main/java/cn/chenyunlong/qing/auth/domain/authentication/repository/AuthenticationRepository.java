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

package cn.chenyunlong.qing.auth.domain.authentication.repository;

import cn.chenyunlong.qing.auth.domain.authentication.Authentication;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationId;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

/**
 * 认证仓库接口
 *
 * @author 陈云龙
 */
public interface AuthenticationRepository extends BaseRepository<Authentication, AuthenticationId> {

    /**
     * 根据用户ID查询认证记录
     *
     * @param userId 用户ID
     * @return 认证记录列表
     */
    List<Authentication> findByUserId(AggregateId userId);

    /**
     * 根据认证主体查询认证记录
     *
     * @param principal 认证主体
     * @return 认证记录列表
     */
    List<Authentication> findByPrincipal(String principal);
}
