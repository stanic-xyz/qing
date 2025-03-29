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
import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class QingUser extends BaseAggregate {

    @Column(unique = true)
    @FieldDesc(name = "用户唯一ID")
    private Long uid;

    @FieldDesc(name = "用户名", description = "用户（唯一），用于前端显示！")
    @Column(unique = true)
    private String username;

    @FieldDesc(name = "昵称", description = "用户名称（唯一），用于前端显示！")
    @Column(unique = true)
    private String nickname;

    private String password;

    @FieldDesc(name = "密码过期")
    private Boolean credentialsExpired;

    private String phone;
    private String email;
    private String avatar;
    private String description;
    private LocalDateTime expireTime;
    private MFAType mfaType;
    private String mfaKey;
}
