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

package cn.chenyunlong.qing.domain.auth.user;


import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * 用户信息
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class UserToken extends BaseAggregate {

    @Column(unique = true)
    @FieldDesc(name = "用户唯一ID")
    private Long uid;

    private Long applicationId;

    private String deviceId;

    private String ip;

    private String accessToken;
    private String refreshToken;
    private Instant expireAt;
    private Instant createTime;


    private Instant updateTime;

    /**
     * 登录类型
     */
    private Integer loginType;

    private Instant lastLoginTime;

    private Integer tokenType;

}
