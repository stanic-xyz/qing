package cn.chenyunlong.common.validator;

import lombok.Getter;

/**
 * 校验结果
 */
@Getter
public class ValidationResult {
    private final boolean valid;
    private final String message;
    private final String code;

    private ValidationResult(boolean valid, String message, String code) {
        this.valid = valid;
        this.message = message;
        this.code = code;
    }

    public static ValidationResult valid() {
        return new ValidationResult(true, null, null);
    }

    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, message, null);
    }

    public static ValidationResult success() {
        return new ValidationResult(true, null, null);
    }

    public static ValidationResult failure(String message, String code) {
        return new ValidationResult(false, message, code);
    }

}
