package cn.chenyunlong.qing.auth.domain.user.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordConsistency {

    String message() default "{javax.validation.constraints.password.consistency.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
