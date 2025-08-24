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

import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.TokenId;
import cn.chenyunlong.qing.auth.domain.authentication.TokenType;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

/**
 * 令牌仓库接口
 *
 * @author 陈云龙
 */
public interface TokenRepository extends BaseRepository<AuthenticationToken, TokenId> {

    /**
     * 根据令牌值查找令牌
     *
     * @param tokenValue 令牌值
     * @return 令牌对象
     */
    Optional<AuthenticationToken> findByTokenValue(String tokenValue);

    /**
     * 根据用户ID查找有效的令牌列表
     *
     * @param userId 用户ID
     * @return 令牌列表
     */
    List<AuthenticationToken> findValidTokensByUserId(AggregateId userId);

    /**
     * 根据用户ID和令牌类型查找有效的令牌
     *
     * @param userId    用户ID
     * @param tokenType 令牌类型
     * @return 令牌列表
     */
    List<AuthenticationToken> findValidTokensByUserIdAndType(AggregateId userId, TokenType tokenType);

    /**
     * 撤销用户的所有令牌
     *
     * @param userId 用户ID
     * @param reason 撤销原因
     * @return 撤销的令牌数量
     */
    int revokeAllTokensByUserId(AggregateId userId, String reason);
}
