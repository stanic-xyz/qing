package cn.chenyunlong.qing.anime.domain.anime.event;

import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * 动漫状态变更事件
 *
 * <p>当动漫状态发生变更时发布的领域事件，包括上架、下架、删除、恢复等操作</p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Getter
public class AnimeStatusChangedEvent extends ApplicationEvent {

    /**
     * 动漫ID
     */
    private final AnimeId animeId;

    /**
     * 动漫名称
     */
    private final String animeName;

    /**
     * 状态操作类型
     */
    private final StatusOperation operation;

    /**
     * 操作前状态
     */
    private final AnimeStatus oldStatus;

    /**
     * 操作后状态
     */
    private final AnimeStatus newStatus;

    /**
     * 操作原因
     */
    private final String reason;

    /**
     * 操作时间
     */
    private final LocalDateTime operationTime;

    /**
     * 操作者ID
     */
    private final Long operatorId;

    /**
     * 状态操作类型
     */
    public enum StatusOperation {
        /**
         * 上架
         */
        PUT_ON_SHELF,

        /**
         * 下架
         */
        TAKE_OFF_SHELF,

        /**
         * 删除
         */
        DELETE,

        /**
         * 恢复
         */
        RESTORE
    }

    /**
     * 动漫状态
     */
    public record AnimeStatus(boolean onShelf, boolean deleted) {

        @Override
        public String toString() {
            if (deleted) {
                return "已删除";
            }
            return onShelf ? "已上架" : "已下架";
        }
    }

    /**
     * 构造函数
     *
     * @param source        事件源
     * @param animeId       动漫ID
     * @param animeName     动漫名称
     * @param operation     操作类型
     * @param oldStatus     操作前状态
     * @param newStatus     操作后状态
     * @param reason        操作原因
     * @param operationTime 操作时间
     * @param operatorId    操作者ID
     */
    public AnimeStatusChangedEvent(Object source, AnimeId animeId, String animeName,
                                   StatusOperation operation, AnimeStatus oldStatus, AnimeStatus newStatus,
                                   String reason, LocalDateTime operationTime, Long operatorId) {
        super(source);
        this.animeId = animeId;
        this.animeName = animeName;
        this.operation = operation;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.reason = reason;
        this.operationTime = operationTime;
        this.operatorId = operatorId;
    }

    /**
     * 创建上架事件
     *
     * @param source        事件源
     * @param animeId       动漫ID
     * @param animeName     动漫名称
     * @param reason        上架原因
     * @param operationTime 操作时间
     * @param operatorId    操作者ID
     * @return 状态变更事件
     */
    public static AnimeStatusChangedEvent putOnShelf(Object source, AnimeId animeId, String animeName,
                                                     String reason, LocalDateTime operationTime, Long operatorId) {
        return new AnimeStatusChangedEvent(source, animeId, animeName, StatusOperation.PUT_ON_SHELF,
                new AnimeStatus(false, false), new AnimeStatus(true, false),
                reason, operationTime, operatorId);
    }

    /**
     * 创建下架事件
     *
     * @param source        事件源
     * @param animeId       动漫ID
     * @param animeName     动漫名称
     * @param reason        下架原因
     * @param operationTime 操作时间
     * @param operatorId    操作者ID
     * @return 状态变更事件
     */
    public static AnimeStatusChangedEvent takeOffShelf(Object source, AnimeId animeId, String animeName,
                                                       String reason, LocalDateTime operationTime, Long operatorId) {
        return new AnimeStatusChangedEvent(source, animeId, animeName, StatusOperation.TAKE_OFF_SHELF,
                new AnimeStatus(true, false), new AnimeStatus(false, false),
                reason, operationTime, operatorId);
    }

    /**
     * 创建删除事件
     *
     * @param source        事件源
     * @param animeId       动漫ID
     * @param animeName     动漫名称
     * @param reason        删除原因
     * @param operationTime 操作时间
     * @param operatorId    操作者ID
     * @return 状态变更事件
     */
    public static AnimeStatusChangedEvent delete(Object source, AnimeId animeId, String animeName,
                                                 String reason, LocalDateTime operationTime, Long operatorId) {
        return new AnimeStatusChangedEvent(source, animeId, animeName, StatusOperation.DELETE,
                new AnimeStatus(false, false), new AnimeStatus(false, true),
                reason, operationTime, operatorId);
    }

    /**
     * 创建恢复事件
     *
     * @param source        事件源
     * @param animeId       动漫ID
     * @param animeName     动漫名称
     * @param reason        恢复原因
     * @param operationTime 操作时间
     * @param operatorId    操作者ID
     * @return 状态变更事件
     */
    public static AnimeStatusChangedEvent restore(Object source, AnimeId animeId, String animeName,
                                                  String reason, LocalDateTime operationTime, Long operatorId) {
        return new AnimeStatusChangedEvent(source, animeId, animeName, StatusOperation.RESTORE,
                new AnimeStatus(false, true), new AnimeStatus(false, false),
                reason, operationTime, operatorId);
    }

    @Override
    public String toString() {
        return String.format("AnimeStatusChangedEvent{animeId=%s, animeName='%s', operation=%s, oldStatus=%s, newStatus=%s, operationTime=%s}",
                animeId.getValue(), animeName, operation, oldStatus, newStatus, operationTime);
    }
}
