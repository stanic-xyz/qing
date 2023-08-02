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

package cn.chenyunlong.security.config.security.configures.my;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * @author Stan
 */
public class MyAuthTokenConfigurer
    extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    public MyAuthTokenConfigurer() {
        /**
         * 也是我实现的UserDetailsService
         */
        /**
         * 我们之前自定义的 token功能类
         */
    }

    public static MyAuthTokenConfigurer myLogin() {
        return new MyAuthTokenConfigurer();
    }

    @Override
    public void configure(HttpSecurity http) {

    }
}
