package cn.chenyunlong.qing.anime.domain.playback;

import cn.chenyunlong.qing.domain.common.EntityId;
import lombok.Getter;
import lombok.NonNull;

/**
 * 播放进度标识符
 *
 * <p>作为播放进度的唯一标识，确保播放进度实体的身份识别</p>
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
public class PlaybackProgressId extends EntityId<Long> {

    /**
     * 播放进度的唯一标识符
     */
    @NonNull
    Long value;

    /**
     * 私有构造函数，确保只能通过工厂方法创建
     *
     * @param value ID值
     */
    private PlaybackProgressId(@NonNull Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("播放进度ID必须为正数");
        }
        this.value = value;
    }

    /**
     * 创建播放进度ID
     *
     * @param value ID值
     * @return 播放进度ID实例
     */
    public static PlaybackProgressId of(@NonNull Long value) {
        return new PlaybackProgressId(value);
    }

    /**
     * 从字符串创建播放进度ID
     *
     * @param id 字符串形式的ID
     * @return 播放进度ID实例
     */
    public static PlaybackProgressId of(@NonNull String id) {
        try {
            return new PlaybackProgressId(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的播放进度ID格式: " + id, e);
        }
    }
}
