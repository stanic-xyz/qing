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

package person.pluto.natcross2.api.secret;

import lombok.Data;
import person.pluto.natcross2.utils.AESUtil;

import java.security.Key;

/**
 * <p>
 * AES加密方式
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:01:40
 */
@Data
public class AESSecret implements ISecret {

    private Key aesKey;

    @Override
    public byte[] encrypt(byte[] content, int offset, int len) throws Exception {
        return AESUtil.encrypt(aesKey, content, offset, len);
    }

    @Override
    public byte[] decrypt(byte[] result) throws Exception {
        return AESUtil.decrypt(aesKey, result);
    }

    public void setBaseAesKey(String aesKey) {
        this.aesKey = AESUtil.createKeyByBase64(aesKey);
    }

}
