package cn.chenyunlong.qing.anime.application.command;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 创建动漫命令
 *
 * <p>用于创建新动漫的命令对象</p>
 *
 * <p>命令验证规则：</p>
 * <ul>
 *   <li>动漫名称不能为空</li>
 *   <li>分类ID必须有效</li>
 *   <li>标签列表可以为空</li>
 * </ul>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
public class CreateAnimeCommand {

    /**
     * 动漫名称
     */
    @NonNull
    private String name;

    /**
     * 动漫简介
     */
    private String instruction;

    /**
     * 动漫分类ID
     */
    @NonNull
    private Long categoryId;

    /**
     * 标签ID列表
     */
    private List<Long> tagIds;

    /**
     * 播放状态
     */
    private String playStatus;

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
     * 地区
     */
    private String district;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 类型ID
     */
    private Long typeId;

    /**
     * 验证命令的有效性
     *
     * @throws IllegalArgumentException 当命令无效时
     */
    public void validate() {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("动漫名称不能为空");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("动漫名称长度不能超过100个字符");
        }

        if (categoryId <= 0) {
            throw new IllegalArgumentException("动漫分类ID必须有效");
        }

        if (instruction != null && instruction.length() > 2000) {
            throw new IllegalArgumentException("动漫简介长度不能超过2000个字符");
        }

        if (tagIds != null && tagIds.size() > 20) {
            throw new IllegalArgumentException("标签数量不能超过20个");
        }
    }

    /**
     * 获取清理后的名称
     *
     * @return 清理后的名称
     */
    public String getCleanName() {
        return StrUtil.trim(name);
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
