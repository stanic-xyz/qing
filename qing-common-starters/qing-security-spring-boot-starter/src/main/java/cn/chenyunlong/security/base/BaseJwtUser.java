/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.security.base;

import cn.chenyunlong.common.annotation.FieldDesc;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 基础session 数据 通过token 直接能换取的信息
 */
@Data
public abstract class BaseJwtUser implements Serializable {

    @FieldDesc(name = "用户Id")
    private String userId;

    @FieldDesc(name = "用户名")
    private String username;

    @FieldDesc(name = "token")
    private String token;

    @FieldDesc(name = "额外信息")
    private Map<String, String> extInfo;

    @FieldDesc(name = "权限信息")
    private Collection<? extends GrantedAuthority> authorities;
}
