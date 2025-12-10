package cn.chenyunlong.qing.domain.common;

import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.NotImplementedException;

import java.io.Serializable;
import java.util.Objects;

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
public interface Identifiable<T extends Serializable> {

    /**
     * 获取ID值
     *
     * @return ID值
     */
    T id();

    default Identifiable<T> ofValue(T id) {
        throw new NotImplementedException();
    }

    // 提供equals的模板方法
    default boolean identifiableEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().isInstance(obj)) {
            return false;
        }

        Identifiable<?> other = (Identifiable<?>) obj;

        // 使用Objects.equals处理null值
        return Objects.equals(id(), other.id());
    }

    // 提供hashCode的模板方法
    default int identifiableHashCode() {
        T id = id();
        return id != null ? id.hashCode() : 0;
    }

    static Long simpleGenerate() {
        return IdUtil.getSnowflakeNextId();
    }
}
