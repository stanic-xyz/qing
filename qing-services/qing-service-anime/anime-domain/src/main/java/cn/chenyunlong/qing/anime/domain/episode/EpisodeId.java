package cn.chenyunlong.qing.anime.domain.episode;

import cn.chenyunlong.qing.domain.common.Identifiable;
import lombok.Getter;
import lombok.NonNull;

/**
 * 单集标识符
 *
 * <p>
 * 作为单集的唯一标识，确保单集实体的身份识别
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
public class EpisodeId implements Identifiable<Long> {

    @Override
    public Long id() {
        return value;
    }

    /**
     * 单集的唯一标识符
     */
    Long value;

    /**
     * 私有构造函数，强制使用工厂方法
     */
    private EpisodeId(@NonNull Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Episode ID must be positive");
        }
        this.value = value;
    }

    /**
     * 创建单集ID
     *
     * @param value ID值
     * @return 单集ID实例
     * @throws IllegalArgumentException 如果ID值无效
     */
    public static EpisodeId of(@NonNull Long value) {
        return new EpisodeId(value);
    }

    /**
     * 从字符串创建单集ID
     *
     * @param id 字符串形式的ID
     * @return 单集ID实例
     * @throws IllegalArgumentException 如果ID格式无效
     */
    public static EpisodeId of(@NonNull String id) {
        try {
            return new EpisodeId(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid episode ID format: " + id, e);
        }
    }
}
