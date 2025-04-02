package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.common.validator.CreateGroup;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class DefaultCustomValidator<T> implements CustomValidator<T> {

    private Validator validator;


    public static <T> CustomValidator<T> defaultValidator() {

        DefaultCustomValidator<T> customValidator = new DefaultCustomValidator<>();

        try {
            Validator validator1 = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
            if (validator1 != null) {
                customValidator.setValidator(validator1);
                return customValidator;
            }
        } catch (Exception e) {
            log.info("配置校验器失败！", e);
        }
        return null;
    }

    @Override
    public void doValidate(T data, Class<CreateGroup> createGroupClass) {

    }
}
