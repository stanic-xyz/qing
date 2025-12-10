package cn.chenyunlong.qing.domain.common;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.ValidateResult;
import cn.chenyunlong.qing.domain.common.exception.ValidationException;
import cn.hutool.core.collection.CollUtil;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * 简单实体基类，支持审计和有效性状态管理
 * 注意：这个基类主要用于技术层面的统一，业务实体可以选择性继承
 */
public abstract class BaseSimpleBusinessEntity<ID> extends AbstractAggregateRoot<BaseSimpleBusinessEntity<ID>>
        implements Auditable, Validatable, Entity<ID> {

    @Setter
    protected ID id;
    protected Integer version = 0;
    protected AuditInfo auditInfo;
    protected ValidStatus validStatus;

    // 构造器
    protected BaseSimpleBusinessEntity() {
        // 子类可以在构造器中调用init()
    }

    protected BaseSimpleBusinessEntity(ID id) {
        this.id = id;
    }

    // Entity 接口实现
    @Override
    public ID getId() {
        return id;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    // Auditable 接口实现
    @Override
    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    @Override
    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    // Validatable 接口实现
    @Override
    public ValidStatus getValidStatus() {
        return validStatus;
    }

    @Override
    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    // 增强的状态管理方法

    /**
     * 初始化实体状态
     * 设置状态为 INITIAL，并创建审计信息
     */
    public void init() {
        this.validStatus = ValidStatus.INITIAL;
        if (this.auditInfo == null) {
            this.auditInfo = AuditInfo.createSystem();
        }
    }

    /**
     * 设置为有效状态，带审计更新
     */
    public void valid(String updatedBy) {
        if (canBeValid()) {
            this.validStatus = ValidStatus.VALID;
            updateAudit(updatedBy);
        } else {
            throw new IllegalStateException(
                    String.format("Cannot set valid status from current status: %s", validStatus)
            );
        }
    }

    /**
     * 设置为无效状态，带审计更新
     */
    public void invalid(String updatedBy) {
        if (canBeInvalid()) {
            this.validStatus = ValidStatus.INVALID;
            updateAudit(updatedBy);
        } else {
            throw new IllegalStateException(
                    String.format("Cannot set invalid status from current status: %s", validStatus)
            );
        }
    }

    /**
     * 带验证的有效性设置
     */
    public void validWithCheck(String updatedBy, Predicate<BaseSimpleBusinessEntity<ID>> validation) {
        if (validation.test(this)) {
            valid(updatedBy);
        } else {
            throw new ValidationException(CollUtil.toList(new ValidateResult("all", "Entity validation failed, cannot set to valid")));
        }
    }

    // 审计辅助方法
    protected void updateAudit(String updatedBy) {
        if (this.auditInfo != null) {
            this.auditInfo = this.auditInfo.update(updatedBy);
        } else {
            this.auditInfo = AuditInfo.create(updatedBy);
        }
    }

    // 业务逻辑方法

    /**
     * 检查实体是否可用于业务操作
     */
    public boolean isAvailable() {
        return ValidStatus.VALID.equals(validStatus);
    }

    /**
     * 实体激活（从无效恢复到有效）
     */
    public void activate(String updatedBy) {
        if (ValidStatus.INVALID.equals(validStatus)) {
            valid(updatedBy);
        }
    }

    /**
     * 实体停用（从有效变为无效）
     */
    public void deactivate(String updatedBy) {
        if (ValidStatus.VALID.equals(validStatus)) {
            invalid(updatedBy);
        }
    }

    // 相等性和哈希码
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BaseSimpleBusinessEntity<?> that = (BaseSimpleBusinessEntity<?>) obj;

        if (id == null || that.id == null) {
            return super.equals(obj);
        }

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return getClass().hashCode() + id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, status=%s]",
                getClass().getSimpleName(), id, validStatus);
    }

    // 静态工厂方法

    /**
     * 创建已初始化的实体模板方法
     */
    protected static <T extends BaseSimpleBusinessEntity<?>> T createInitialized(T entity) {
        entity.init();
        return entity;
    }

    @Override
    public Collection<Object> domainEvents() {
        return super.domainEvents();
    }
}
