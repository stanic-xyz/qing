package cn.chenyunlong.qing.anime.domain.anime.event;

import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.domain.anime.models.Tags;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * 动漫创建事件
 *
 * <p>当动漫成功创建时发布的领域事件</p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Getter
public class AnimeCreatedEvent extends ApplicationEvent {

    /**
     * 动漫ID
     */
    private final AnimeId animeId;

    /**
     * 动漫名称
     */
    private final String animeName;

    /**
     * 分类ID
     */
    private final Long categoryId;

    /**
     * 标签
     */
    private final Tags tags;

    /**
     * 播放状态
     */
    private final String playStatus;

    /**
     * 创建时间
     */
    private final LocalDateTime createTime;

    /**
     * 创建者ID
     */
    private final Long createdBy;

    /**
     * 构造函数
     *
     * @param source     事件源
     * @param animeId    动漫ID
     * @param animeName  动漫名称
     * @param categoryId 分类ID
     * @param tags       标签
     * @param playStatus 播放状态
     * @param createTime 创建时间
     * @param createdBy  创建者ID
     */
    public AnimeCreatedEvent(Object source, AnimeId animeId, String animeName,
                             Long categoryId, Tags tags, String playStatus,
                             LocalDateTime createTime, Long createdBy) {
        super(source);
        this.animeId = animeId;
        this.animeName = animeName;
        this.categoryId = categoryId;
        this.tags = tags;
        this.playStatus = playStatus;
        this.createTime = createTime;
        this.createdBy = createdBy;
    }

    /**
     * 创建动漫创建事件
     *
     * @param source     事件源
     * @param animeId    动漫ID
     * @param animeName  动漫名称
     * @param categoryId 分类ID
     * @param tags       标签
     * @param playStatus 播放状态
     * @param createTime 创建时间
     * @param createdBy  创建者ID
     * @return 动漫创建事件
     */
    public static AnimeCreatedEvent of(Object source, AnimeId animeId, String animeName,
                                       Long categoryId, Tags tags, String playStatus,
                                       LocalDateTime createTime, Long createdBy) {
        return new AnimeCreatedEvent(source, animeId, animeName, categoryId,
                tags, playStatus, createTime, createdBy);
    }

    @Override
    public String toString() {
        return String.format("AnimeCreatedEvent{animeId=%s, animeName='%s', categoryId=%d, createTime=%s}",
                animeId.getValue(), animeName, categoryId, createTime);
    }
}
