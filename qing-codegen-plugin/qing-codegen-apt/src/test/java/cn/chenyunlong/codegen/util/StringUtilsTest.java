/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.codegen.util;

import junit.framework.TestCase;
import org.junit.Assert;

public class StringUtilsTest extends TestCase {

    public void testCamel() {
        Assert.assertEquals("username", StringUtils.lowerCamel("username"));
        Assert.assertEquals("fileName", StringUtils.lowerCamel("fileName"));
    }

    public void testUpperCamel() {
        Assert.assertEquals("Username", StringUtils.bigCamel("username"));
        Assert.assertEquals("FileName", StringUtils.bigCamel("fileName"));
    }
}
