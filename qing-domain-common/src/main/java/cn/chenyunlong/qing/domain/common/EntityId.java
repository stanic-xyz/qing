package cn.chenyunlong.qing.domain.common;

import java.io.Serializable;

/**
 * 实体ID接口
 *
 * <p>
 * 所有实体标识符的基础接口，提供类型安全的ID访问
 * </p>
 *
 * @param <T> ID的类型
 * @author chenyunlong
 * @since 1.0.0
 */
public abstract class EntityId<T extends Serializable> {

    /**
     * 获取ID值
     *
     * @return ID值
     */
    public abstract T getValue();

    /**
     * 获取ID的字符串表示
     *
     * @return ID的字符串形式
     */
    String asString() {
        T value = getValue();
        return value != null ? value.toString() : null;
    }

    /**
     * 创建相同类型的EntityId实例
     * 子类需要重写此方法以提供正确的实例创建逻辑
     *
     * @param value ID值
     * @return 新的EntityId实例
     */
    public EntityId<T> ofValue(T value) {
        throw new UnsupportedOperationException("Subclass must implement ofValue method");
    }
}
