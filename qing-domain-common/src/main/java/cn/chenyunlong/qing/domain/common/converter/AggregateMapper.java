package cn.chenyunlong.qing.domain.common.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mapstruct.Named;

import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.EntityId;

/**
 * 聚合映射器 - 提供EntityId与基础类型之间的通用转换
 *
 * @author chenyunlong
 * @since 2024/12/24 23:47
 */
public class AggregateMapper {

    private static final ConcurrentMap<Class<?>, Method> OF_METHOD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();

    /**
     * 生成聚合ID
     *
     * @return 聚合ID
     */
    @Named("generate")
    public AggregateId generate() {
        return AggregateId.generate();
    }

    /**
     * 获取Long类型ID值
     *
     * @param entityId 实体ID
     * @return ID值
     */
    @Named("getLongId")
    public Long getLongId(EntityId<Long> entityId) {
        return entityId != null ? entityId.getValue() : null;
    }

    /**
     * 获取String类型ID值
     *
     * @param entityId 实体ID
     * @return ID值
     */
    @Named("getStringId")
    public String getStringId(EntityId<String> entityId) {
        return entityId != null ? entityId.getValue() : null;
    }

    /**
     * 获取Integer类型ID值
     *
     * @param entityId 实体ID
     * @return ID值
     */
    @Named("getIntegerId")
    public Integer getIntegerId(EntityId<Integer> entityId) {
        return entityId != null ? entityId.getValue() : null;
    }

    /**
     * 获取EntityId的值
     *
     * @param entityId 实体ID
     * @return ID值
     */
    @Named("getValue")
    public Object getValue(EntityId<?> entityId) {
        return entityId != null ? entityId.getValue() : null;
    }

    /**
     * 通过反射创建EntityId实例
     * 优先尝试使用静态of方法，如果没有则使用构造函数
     *
     * @param value         值
     * @param entityIdClass EntityId子类
     * @return EntityId实例
     */
    @Named("createEntityId")
    public EntityId<?> createEntityId(Object value, Class<? extends EntityId<?>> entityIdClass) {
        if (value == null) {
            return null;
        }

        try {
            // 尝试使用缓存的of方法
            Class<?> valueClass = value.getClass();
            Method ofMethod = getOfMethod(entityIdClass, valueClass);

            if (ofMethod != null) {
                return (EntityId<?>) ofMethod.invoke(null, value);
            }

            // 如果没有of方法，尝试使用构造函数
            Constructor<?> constructor = getConstructor(entityIdClass, valueClass);

            if (constructor != null) {
                return (EntityId<?>) constructor.newInstance(value);
            }

            throw new IllegalArgumentException("无法为类 " + entityIdClass.getName() + " 创建实例");

        } catch (Exception e) {
            throw new RuntimeException("创建EntityId实例失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取of方法
     */
    private Method getOfMethod(Class<? extends EntityId<?>> entityIdClass, Class<?> valueClass) {
        return OF_METHOD_CACHE.computeIfAbsent(entityIdClass, clazz -> {
            try {
                // 尝试具体类型的of方法
                return clazz.getMethod("of", valueClass);
            } catch (NoSuchMethodException e1) {
                try {
                    return clazz.getMethod("of", Object.class);
                } catch (NoSuchMethodException e2) {
                    return null;
                }
            }
        });
    }

    /**
     * 获取构造函数
     */
    private Constructor<?> getConstructor(Class<? extends EntityId<?>> entityIdClass, Class<?> valueClass) {
        return CONSTRUCTOR_CACHE.computeIfAbsent(entityIdClass, clazz -> {
            try {
                Constructor<?> ctor = clazz.getDeclaredConstructor(valueClass);
                ctor.setAccessible(true);
                return ctor;
            } catch (NoSuchMethodException e) {
                try {
                    // 尝试Object类型的构造函数
                    Constructor<?> objCtor = clazz.getDeclaredConstructor(Object.class);
                    objCtor.setAccessible(true);
                    return objCtor;
                } catch (NoSuchMethodException e2) {
                    return null;
                }
            }
        });
    }

}
