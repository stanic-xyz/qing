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

import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author 陈云龙
 * @date 2020/11/18
 **/
public class RSA2UtilsTest {

    @Test
    public void testCreateKeys() {
        Map<String, String> keys = RSA2Utils.createKeys(1024);
        keys.keySet().forEach(key -> System.out.println(key + "\t:" + keys.get(key)));
    }

    public void testGetPublicKey() {
    }

    public void testGetPrivateKey() {
    }

    public void testPublicEncrypt() {
    }

    public void testPrivateDecrypt() {
    }

    public void testPrivateEncrypt() {
    }

    public void testPublicDecrypt() {
    }
}
