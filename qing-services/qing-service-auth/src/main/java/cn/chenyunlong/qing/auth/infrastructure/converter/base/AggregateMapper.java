package cn.chenyunlong.qing.auth.infrastructure.converter.base;

import cn.chenyunlong.jpa.support.BaseJpaEntity;
import cn.chenyunlong.qing.domain.common.AuditInfo;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import cn.chenyunlong.qing.domain.common.Identifiable;
import cn.chenyunlong.qing.infrastructure.entity.BaseEntity;
import org.mapstruct.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 聚合映射器 - 提供EntityId与基础类型之间的通用转换
 *
 * @author chenyunlong
 * @since 2024/12/24 23:47
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public class AggregateMapper {

    private static final ConcurrentMap<Class<?>, Method> OF_METHOD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();

    public AggregateMapper() {}

    /**
     * 获取Long类型ID值
     *
     * @param id 实体ID
     * @return ID值
     */
    @Named("getLongId")
    public Long getLongId(Identifiable<Long> id) {
        return id != null ? id.id() : null;
    }

    /**
     * 获取String类型ID值
     *
     * @param id 实体ID
     * @return ID值
     */
    @Named("getStringId")
    public String getStringId(Identifiable<String> id) {
        return id != null ? id.id() : null;
    }

    /**
     * 获取Integer类型ID值
     *
     * @param id 实体ID
     * @return ID值
     */
    @Named("getIntegerId")
    public Integer getIntegerId(Identifiable<Integer> id) {
        return id != null ? id.id() : null;
    }

    /**
     * 获取EntityId的值
     *
     * @param id 实体ID
     * @return ID值
     */
    @Named("getValue")
    public Object getValue(Identifiable<?> id) {
        return id != null ? id.id() : null;
    }

    public Long toIdentifierId(Identifiable<Long> id) {
        return id != null ? id.id() : null;
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
    public Identifiable<?> createEntityId(Object value, Class<?> entityIdClass) {
        if (value == null) {
            return null;
        }

        try {
            // 尝试使用缓存的of方法
            Class<?> valueClass = value.getClass();
            Method ofMethod = getOfMethod(entityIdClass, valueClass);

            if (ofMethod != null) {
                return (Identifiable<?>) ofMethod.invoke(null, value);
            }

            // 如果没有of方法，尝试使用构造函数
            Constructor<?> constructor = getConstructor(entityIdClass, valueClass);

            if (constructor != null) {
                return (Identifiable<?>) constructor.newInstance(value);
            }

            throw new IllegalArgumentException("无法为类 " + entityIdClass.getName() + " 创建实例");

        } catch (Exception e) {
            throw new RuntimeException("创建EntityId实例失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取of方法
     */
    private Method getOfMethod(Class<?> entityIdClass, Class<?> valueClass) {
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
    private Constructor<?> getConstructor(Class<?> entityIdClass, Class<?> valueClass) {
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

    Long mapDomainId2Value(Identifiable<Long> id) {
        return id != null ? id.id() : null;
    }

    /**
     * 将 BaseJpaEntity 的审计字段映射到 BaseSimpleBusinessEntity 的 AuditInfo
     */
    @AfterMapping
    public void mapAuditFields(BaseJpaEntity source, @MappingTarget BaseSimpleBusinessEntity<?> target) {
        if (source == null) return;
        // 创建或设置 AuditInfo
        AuditInfo auditInfo = target.getAuditInfo();
        if (auditInfo == null) {
            auditInfo = AuditInfo.restore(source.getCreateBy(), source.getCreatedAt(), source.getUpdatedBy(), source.getUpdatedAt());
            target.setAuditInfo(auditInfo);
        }
        target.setVersion(source.getVersion());
        // 如果有 validStatus 需要映射
        target.setValidStatus(source.getValidStatus());
    }

    /**
     * 反向映射：将 BaseSimpleBusinessEntity 的 AuditInfo 映射到 BaseJpaEntity
     */
    @AfterMapping
    public void mapAuditFieldsReverse(BaseSimpleBusinessEntity<?> anySource, @MappingTarget BaseJpaEntity target) {
        if (anySource == null) return;
        AuditInfo auditInfo = anySource.getAuditInfo();
        if (auditInfo != null) {
            target.setCreatedAt(auditInfo.createdAt());
            target.setCreateBy(auditInfo.createdBy());
            target.setUpdatedAt(auditInfo.updatedAt());
            target.setUpdatedBy(auditInfo.updatedBy());
        }
        target.setVersion(anySource.getVersion());
    }
}
