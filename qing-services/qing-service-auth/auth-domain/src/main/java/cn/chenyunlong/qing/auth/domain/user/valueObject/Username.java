package cn.chenyunlong.qing.auth.domain.user.valueObject;

import cn.chenyunlong.qing.domain.common.ValueObject;

import java.util.regex.Pattern;

/**
 * 用户名值对象
 */
public record Username(String value) implements ValueObject {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,50}$");

    public Username(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (!USERNAME_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("用户名格式不正确");
        }
        if (Character.isDigit(value.charAt(0))) {
            throw new IllegalArgumentException("用户名不能以数字开头");
        }
        this.value = value.trim();
    }

    public static Username of(String value) {
        return new Username(value);
    }
}
