package cn.chenyunlong.jpa.support;

import cn.chenyunlong.common.exception.ValidationException;
import cn.chenyunlong.common.model.ValidateResult;
import cn.chenyunlong.common.validator.ValidateGroup;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基础JPA处理服务。
 * 服务尚未定义。
 */
@Slf4j
public abstract class BaseJpaService {

    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public BaseJpaService() {
    }

    public <T extends BaseEntity, ID> EntityUpdater<T, ID> doUpdate(BaseRepository<T, ID> repository) {
        return new EntityUpdater<>(repository);
    }

    public <T extends BaseEntity, ID> EntityCreator<T, ID> doCreate(BaseRepository<T, ID> repository) {
        return new EntityCreator<>(repository);
    }

    private <T> void doValidate(T t, Class<? extends ValidateGroup> group) {
        Set<ConstraintViolation<T>>
            constraintViolations = validator.validate(t, group, Default.class);
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            List<ValidateResult> results = constraintViolations.stream()
                .map((cv) -> new ValidateResult(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
            throw new ValidationException(results);
        }
    }
}
