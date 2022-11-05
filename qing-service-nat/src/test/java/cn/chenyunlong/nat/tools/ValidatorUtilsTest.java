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

package cn.chenyunlong.nat.tools;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Locale;

public class ValidatorUtilsTest {

    @Test
    public void isUserId() {
        Assert.isTrue(ValidatorUtils.isUserId("1000000"), "校验用户类型信息错误");
    }

    @Test
    public void isUserAccount() {
    }

    @Test
    public void isMobileNO() {
        Assert.isTrue(ValidatorUtils.isMobileNO("13628559626"), "建议是否是移动电话号码方法错误");
    }

    @Test
    public void isEmail() {
        Assert.isTrue(ValidatorUtils.isEmail("1576302867@qq.com"), "建议是否是Email方法错误");
    }

    @Test
    void testIsMobileNO() {
    }

    @Test
    void testIsEmail() {
    }

    @Test
    void testIsUserId() {
    }

    @Test
    void testIsUserAccount() {
    }

    @Test
    void isUserPwd() {
    }

    @Test
    void isPassport() {
    }

    @Test
    void isZipCode() {
    }

    @Test
    void isOrganizeCode() {
    }

    @Test
    void isOfficePhone() {
    }

    @Test
    void isRegisteredCapital() {
        Assert.isTrue(ValidatorUtils.isRegisteredCapital("336.25"), "注册资本校验失败");
    }

    @Test
    void isBigDecimal() {
    }

    @Test
    void isBankAccountNo() {
        Assert.isTrue(ValidatorUtils.isBankAccountNo("621700521152856"), "校验银行卡账号错误");
    }

    @Test
    void isIdNumber() {
    }

    @Test
    void IDCardValidate() {
    }

    @Test
    void getAreaCode() {
    }

    @Test
    void isNumeric() {
    }

    @Test
    void isDate() {
    }

    @Test
    void isIPv4Address() {
        Assert.isTrue(ValidatorUtils.isIPv4Address("125.252.123.1"), "广播");
    }

    @Test
    void getConstellation() {
        Assert.isTrue("".equals(ValidatorUtils.getConstellation(null, Locale.CHINESE)), "射手座错误");
        //水瓶座
        Assert.isTrue("水瓶座".equals(ValidatorUtils.getConstellation(LocalDate.of(2022, 1, 20), Locale.CHINESE)), "水瓶座错误");
        //射手座
        Assert.isTrue("射手座".equals(ValidatorUtils.getConstellation(LocalDate.of(2022, 12, 21), Locale.CHINESE)), "射手座错误");

        //国际化

        //水瓶座
        String constellationEnglish = ValidatorUtils.getConstellation(LocalDate.of(2022, 1, 20), Locale.ENGLISH);
        Assert.isTrue("Aquarius".equals(constellationEnglish), "水瓶座英语错误");
        //射手座
        Assert.isTrue("Sagittarius".equals(ValidatorUtils.getConstellation(LocalDate.of(2022, 12, 21), Locale.ENGLISH)), "射手座英语错误");
    }
}
