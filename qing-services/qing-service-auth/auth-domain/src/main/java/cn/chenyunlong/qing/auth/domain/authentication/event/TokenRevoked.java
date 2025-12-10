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

package cn.chenyunlong.qing.auth.domain.authentication.event;

import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 令牌撤销事件
 *
 * @author 陈云龙
 */
@Getter
public class TokenRevoked {

    /**
     * 令牌ID
     */
    private final TokenId tokenId;

    /**
     * 用户ID
     */
    private final UserId userId;

    /**
     * 撤销原因
     */
    private final String reason;

    /**
     * 撤销时间
     */
    private final LocalDateTime revokedAt;

    /**
     * 构造函数
     *
     * @param tokenId 令牌ID
     * @param userId  用户ID
     * @param reason  撤销原因
     */
    public TokenRevoked(TokenId tokenId, UserId userId, String reason) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.reason = reason;
        this.revokedAt = LocalDateTime.now();
    }
}
