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
 */

package cn.chenyunlong.natcross.tools;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author 陈云龙
 * @date 2020/11/18
 **/
public class RSAEncryptTest {

    @Test
    public void testRSA() throws Exception {
        String publicPath = "src/test/resources/ssl"; //公匙存放位置
        String privatePath = "src/test/resources/ssl"; //私匙存放位置

        Base64 base64 = new Base64();

        System.out.println("--------------公钥加密私钥解密过程-------------------");
        String signKey = "ihep_公钥加密私钥ff粉粉粉粉粉粉粉顶顶顶顶粉粉粉粉解密";
        // 公钥加密过程
        byte[] cipherData = RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(publicPath)),
                signKey.getBytes());
        String cipher = new String(base64.encode(cipherData));
        // 私钥解密过程
        byte[] res = RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(privatePath)),
                base64.decode(cipher));
        assert res != null;
        String restr = new String(res);
        System.out.println("原文：" + signKey);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + restr);
        System.out.println();

        System.out.println("--------------私钥加密公钥解密过程-------------------");
        // 私钥加密过程
        cipherData = RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(privatePath)), signKey.getBytes());
        cipher = new String(base64.encode(cipherData));
        // 公钥解密过程
        res = RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(publicPath)), base64.decode(cipher));
        assert res != null;
        restr = new String(res);
        System.out.println("原文：" + signKey);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + restr);
        System.out.println();
        Assertions.assertEquals(signKey, restr);
    }
}
