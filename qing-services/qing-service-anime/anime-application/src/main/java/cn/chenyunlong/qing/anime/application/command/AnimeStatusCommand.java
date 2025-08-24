package cn.chenyunlong.qing.anime.application.command;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * 动漫状态操作命令
 *
 * <p>用于动漫状态变更操作的命令对象，包括上架、下架、删除等操作</p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
public class AnimeStatusCommand {

    /**
     * 动漫ID
     */
    @NotNull
    private Long animeId;

    /**
     * 操作类型
     */
    @NotNull
    private StatusOperation operation;

    /**
     * 操作原因（可选）
     */
    private String reason;

    /**
     * 操作人ID（可选）
     */
    private Long operatorId;

    /**
     * 状态操作类型枚举
     */
    @Getter
    public enum StatusOperation {
        /**
         * 上架
         */
        PUT_ON_SHELF("上架"),

        /**
         * 下架
         */
        TAKE_OFF_SHELF("下架"),

        /**
         * 删除
         */
        DELETE("删除"),

        /**
         * 恢复
         */
        RESTORE("恢复");

        private final String description;

        StatusOperation(String description) {
            this.description = description;
        }

    }

    /**
     * 验证命令的有效性
     *
     * @throws IllegalArgumentException 当命令无效时
     */
    public void validate() {
        if (animeId == null || animeId <= 0) {
            throw new IllegalArgumentException("动漫ID不能为空且必须大于0");
        }

        if (operation == null) {
            throw new IllegalArgumentException("操作类型不能为空");
        }

        if (reason != null && reason.length() > 500) {
            throw new IllegalArgumentException("操作原因长度不能超过500个字符");
        }
    }

    /**
     * 创建上架命令
     *
     * @param animeId 动漫ID
     * @return 上架命令
     */
    public static AnimeStatusCommand putOnShelf(Long animeId) {
        return AnimeStatusCommand.builder()
                .animeId(animeId)
                .operation(StatusOperation.PUT_ON_SHELF)
                .build();
    }

    /**
     * 创建下架命令
     *
     * @param animeId 动漫ID
     * @param reason  下架原因
     * @return 下架命令
     */
    public static AnimeStatusCommand takeOffShelf(Long animeId, String reason) {
        return AnimeStatusCommand.builder()
                .animeId(animeId)
                .operation(StatusOperation.TAKE_OFF_SHELF)
                .reason(reason)
                .build();
    }

    /**
     * 创建删除命令
     *
     * @param animeId 动漫ID
     * @param reason  删除原因
     * @return 删除命令
     */
    public static AnimeStatusCommand delete(Long animeId, String reason) {
        return AnimeStatusCommand.builder()
                .animeId(animeId)
                .operation(StatusOperation.DELETE)
                .reason(reason)
                .build();
    }

    /**
     * 创建恢复命令
     *
     * @param animeId 动漫ID
     * @return 恢复命令
     */
    public static AnimeStatusCommand restore(Long animeId) {
        return AnimeStatusCommand.builder()
                .animeId(animeId)
                .operation(StatusOperation.RESTORE)
                .build();
    }
}
