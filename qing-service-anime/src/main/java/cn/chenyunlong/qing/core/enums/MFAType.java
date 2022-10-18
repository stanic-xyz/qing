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

package cn.chenyunlong.qing.core.enums;

/**
 * MFA type.
 *
 * @author Stan
 */
public enum MFAType implements ValueEnum<Integer> {

    /**
     * Disable MFA auth.
     */
    NONE(0),

    /**
     * Time-based One-time Password (rfc6238).
     * see: https://tools.ietf.org/html/rfc6238
     */
    TFA_TOTP(1);

    private final Integer value;

    MFAType(Integer value) {
        this.value = value;
    }

    public static boolean useMFA(MFAType mfaType) {
        return mfaType != null && MFAType.NONE != mfaType;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
