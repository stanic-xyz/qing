package cn.chenyunlong.qing.auth.domain.user.valueObject;

import cn.chenyunlong.qing.domain.common.ValueObject;

import java.util.regex.Pattern;

/**
 * 邮箱值对象
 */
public record Email(String value) implements ValueObject {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }
        this.value = value.trim().toLowerCase();
    }

    public static Email of(String value) {
        return new Email(value);
    }

    // equals, hashCode, toString 方法...
}
