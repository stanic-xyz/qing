package cn.chenyunlong.qing.auth.domain.user.valueObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneNumberTest {

    @Test
    void shouldCreateValidChineseMobileNumber() {
        // Given
        String[] validNumbers = {
                "13800138000",
                "+8613800138000",
                "008613800138000",
                "86 138 0013 8000",
                "138-0013-8000",
                "(86)13800138000"
        };

        for (String number : validNumbers) {
            // When
            PhoneNumber phoneNumber = PhoneNumber.of(number);

            // Then
            assertThat(phoneNumber).isNotNull();
            assertThat(phoneNumber.toE164()).isEqualTo("+8613800138000");
            assertThat(phoneNumber.countryCode()).isEqualTo("+86");
            assertThat(phoneNumber.localNumber()).isEqualTo("13800138000");
            assertThat(phoneNumber.isChineseMobile()).isTrue();
        }
    }

    @Test
    void shouldThrowExceptionForInvalidChineseNumber() {
        // Given
        String[] invalidNumbers = {
                "12345678901",      // 无效号段
                "1380013800",       // 长度不足
                "138001380000",     // 长度过长
                "abcdefg",          // 非数字
                "",                 // 空字符串
                null                // null
        };

        for (String number : invalidNumbers) {
            // When & Then
            assertThatThrownBy(() -> PhoneNumber.of(number))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void shouldFormatPhoneNumberCorrectly() {
        // Given
        PhoneNumber chineseMobile = PhoneNumber.of("13800138000");
        PhoneNumber northAmerican = PhoneNumber.of("+18005550199");
        PhoneNumber ukNumber = PhoneNumber.of("+447911123456");

        // Then
        assertThat(chineseMobile.toInternationalFormat())
                .isEqualTo("+86 138 0013 8000");

        assertThat(chineseMobile.toLocalFormat())
                .isEqualTo("138-0013-8000");

        assertThat(northAmerican.toInternationalFormat())
                .isEqualTo("+1 (800) 555-0199");

        assertThat(northAmerican.toLocalFormat())
                .isEqualTo("(800) 555-0199");
    }

    @Test
    void shouldMaskPhoneNumber() {
        // Given
        PhoneNumber phoneNumber = PhoneNumber.of("13800138000");

        // When
        String masked = phoneNumber.toMaskedFormat();

        // Then
        assertThat(masked).isEqualTo("+86 138****8000");
    }

    @Test
    void shouldIdentifyOperator() {
        // 中国移动
        assertThat(PhoneNumber.of("13800138000").getOperator())
                .isEqualTo("China Mobile");

        // 中国联通
        assertThat(PhoneNumber.of("13000138000").getOperator())
                .isEqualTo("China Unicom");

        // 中国电信
        assertThat(PhoneNumber.of("13300138000").getOperator())
                .isEqualTo("China Telecom");

        // 虚拟运营商
        assertThat(PhoneNumber.of("17000138000").getOperator())
                .isEqualTo("Other");
    }

    @Test
    void shouldHaveValueObjectProperties() {
        // Given
        PhoneNumber phone1 = PhoneNumber.of("13800138000");
        PhoneNumber phone2 = PhoneNumber.of("+8613800138000");
        PhoneNumber phone3 = PhoneNumber.of("13800138001");

        // Then - 基于值的相等性
        assertThat(phone1.equals(phone2)).isTrue();
        assertThat(phone1).isNotEqualTo(phone3);
    }

    //    @ParameterizedTest
    //    @CsvSource({
    //            "+8613800138000, +86 138 0013 8000",
    //            "+18005550199, +1 (800) 555-0199",
    //            "+447911123456, +44 7911 123456"
    //    })
    //    void shouldFormatInternationalNumbers(String input, String expected) {
    //        PhoneNumber phone = PhoneNumber.of(input);
    //        assertThat(phone.toInternationalFormat()).isEqualTo(expected);
    //    }

    @Test
    void shouldComparePhoneNumbers() {
        PhoneNumber phone1 = PhoneNumber.of("13800138000");
        PhoneNumber phone2 = PhoneNumber.of("13800138001");
        PhoneNumber phone3 = PhoneNumber.of("13900138000");

        assertThat(phone1.compareTo(phone2)).isNegative();
        assertThat(phone2.compareTo(phone1)).isPositive();
        assertThat(phone1.compareTo(PhoneNumber.of("13800138000"))).isZero();
        assertThat(phone1.compareTo(phone3)).isNegative(); // 138 < 139
    }

    @Test
    void testExtractLocalNumberCorrectly() {
        // 测试用例：各种格式的电话号码

        // 1. E.164格式
        PhoneNumber p1 = PhoneNumber.of("+8613800138000");
        assertThat(p1.countryCode()).isEqualTo("+86");
        assertThat(p1.localNumber()).isEqualTo("13800138000");
        assertThat(p1.toE164()).isEqualTo("+8613800138000");

        // 2. 中国手机号（无国家代码）
        PhoneNumber p2 = PhoneNumber.of("13800138000");
        assertThat(p2.countryCode()).isEqualTo("+86");
        assertThat(p2.localNumber()).isEqualTo("13800138000");
        assertThat(p2.toE164()).isEqualTo("+8613800138000");

        // 4. 北美号码
        PhoneNumber p4 = PhoneNumber.of("+18005550199");
        assertThat(p4.countryCode()).isEqualTo("+1");
        assertThat(p4.localNumber()).isEqualTo("8005550199");
        assertThat(p4.toE164()).isEqualTo("+18005550199");

        // 6. 带格式的号码
        PhoneNumber p6 = PhoneNumber.of("138-0013-8000");
        assertThat(p6.countryCode()).isEqualTo("+86");
        assertThat(p6.localNumber()).isEqualTo("13800138000");
        assertThat(p6.toE164()).isEqualTo("+8613800138000");

        // 7. 带空格的号码
        PhoneNumber p7 = PhoneNumber.of("138 0013 8000");
        assertThat(p7.countryCode()).isEqualTo("+86");
        assertThat(p7.localNumber()).isEqualTo("13800138000");
        assertThat(p7.toE164()).isEqualTo("+8613800138000");
    }

    @ParameterizedTest
    @CsvSource({
            // 输入格式, 期望国家代码, 期望本地号码
            "+8613800138000, +86, 13800138000",
            "13800138000, +86, 13800138000",
            "008613800138000, +86, 13800138000",
            "86-138-0013-8000, +86, 13800138000"
    })
    void testVariousFormats(String input, String expectedCountryCode, String expectedLocalNumber) {
        PhoneNumber phone = PhoneNumber.of(input);

        assertThat(phone.countryCode()).isEqualTo(expectedCountryCode);
        assertThat(phone.localNumber()).isEqualTo(expectedLocalNumber);
    }
}
