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

package com.lancoo.security.base.extension;


import com.lancoo.security.base.BaseJwtUser;

/**
 * Spring Security工具类
 *
 * @author 陈云龙
 * @author gim
 * @date 2022/11/11
 */
public interface UserContextAware {

    /**
     * 处理token
     *
     * @param token token
     * @return 用户信息
     */
    BaseJwtUser processToken(String token);

}
