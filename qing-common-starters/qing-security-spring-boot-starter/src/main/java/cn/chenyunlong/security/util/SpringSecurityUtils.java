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

package cn.chenyunlong.security.util;

import cn.chenyunlong.security.base.BaseJwtUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * Spring Security工具类
 */
public class SpringSecurityUtils {

    /**
     * 获取当前用户名
     *
     * @return 当前用户信息
     */
    public static String getCurrentUsername() {
        String temp;
        try {
            BaseJwtUser jwtUser = getJwtUser();
            if (Objects.nonNull(jwtUser)) {
                return jwtUser.getUserId();
            } else {
                temp = "-";
            }
        } catch (Exception e) {
            temp = "";
        }
        return temp;
    }

    public static BaseJwtUser getJwtUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass().isAssignableFrom(AnonymousAuthenticationToken.class)) {
            return null;
        } else {
            return (BaseJwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
    }
}
