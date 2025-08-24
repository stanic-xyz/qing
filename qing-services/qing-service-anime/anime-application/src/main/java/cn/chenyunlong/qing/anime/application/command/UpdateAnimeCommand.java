package cn.chenyunlong.qing.anime.application.command;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 更新动漫命令
 *
 * <p>用于更新现有动漫信息的命令对象</p>
 *
 * <p>命令验证规则：</p>
 * <ul>
 *   <li>动漫ID不能为空</li>
 *   <li>如果提供名称，则不能为空</li>
 *   <li>标签列表可以为空</li>
 * </ul>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
public class UpdateAnimeCommand {

    /**
     * 动漫ID
     */
    @NonNull
    private Long animeId;

    /**
     * 动漫名称
     */
    private String name;

    /**
     * 动漫简介
     */
    private String instruction;

    /**
     * 标签ID列表
     */
    private List<Long> tagIds;

    /**
     * 播放状态
     */
    private Integer playStatus;

    /**
     * 封面图片URL
     */
    private String coverUrl;

    /**
     * 原始名称
     */
    private String originalName;

    /**
     * 其他名称
     */
    private String otherName;

    /**
     * 作者
     */
    private String author;

    /**
     * 官方网站
     */
    private String officialWebsite;

    /**
     * 验证命令的有效性
     *
     * @throws IllegalArgumentException 当命令无效时
     */
    public void validate() {
        if (animeId <= 0) {
            throw new IllegalArgumentException("动漫ID不能为空且必须大于0");
        }

        if (name != null) {
            if (!StringUtils.hasText(name)) {
                throw new IllegalArgumentException("动漫名称不能为空字符串");
            }

            if (name.length() > 100) {
                throw new IllegalArgumentException("动漫名称长度不能超过100个字符");
            }
        }

        if (instruction != null && instruction.length() > 2000) {
            throw new IllegalArgumentException("动漫简介长度不能超过2000个字符");
        }

        if (tagIds != null && tagIds.size() > 20) {
            throw new IllegalArgumentException("标签数量不能超过20个");
        }
    }

    /**
     * 检查是否有任何字段需要更新
     *
     * @return true如果有字段需要更新，false否则
     */
    public boolean hasUpdates() {
        return !StringUtils.hasText(name) &&
                instruction == null &&
                tagIds == null &&
                playStatus!=null &&
                !StringUtils.hasText(coverUrl) &&
                !StringUtils.hasText(originalName) &&
                !StringUtils.hasText(otherName) &&
                !StringUtils.hasText(author) &&
                !StringUtils.hasText(officialWebsite);
    }

    /**
     * 获取清理后的名称
     *
     * @return 清理后的名称
     */
    public String getCleanName() {
        return name != null ? name.trim() : null;
    }

    /**
     * 获取清理后的简介
     *
     * @return 清理后的简介
     */
    public String getCleanInstruction() {
        return instruction != null ? instruction.trim() : null;
    }
}
