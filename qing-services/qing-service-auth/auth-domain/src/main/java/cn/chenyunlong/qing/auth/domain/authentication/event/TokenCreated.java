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

import cn.chenyunlong.qing.auth.domain.authentication.TokenId;
import cn.chenyunlong.qing.auth.domain.user.QingUserId;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 令牌创建事件
 *
 * @author 陈云龙
 */
@Getter
public class TokenCreated {

    /**
     * 令牌ID
     */
    private final TokenId tokenId;

    /**
     * 用户ID
     */
    private final QingUserId userId;

    /**
     * 创建时间
     */
    private final LocalDateTime createdAt;

    /**
     * 构造函数
     *
     * @param tokenId 令牌ID
     * @param userId  用户ID
     */
    public TokenCreated(TokenId tokenId, QingUserId userId) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}
