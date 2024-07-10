package cn.chenyunlong.qing.domain.auth.user.dto.validator;


import cn.chenyunlong.qing.domain.auth.user.dto.request.RegisterPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordValidator
    implements ConstraintValidator<PasswordConsistency, RegisterPassword> {

    @Override
    public boolean isValid(RegisterPassword password,
        ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return true;
        }
        if (password.getPassword() == null) {
            return true;
        }
        return password.getPassword().equals(password.getConfirmPass());
    }
}
