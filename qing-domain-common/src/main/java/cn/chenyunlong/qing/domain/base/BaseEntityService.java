package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.common.exception.ValidationException;
import cn.chenyunlong.common.model.ValidateResult;
import cn.chenyunlong.common.validator.ValidateGroup;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;
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
public abstract class BaseEntityService<Aggregate extends BaseAggregate, ID extends AggregateId> {

    private static final Validator validator;

    private BaseRepository<Aggregate, ID> baseRepository;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public BaseEntityService() {
    }

    public <T extends BaseAggregate, ID extends AggregateId> EntityUpdater<T, ID> doUpdate(BaseRepository<T, ID> repository) {
        return new EntityUpdater<>(repository);
    }

    public <T extends BaseAggregate, ID extends AggregateId> EntityCreator<T, ID> doCreate(BaseRepository<T, ID> repository) {
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
