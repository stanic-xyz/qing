package cn.chenyunlong.jpa.support;

import cn.chenyunlong.common.exception.ValidationException;
import cn.chenyunlong.common.model.ValidateResult;
import cn.chenyunlong.common.validator.CreateGroup;
import cn.chenyunlong.common.validator.UpdateGroup;
import cn.chenyunlong.common.validator.ValidateGroup;
import com.google.common.base.Preconditions;
import io.vavr.control.Try;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.CollectionUtils;

/**
 * 基础JPA处理服务。
 * 服务尚未定义。
 */
@SuppressWarnings("unused")
public abstract class BaseJpaService {
    private static final Logger log = LoggerFactory.getLogger(BaseJpaService.class);
    static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public BaseJpaService() {
    }

    public <T, ID> Updater<T, ID> doUpdate(CrudRepository<T, ID> repository) {
        return new Updater<>(repository);
    }

    public <T, ID> Creator<T, ID> doCreate(CrudRepository<T, ID> repository) {
        return new Creator<>(repository);
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

    /**
     * 创造者。
     * 提供领域对象的创建功能
     *
     * @param <T>  创建着类型
     * @param <ID> 创建者ID
     */
    public class Creator<T, ID> {
        private final CrudRepository<T, ID> repository;
        private T typeClazz;
        private Consumer<T> successCallback = (t) -> BaseJpaService.log.info("save success");
        private Consumer<? super Throwable> errorCallback = Throwable::printStackTrace;

        public Creator(CrudRepository<T, ID> repository) {
            this.repository = repository;
        }

        /**
         * 创建服务。
         *
         * @param supplier 提供者
         * @return 创造者
         */
        public Creator<T, ID> create(Supplier<T> supplier) {
            this.typeClazz = supplier.get();
            return this;
        }

        /**
         * 初始化创建器。
         *
         * @param consumer 消费者
         * @return 创建器
         */
        public Creator<T, ID> init(Consumer<T> consumer) {
            Preconditions.checkArgument(Objects.nonNull(consumer), "entity must supply");
            consumer.accept(this.typeClazz);
            return this;
        }

        /**
         * 成功回调。
         *
         * @param consumer 消费者
         */
        public Creator<T, ID> successCallback(Consumer<T> consumer) {
            this.successCallback = consumer;
            return this;
        }

        /**
         * 异常回调。
         *
         * @param consumer 消费者
         */
        public Creator<T, ID> errorCallback(Consumer<? super Throwable> consumer) {
            this.errorCallback = consumer;
            return this;
        }

        /**
         * 执行。
         */
        public Optional<T> execute() {
            BaseJpaService.this.doValidate(this.typeClazz, CreateGroup.class);
            T save =
                Try.of(() -> this.repository.save(this.typeClazz)).onSuccess(this.successCallback)
                    .onFailure(this.errorCallback).getOrNull();
            return Optional.ofNullable(save);
        }
    }

    public class Updater<T, ID> {
        private final CrudRepository<T, ID> repository;
        private T entity;
        private Consumer<T> successCallback;
        private Consumer<? super Throwable> errorCallback;

        private Updater(CrudRepository<T, ID> repository) {
            this.successCallback = (t) -> BaseJpaService.log.info(t.toString());
            this.errorCallback = (e) -> BaseJpaService.log.error(e.getMessage());
            this.repository = repository;
        }

        /**
         * 根据ID加载实体类。
         *
         * @param id 编号
         */
        public Updater<T, ID> loadById(ID id) {
            Preconditions.checkArgument(Objects.nonNull(id), "id is null");
            Optional<T> loadEntity = this.repository.findById(id);
            this.entity = loadEntity.orElse(this.entity);
            return this;
        }

        public Updater<T, ID> load(Supplier<T> supplier) {
            this.entity = supplier.get();
            return this;
        }

        /**
         * 更新服务。
         *
         * @param consumer 消费者。
         * @return 更新器。
         */
        public Updater<T, ID> update(Consumer<T> consumer) {
            Preconditions.checkArgument(Objects.nonNull(this.entity), "entity is null");
            consumer.accept(this.entity);
            return this;
        }

        public Updater<T, ID> sucCallback(Consumer<T> consumer) {
            this.successCallback = consumer;
            return this;
        }

        public Updater<T, ID> errorCallback(Consumer<? super Throwable> consumer) {
            this.errorCallback = consumer;
            return this;
        }

        /**
         * 执行服务。
         *
         * @return 执行结果
         */
        public Optional<T> execute() {
            BaseJpaService.this.doValidate(this.entity, UpdateGroup.class);
            T save = Try.of(() ->
                    this.repository.save(this.entity))
                .onSuccess(this.successCallback)
                .onFailure(this.errorCallback).getOrNull();
            return Optional.ofNullable(save);
        }
    }
}
