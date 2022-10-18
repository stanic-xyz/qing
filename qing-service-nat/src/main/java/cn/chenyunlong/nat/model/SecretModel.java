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

package cn.chenyunlong.nat.model;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * <p>
 * 加密模型，全局使用统一的
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 09:53:51
 */
@Data
@EqualsAndHashCode
public class SecretModel {

    /**
     * base64格式的AES密钥
     */
    private String aeskey;

    /**
     * 交互签名密钥
     */
    private String tokenKey;

    public SecretModel() {
    }

    /**
     * 判断是否启用加密模式
     *
     * @return 返回是否启用加密模式
     * @author Pluto
     * @since 2020-01-10 09:57:55
     */
    public boolean isValid() {
        return StringUtils.isNoneBlank(this.getAeskey(), this.getTokenKey());
    }

}
