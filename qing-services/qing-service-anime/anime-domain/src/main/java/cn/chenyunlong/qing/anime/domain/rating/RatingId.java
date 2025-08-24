package cn.chenyunlong.qing.anime.domain.rating;

import cn.chenyunlong.qing.domain.common.EntityId;
import lombok.Getter;
import lombok.NonNull;

/**
 * 评分标识符
 *
 * <p>作为评分的唯一标识，确保评分实体的身份识别</p>
 *
 * <p>值对象特性：</p>
 * <ul>
 *   <li>不可变性：一旦创建，ID值不可更改</li>
 *   <li>值相等性：基于ID值进行相等性比较</li>
 *   <li>自验证：确保ID值的有效性</li>
 * </ul>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Getter
public class RatingId extends EntityId<Long> {

    /**
     * 评分的唯一标识符
     */
    @NonNull
    Long value;

    /**
     * 私有构造函数，确保只能通过工厂方法创建
     *
     * @param value ID值
     */
    private RatingId(@NonNull Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("评分ID必须为正数");
        }
        this.value = value;
    }

    /**
     * 创建评分ID
     *
     * @param value ID值
     * @return 评分ID实例
     */
    public static RatingId of(@NonNull Long value) {
        return new RatingId(value);
    }

    /**
     * 从字符串创建评分ID
     *
     * @param id 字符串形式的ID
     * @return 评分ID实例
     */
    public static RatingId of(@NonNull String id) {
        try {
            return new RatingId(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的评分ID格式: " + id, e);
        }
    }

    /**
     * 兼容旧API的方法
     *
     * @return ID值
     * @deprecated 使用 {@link #getValue()} 替代
     */
    @Deprecated
    public Long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RatingId(" + value + ")";
    }
}
