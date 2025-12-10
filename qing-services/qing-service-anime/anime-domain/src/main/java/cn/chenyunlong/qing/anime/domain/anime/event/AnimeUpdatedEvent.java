package cn.chenyunlong.qing.anime.domain.anime.event;

import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.domain.anime.models.Tags;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * 动漫更新事件
 *
 * <p>当动漫信息更新时发布的领域事件</p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Getter
public class AnimeUpdatedEvent extends ApplicationEvent {

    /**
     * 动漫ID
     */
    private final AnimeId animeId;

    /**
     * 动漫名称
     */
    private final String animeName;

    /**
     * 更新类型
     */
    private final UpdateType updateType;

    /**
     * 更新前的值
     */
    private final Object oldValue;

    /**
     * 更新后的值
     */
    private final Object newValue;

    /**
     * 更新时间
     */
    private final LocalDateTime updateTime;

    /**
     * 更新者ID
     */
    private final Long updatedBy;

    /**
     * 更新类型枚举
     */
    public enum UpdateType {
        /**
         * 基本信息更新
         */
        BASIC_INFO,

        /**
         * 详细信息更新
         */
        DETAIL_INFO,

        /**
         * 标签更新
         */
        TAGS,

        /**
         * 播放状态更新
         */
        PLAY_STATUS
    }

    /**
     * 构造函数
     *
     * @param source 事件源
     * @param animeId 动漫ID
     * @param animeName 动漫名称
     * @param updateType 更新类型
     * @param oldValue 更新前的值
     * @param newValue 更新后的值
     * @param updateTime 更新时间
     * @param updatedBy 更新者ID
     */
    public AnimeUpdatedEvent(Object source, AnimeId animeId, String animeName,
                           UpdateType updateType, Object oldValue, Object newValue,
                           LocalDateTime updateTime, Long updatedBy) {
        super(source);
        this.animeId = animeId;
        this.animeName = animeName;
        this.updateType = updateType;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.updateTime = updateTime;
        this.updatedBy = updatedBy;
    }

    /**
     * 创建基本信息更新事件
     *
     * @param source 事件源
     * @param animeId 动漫ID
     * @param animeName 动漫名称
     * @param oldName 旧名称
     * @param newName 新名称
     * @param updateTime 更新时间
     * @param updatedBy 更新者ID
     * @return 动漫更新事件
     */
    public static AnimeUpdatedEvent basicInfoUpdated(Object source, AnimeId animeId, String animeName,
                                                    String oldName, String newName,
                                                    LocalDateTime updateTime, Long updatedBy) {
        return new AnimeUpdatedEvent(source, animeId, animeName, UpdateType.BASIC_INFO,
                                   oldName, newName, updateTime, updatedBy);
    }

    /**
     * 创建详细信息更新事件
     *
     * @param source 事件源
     * @param animeId 动漫ID
     * @param animeName 动漫名称
     * @param updateTime 更新时间
     * @param updatedBy 更新者ID
     * @return 动漫更新事件
     */
    public static AnimeUpdatedEvent detailInfoUpdated(Object source, AnimeId animeId, String animeName,
                                                     LocalDateTime updateTime, Long updatedBy) {
        return new AnimeUpdatedEvent(source, animeId, animeName, UpdateType.DETAIL_INFO,
                                   null, null, updateTime, updatedBy);
    }

    /**
     * 创建标签更新事件
     *
     * @param source 事件源
     * @param animeId 动漫ID
     * @param animeName 动漫名称
     * @param oldTags 旧标签
     * @param newTags 新标签
     * @param updateTime 更新时间
     * @param updatedBy 更新者ID
     * @return 动漫更新事件
     */
    public static AnimeUpdatedEvent tagsUpdated(Object source, AnimeId animeId, String animeName,
                                               Tags oldTags, Tags newTags,
                                               LocalDateTime updateTime, Long updatedBy) {
        return new AnimeUpdatedEvent(source, animeId, animeName, UpdateType.TAGS,
                                   oldTags, newTags, updateTime, updatedBy);
    }

    /**
     * 创建播放状态更新事件
     *
     * @param source 事件源
     * @param animeId 动漫ID
     * @param animeName 动漫名称
     * @param oldStatus 旧状态
     * @param newStatus 新状态
     * @param updateTime 更新时间
     * @param updatedBy 更新者ID
     * @return 动漫更新事件
     */
    public static AnimeUpdatedEvent playStatusUpdated(Object source, AnimeId animeId, String animeName,
                                                     String oldStatus, String newStatus,
                                                     LocalDateTime updateTime, Long updatedBy) {
        return new AnimeUpdatedEvent(source, animeId, animeName, UpdateType.PLAY_STATUS,
                                   oldStatus, newStatus, updateTime, updatedBy);
    }

    @Override
    public String toString() {
        return String.format("AnimeUpdatedEvent{animeId=%s, animeName='%s', updateType=%s, updateTime=%s}",
                animeId.id(), animeName, updateType, updateTime);
    }
}
