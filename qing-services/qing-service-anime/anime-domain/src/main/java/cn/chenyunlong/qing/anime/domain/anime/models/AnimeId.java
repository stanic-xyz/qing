package cn.chenyunlong.qing.anime.domain.anime.models;

import cn.chenyunlong.qing.domain.common.EntityId;
import lombok.Getter;
import lombok.NonNull;

/**
 * 动漫聚合根标识符
 *
 * <p>
 * 作为动漫聚合的唯一标识，确保动漫实体的身份识别
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
@Getter
public class AnimeId extends EntityId<Long> {

    /**
     * 动漫的唯一标识符
     */
    @NonNull
    Long value;

    /**
     * 构造函数
     *
     * @param value 动漫的唯一标识符
     * @throws IllegalArgumentException 当ID无效时
     */
    private AnimeId(@NonNull Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("动漫ID必须大于0");
        }
        this.value = value;
    }

    /**
     * 创建动漫ID的工厂方法
     *
     * @param value 动漫的唯一标识符
     * @return 动漫ID实例
     * @throws IllegalArgumentException 当ID无效时
     */
    public static AnimeId of(Long value) {
        return new AnimeId(value);
    }

    /**
     * 创建动漫ID的工厂方法（字符串形式）
     *
     * @param id 动漫的唯一标识符字符串
     * @return 动漫ID实例
     * @throws IllegalArgumentException 当ID无效时
     * @throws NumberFormatException    当字符串无法转换为数字时
     */
    public static AnimeId of(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("动漫ID字符串不能为空");
        }
        try {
            return new AnimeId(Long.parseLong(id.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的动漫ID格式: " + id, e);
        }
    }
}
