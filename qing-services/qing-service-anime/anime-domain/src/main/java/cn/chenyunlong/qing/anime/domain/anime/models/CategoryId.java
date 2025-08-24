package cn.chenyunlong.qing.anime.domain.anime.models;

import cn.chenyunlong.qing.domain.common.EntityId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;

/**
 * 动漫分类标识符
 *
 * <p>
 * 作为动漫分类的唯一标识，确保分类实体的身份识别
 * </p>
 *
 * <p>
 * 值对象特性：
 * </p>
 * <ul>
 * <li>不可变性：一旦创建，ID值不可更改</li>
 * <li>值相等性：基于ID值进行相等性比较</li>
 * <li>自验证：确保ID值的有效性</li>
 * </ul>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
public class CategoryId extends EntityId<Long> {

    /**
     * 分类的唯一标识符
     */
    @Getter
    @NotNull
    Long value;

    /**
     * 构造函数
     *
     * @param value 分类的唯一标识符
     * @throws IllegalArgumentException 当ID无效时
     */
    private CategoryId(@NonNull Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("分类ID必须大于0");
        }
        this.value = value;
    }

    /**
     * 创建分类ID的工厂方法
     *
     * @param value 分类的唯一标识符
     * @return 分类ID实例
     * @throws IllegalArgumentException 当ID无效时
     */
    public static CategoryId of(Long value) {
        return new CategoryId(value);
    }

    /**
     * 创建分类ID的工厂方法（字符串形式）
     *
     * @param id 分类的唯一标识符字符串
     * @return 分类ID实例
     * @throws IllegalArgumentException 当ID无效时
     * @throws NumberFormatException    当字符串无法转换为数字时
     */
    public static CategoryId of(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("分类ID字符串不能为空");
        }
        try {
            return new CategoryId(Long.parseLong(id.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的分类ID格式: " + id, e);
        }
    }
}
