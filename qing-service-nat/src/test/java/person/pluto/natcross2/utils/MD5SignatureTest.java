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

package person.pluto.natcross2.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class MD5SignatureTest {

    @Test
    public void toHexString() {
        String hexString = MD5Signature.toHexString("我的".getBytes());
        Assert.isTrue("e68891e79a84".equals(hexString), "比对结果错误");
    }

    @Test
    public void getRandomStr() {
    }

    @Test
    public void intToBytes() {
    }

    @Test
    public void bytes2int() {
    }

    @Test
    public void getSignature() {
    }

    @Test
    public void testGetSignature() {
    }
}
