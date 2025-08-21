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

import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.Getter;

/**
 * 认证成功事件
 *
 * @author 陈云龙
 */
@Getter
public class AuthenticationSucceeded {

    private final AggregateId authenticationId;
    private final AggregateId userId;
    private final long timestamp;

    public AuthenticationSucceeded(AggregateId authenticationId, AggregateId userId) {
        this.authenticationId = authenticationId;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }
}