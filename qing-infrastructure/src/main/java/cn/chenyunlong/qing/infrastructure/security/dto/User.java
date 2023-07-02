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

package cn.chenyunlong.qing.infrastructure.security.dto;


import cn.chenyunlong.common.enums.MFAType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * @author Stan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long uid;
    private String username;
    private String nickname;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private String description;
    private LocalDateTime expireTime;
    private MFAType mfaType;
    private String mfaKey;
}
