package cn.chenyunlong.qing.anime.domain.type;

import cn.chenyunlong.qing.domain.common.Identifiable;
import lombok.NonNull;

/**
 * 动漫类型标识符
 *
 * <p>作为动漫类型的唯一标识，确保类型实体的身份识别</p>
 *
 * <p>值对象特性：</p>
 * <ul>
 *   <li>不可变性：一旦创建，ID值不可更改</li>
 *   <li>值相等性：基于ID值进行相等性比较</li>
 *   <li>自验证：确保ID值的有效性</li>
 * </ul>
 *
 * @param value 类型的唯一标识符
 * @author chenyunlong
 * @since 1.0.0
 */
public record TypeId(Long value) implements Identifiable<Long> {

    @Override
    public Long id() {
        return value;
    }

    /**
     * 私有构造函数，强制使用工厂方法
     */
    public TypeId(@NonNull Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Type ID must be positive");
        }
        this.value = value;
    }

    /**
     * 创建类型ID
     *
     * @param value ID值
     * @return 类型ID实例
     * @throws IllegalArgumentException 如果ID值无效
     */
    public static TypeId of(@NonNull Long value) {
        return new TypeId(value);
    }

    /**
     * 从字符串创建类型ID
     *
     * @param id 字符串形式的ID
     * @return 类型ID实例
     * @throws IllegalArgumentException 如果ID格式无效
     */
    public static TypeId of(@NonNull String id) {
        try {
            return new TypeId(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid type ID format: " + id, e);
        }
    }
}
