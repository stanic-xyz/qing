package cn.chenyunlong.qing.anime.domain.anime.event;

import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 动漫删除事件
 * 
 * <p>当动漫被永久删除时发布的领域事件</p>
 * 
 * @author chenyunlong
 * @since 1.0.0
 */
@Getter
public class AnimeDeletedEvent extends ApplicationEvent {

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
     * 标签ID列表
     */
    private final List<Long> tagIds;

    /**
     * 封面图片URL
     */
    private final String coverImageUrl;

    /**
     * 相关文件路径列表
     */
    private final List<String> relatedFiles;

    /**
     * 删除原因
     */
    private final String deleteReason;

    /**
     * 删除时间
     */
    private final LocalDateTime deleteTime;

    /**
     * 删除者ID
     */
    private final Long deletedBy;

    /**
     * 构造函数
     * 
     * @param source 事件源
     * @param animeId 动漫ID
     * @param animeName 动漫名称
     * @param categoryId 分类ID
     * @param tagIds 标签ID列表
     * @param coverImageUrl 封面图片URL
     * @param relatedFiles 相关文件路径列表
     * @param deleteReason 删除原因
     * @param deleteTime 删除时间
     * @param deletedBy 删除者ID
     */
    public AnimeDeletedEvent(Object source, AnimeId animeId, String animeName, 
                           Long categoryId, List<Long> tagIds, String coverImageUrl,
                           List<String> relatedFiles, String deleteReason,
                           LocalDateTime deleteTime, Long deletedBy) {
        super(source);
        this.animeId = animeId;
        this.animeName = animeName;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
        this.coverImageUrl = coverImageUrl;
        this.relatedFiles = relatedFiles;
        this.deleteReason = deleteReason;
        this.deleteTime = deleteTime;
        this.deletedBy = deletedBy;
    }

    /**
     * 创建动漫删除事件
     * 
     * @param source 事件源
     * @param animeId 动漫ID
     * @param animeName 动漫名称
     * @param categoryId 分类ID
     * @param tagIds 标签ID列表
     * @param coverImageUrl 封面图片URL
     * @param relatedFiles 相关文件路径列表
     * @param deleteReason 删除原因
     * @param deleteTime 删除时间
     * @param deletedBy 删除者ID
     * @return 动漫删除事件
     */
    public static AnimeDeletedEvent of(Object source, AnimeId animeId, String animeName, 
                                     Long categoryId, List<Long> tagIds, String coverImageUrl,
                                     List<String> relatedFiles, String deleteReason,
                                     LocalDateTime deleteTime, Long deletedBy) {
        return new AnimeDeletedEvent(source, animeId, animeName, categoryId, tagIds, 
                                   coverImageUrl, relatedFiles, deleteReason, deleteTime, deletedBy);
    }

    /**
     * 是否有相关文件需要清理
     * 
     * @return 是否有相关文件
     */
    public boolean hasRelatedFiles() {
        return relatedFiles != null && !relatedFiles.isEmpty();
    }

    /**
     * 是否有封面图片需要清理
     * 
     * @return 是否有封面图片
     */
    public boolean hasCoverImage() {
        return coverImageUrl != null && !coverImageUrl.trim().isEmpty();
    }

    /**
     * 是否有标签关联
     * 
     * @return 是否有标签
     */
    public boolean hasTags() {
        return tagIds != null && !tagIds.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("AnimeDeletedEvent{animeId=%s, animeName='%s', categoryId=%d, deleteTime=%s, deletedBy=%d}", 
                           animeId.getValue(), animeName, categoryId, deleteTime, deletedBy);
    }
}
