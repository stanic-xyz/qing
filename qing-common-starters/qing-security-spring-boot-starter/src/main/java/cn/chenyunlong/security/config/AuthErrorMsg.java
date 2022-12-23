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

package cn.chenyunlong.security.config;

import cn.chenyunlong.common.constants.BaseEnum;

public enum AuthErrorMsg implements BaseEnum {
    /**
     * 认证
     */
    forceLoginOut(70000001, "您的账号在其他设备登录"),
    hasUserOnline(70000002, "当前已有用户在登录"),
    tokenInvalid(70000003, "登录已经过期，请重新登录"),
    tokenIllegal(70000004, "无效令牌"),
    methodNotSupport(70000005, "请求的方法不支持"),
    passwordIncorrect(70000006, "用户名或者密码错误"),
    accessDenied(70000007, "没有操作该功能的权限"),
    verifyCodeIncorrect(70000008, "验证码不正确"),
    accountNotExist(70000010, "账户不存在"),
    tokenExpired(70000009, "登录已经过期，请重新登录"),
    dataInvalid(70000010, "数据格式不正确"),
    requestFail(70000011, "请求失败"),
    AccountIsLock(70000012, "账号已经被禁用"),
    AuthFailure(70000013, "认证失败"),
    DeviceNotSupport(70000014, "设备未识别");

    private final Integer code;
    private final String name;

    AuthErrorMsg(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @return
     */
    @Override
    public Integer getValue() {
        return code;
    }
}
