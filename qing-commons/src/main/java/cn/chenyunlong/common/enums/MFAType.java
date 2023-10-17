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

package cn.chenyunlong.common.enums;

import cn.chenyunlong.common.constants.BaseEnum;

/**
 * MFA 类型。
 *
 * @author Stan
 * @since 2022/11/27
 */
public enum MFAType implements BaseEnum<Integer> {

    /**
     * 禁用MFA。
     */
    NONE(0, "Disable"),

    /**
     * 基于时间的一次性密码 (rfc6238)。
     * see: <a href="https://tools.ietf.org/html/rfc6238">...</a>
     */
    TFA_TOTP(1, "TFA_TOTP");

    private final Integer value;

    private final String mfaName;

    MFAType(Integer mtfType, String mfaName) {
        this.value = mtfType;
        this.mfaName = mfaName;
    }

    /**
     * 判断判断是否使用MFA。
     *
     * @param mfaType mfa类型
     * @return 是否使用MFA
     */
    @SuppressWarnings("unused")
    public static boolean useMFA(MFAType mfaType) {
        return mfaType != null && MFAType.NONE != mfaType;
    }

    /**
     * 获取code码存入数据库。
     *
     * @return 获取编码
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * 获取编码名称，便于维护。
     *
     * @return 获取编码名称
     */
    @Override
    public String getName() {
        return mfaName;
    }

}
