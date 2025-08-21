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

package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.TokenEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 令牌JPA仓库接口
 *
 * @author 陈云龙
 */
@Repository
public interface TokenJpaRepository extends BaseJpaQueryRepository<TokenEntity, Long> {

    /**
     * 根据令牌值查询令牌
     *
     * @param tokenValue 令牌值
     * @return 令牌实体
     */
    @Query("SELECT t FROM TokenEntity t WHERE t.tokenValue = :tokenValue")
    TokenEntity findByTokenValue(@Param("tokenValue") String tokenValue);

    /**
     * 查询用户有效令牌
     *
     * @param userId  用户ID
     * @param now     当前时间
     * @param revoked 是否已撤销
     * @return 令牌实体列表
     */
    @Query("SELECT t FROM TokenEntity t WHERE t.userId = :userId AND t.expiresAt > :now AND t.revoked = :revoked")
    List<TokenEntity> findValidTokensByUserId(
        @Param("userId") Long userId,
        @Param("now") LocalDateTime now,
        @Param("revoked") boolean revoked);

    /**
     * 查询用户指定类型的有效令牌
     *
     * @param userId    用户ID
     * @param tokenType 令牌类型
     * @param now       当前时间
     * @param revoked   是否已撤销
     * @return 令牌实体列表
     */
    @Query("SELECT t FROM TokenEntity t WHERE t.userId = :userId AND t.tokenType = :tokenType AND t.expiresAt > :now AND t.revoked = :revoked")
    List<TokenEntity> findValidTokensByUserIdAndType(
        @Param("userId") Long userId,
        @Param("tokenType") String tokenType,
        @Param("now") LocalDateTime now,
        @Param("revoked") boolean revoked);

    /**
     * 撤销用户所有令牌
     *
     * @param userId  用户ID
     * @param now     撤销时间
     * @param revoked 撤销状态
     * @param reason  撤销原因
     * @return 影响的行数
     */
    @Modifying
    @Query("UPDATE TokenEntity t SET t.revoked = :revoked, t.revokeReason = :reason WHERE t.userId = :userId AND t.expiresAt > :now")
    int revokeAllTokensByUserId(
        @Param("userId") Long userId,
        @Param("now") LocalDateTime now,
        @Param("revoked") boolean revoked,
        @Param("reason") String reason);
}
