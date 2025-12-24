package cn.chenyunlong.qing.auth.domain.user.valueObject;

import lombok.Value;

@Value
public class RawPassword {
    String value;

    private RawPassword(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (value.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (!value.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!value.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!value.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        this.value = value;
    }

    public static RawPassword of(String password) {
        return new RawPassword(password);
    }

    public boolean isOk() {
        return true;
    }
}
