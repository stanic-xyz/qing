package cn.chenyunlong.qing.anime.domain.watchhistory;

import cn.chenyunlong.qing.domain.common.Identifiable;
import lombok.Getter;
import lombok.NonNull;

/**
 * 观看历史标识符
 *
 * <p>作为观看历史的唯一标识，确保观看历史实体的身份识别</p>
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
public class WatchHistoryId implements Identifiable<Long> {

    /**
     * 观看历史的唯一标识符
     */
    @NonNull
    Long value;

    /**
     * 构造函数
     *
     * @param value 观看历史的唯一标识符
     * @throws IllegalArgumentException 当ID无效时
     */
    private WatchHistoryId(@NonNull Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("观看历史ID必须大于0");
        }
        this.value = value;
    }

    /**
     * 创建观看历史ID的工厂方法
     *
     * @param value 观看历史的唯一标识符
     * @return 观看历史ID实例
     * @throws IllegalArgumentException 当ID无效时
     */
    public static WatchHistoryId of(Long value) {
        return new WatchHistoryId(value);
    }

    /**
     * 创建观看历史ID的工厂方法（字符串形式）
     *
     * @param id 观看历史的唯一标识符字符串
     * @return 观看历史ID实例
     * @throws IllegalArgumentException 当ID无效时
     * @throws NumberFormatException    当字符串无法转换为数字时
     */
    public static WatchHistoryId of(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("观看历史ID字符串不能为空");
        }
        try {
            return new WatchHistoryId(Long.parseLong(id.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的观看历史ID格式: " + id, e);
        }
    }

    /**
     * 获取ID值（兼容旧API）
     *
     * @return ID值
     * @deprecated 使用 {@link #id()} 替代
     */
    @Deprecated
    public Long id() {
        return value;
    }
}
