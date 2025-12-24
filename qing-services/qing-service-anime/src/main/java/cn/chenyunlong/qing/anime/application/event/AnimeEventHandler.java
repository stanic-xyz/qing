package cn.chenyunlong.qing.anime.application.event;

import cn.chenyunlong.qing.anime.domain.anime.event.AnimeCreatedEvent;
import cn.chenyunlong.qing.anime.domain.anime.event.AnimeDeletedEvent;
import cn.chenyunlong.qing.anime.domain.anime.event.AnimeStatusChangedEvent;
import cn.chenyunlong.qing.anime.domain.anime.event.AnimeUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 动漫领域事件处理器
 *
 * <p>
 * 处理动漫相关的领域事件，执行后续的业务逻辑
 * </p>
 *
 * <p>
 * 职责：
 * </p>
 * <ul>
 * <li>处理动漫创建事件</li>
 * <li>处理动漫更新事件</li>
 * <li>处理动漫状态变更事件</li>
 * <li>处理动漫删除事件</li>
 * <li>发送通知和消息</li>
 * <li>更新缓存和索引</li>
 * </ul>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class AnimeEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AnimeEventHandler.class);

    /**
     * 处理动漫创建事件
     *
     * <p>
     * 当动漫创建成功后执行的后续操作：
     * </p>
     * <ul>
     * <li>记录操作日志</li>
     * <li>发送创建通知</li>
     * <li>更新搜索索引</li>
     * <li>清理相关缓存</li>
     * </ul>
     *
     * @param event 动漫创建事件
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAnimeCreated(AnimeCreatedEvent event) {
        log.info("处理动漫创建事件，动漫ID: {}, 名称: {}",
                event.getAnimeId().id(), event.getAnimeName());

        try {
            // 记录操作日志
            recordOperationLog("ANIME_CREATED", event.getAnimeId().id(),
                    "动漫创建成功: " + event.getAnimeName());

            // 发送创建通知
            sendCreationNotification(event);

            // 更新搜索索引
            updateSearchIndex(event.getAnimeId().id());

            // 清理相关缓存
            clearRelatedCache(event.getCategoryId());

            log.info("动漫创建事件处理完成，动漫ID: {}", event.getAnimeId().id());

        } catch (Exception e) {
            log.error("处理动漫创建事件失败，动漫ID: {}", event.getAnimeId().id(), e);
            // 这里可以发送告警或重试
        }
    }

    /**
     * 处理动漫更新事件
     *
     * <p>
     * 当动漫信息更新后执行的后续操作：
     * </p>
     * <ul>
     * <li>记录操作日志</li>
     * <li>发送更新通知</li>
     * <li>更新搜索索引</li>
     * <li>清理相关缓存</li>
     * </ul>
     *
     * @param event 动漫更新事件
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAnimeUpdated(AnimeUpdatedEvent event) {
        log.info("处理动漫更新事件，动漫ID: {}, 名称: {}",
                event.getAnimeId().id(), event.getAnimeName());

        try {
            // 记录操作日志
            recordOperationLog("ANIME_UPDATED", event.getAnimeId().id(),
                    "动漫信息更新: " + event.getAnimeName());

            // 发送更新通知
            sendUpdateNotification(event);

            // 更新搜索索引
            updateSearchIndex(event.getAnimeId().id());

            // 清理相关缓存
            clearAnimeCache(event.getAnimeId().id());

            log.info("动漫更新事件处理完成，动漫ID: {}", event.getAnimeId().id());

        } catch (Exception e) {
            log.error("处理动漫更新事件失败，动漫ID: {}", event.getAnimeId().id(), e);
        }
    }

    /**
     * 处理动漫状态变更事件
     *
     * <p>
     * 当动漫状态发生变更后执行的后续操作：
     * </p>
     * <ul>
     * <li>记录操作日志</li>
     * <li>发送状态变更通知</li>
     * <li>更新搜索索引</li>
     * <li>清理相关缓存</li>
     * </ul>
     *
     * @param event 动漫状态变更事件
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAnimeStatusChanged(AnimeStatusChangedEvent event) {
        log.info("处理动漫状态变更事件，动漫ID: {}, 状态: {} -> {}",
                event.getAnimeId().id(), event.getOldStatus(), event.getNewStatus());

        try {
            // 记录操作日志
            recordOperationLog("ANIME_STATUS_CHANGED", event.getAnimeId().id(),
                    String.format("动漫状态变更: %s -> %s", event.getOldStatus(), event.getNewStatus()));

            // 发送状态变更通知
            sendStatusChangeNotification(event);

            // 更新搜索索引
            updateSearchIndex(event.getAnimeId().id());

            // 清理相关缓存
            clearAnimeCache(event.getAnimeId().id());
            clearStatusCache(event.getNewStatus());

            log.info("动漫状态变更事件处理完成，动漫ID: {}", event.getAnimeId().id());

        } catch (Exception e) {
            log.error("处理动漫状态变更事件失败，动漫ID: {}", event.getAnimeId().id(), e);
        }
    }

    /**
     * 处理动漫删除事件
     *
     * <p>
     * 当动漫被删除后执行的后续操作：
     * </p>
     * <ul>
     * <li>记录操作日志</li>
     * <li>发送删除通知</li>
     * <li>清理搜索索引</li>
     * <li>清理相关缓存</li>
     * <li>清理相关文件</li>
     * </ul>
     *
     * @param event 动漫删除事件
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAnimeDeleted(AnimeDeletedEvent event) {
        log.info("处理动漫删除事件，动漫ID: {}, 名称: {}",
                event.getAnimeId().id(), event.getAnimeName());

        try {
            // 记录操作日志
            recordOperationLog("ANIME_DELETED", event.getAnimeId().id(),
                    "动漫删除: " + event.getAnimeName());

            // 发送删除通知
            sendDeletionNotification(event);

            // 清理搜索索引
            removeFromSearchIndex(event.getAnimeId().id());

            // 清理相关缓存
            clearAnimeCache(event.getAnimeId().id());
            clearRelatedCache(event.getCategoryId());

            // 清理相关文件（如封面图片等）
            cleanupRelatedFiles(event.getAnimeId().id());

            log.info("动漫删除事件处理完成，动漫ID: {}", event.getAnimeId().id());

        } catch (Exception e) {
            log.error("处理动漫删除事件失败，动漫ID: {}", event.getAnimeId().id(), e);
        }
    }

    /**
     * 记录操作日志
     *
     * @param operation   操作类型
     * @param animeId     动漫ID
     * @param description 操作描述
     */
    private void recordOperationLog(String operation, Long animeId, String description) {
        log.info("记录操作日志 - 操作: {}, 动漫ID: {}, 描述: {}", operation, animeId, description);
        // 这里可以集成具体的日志记录服务
    }

    /**
     * 发送创建通知
     *
     * @param event 动漫创建事件
     */
    private void sendCreationNotification(AnimeCreatedEvent event) {
        log.debug("发送动漫创建通知，动漫ID: {}", event.getAnimeId().id());
        // 这里可以集成消息队列或通知服务
    }

    /**
     * 发送更新通知
     *
     * @param event 动漫更新事件
     */
    private void sendUpdateNotification(AnimeUpdatedEvent event) {
        log.debug("发送动漫更新通知，动漫ID: {}", event.getAnimeId().id());
        // 这里可以集成消息队列或通知服务
    }

    /**
     * 发送状态变更通知
     *
     * @param event 动漫状态变更事件
     */
    private void sendStatusChangeNotification(AnimeStatusChangedEvent event) {
        log.debug("发送动漫状态变更通知，动漫ID: {}", event.getAnimeId().id());
        // 这里可以集成消息队列或通知服务
    }

    /**
     * 发送删除通知
     *
     * @param event 动漫删除事件
     */
    private void sendDeletionNotification(AnimeDeletedEvent event) {
        log.debug("发送动漫删除通知，动漫ID: {}", event.getAnimeId().id());
        // 这里可以集成消息队列或通知服务
    }

    /**
     * 更新搜索索引
     *
     * @param animeId 动漫ID
     */
    private void updateSearchIndex(Long animeId) {
        log.debug("更新搜索索引，动漫ID: {}", animeId);
        // 这里可以集成Elasticsearch或其他搜索引擎
    }

    /**
     * 从搜索索引中移除
     *
     * @param animeId 动漫ID
     */
    private void removeFromSearchIndex(Long animeId) {
        log.debug("从搜索索引中移除，动漫ID: {}", animeId);
        // 这里可以集成Elasticsearch或其他搜索引擎
    }

    /**
     * 清理动漫缓存
     *
     * @param animeId 动漫ID
     */
    private void clearAnimeCache(Long animeId) {
        log.debug("清理动漫缓存，动漫ID: {}", animeId);
        // 这里可以集成Redis或其他缓存服务
    }

    /**
     * 清理相关缓存
     *
     * @param categoryId 分类ID
     */
    private void clearRelatedCache(Long categoryId) {
        log.debug("清理相关缓存，分类ID: {}", categoryId);
        // 这里可以集成Redis或其他缓存服务
    }

    /**
     * 清理状态缓存
     *
     * @param status 状态
     */
    private void clearStatusCache(AnimeStatusChangedEvent.AnimeStatus status) {
        log.debug("清理状态缓存，状态: {}", status);
        // 这里可以集成Redis或其他缓存服务
    }

    /**
     * 清理相关文件
     *
     * @param animeId 动漫ID
     */
    private void cleanupRelatedFiles(Long animeId) {
        log.debug("清理相关文件，动漫ID: {}", animeId);
        // 这里可以集成文件存储服务，清理封面图片等文件
    }
}
