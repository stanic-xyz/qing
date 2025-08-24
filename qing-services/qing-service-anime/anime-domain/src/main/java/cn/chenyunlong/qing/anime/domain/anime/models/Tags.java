package cn.chenyunlong.qing.anime.domain.anime.models;

import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 动漫标签值对象
 *
 * <p>表示动漫的标签集合，用于分类和检索</p>
 *
 * <p>值对象特性：</p>
 * <ul>
 *   <li>不可变性：标签列表一旦创建不可修改</li>
 *   <li>值相等性：基于标签内容进行相等性比较</li>
 *   <li>自验证：确保标签的有效性和唯一性</li>
 * </ul>
 *
 * <p>业务规则：</p>
 * <ul>
 *   <li>标签不能为空或空白字符串</li>
 *   <li>标签会自动去重和排序</li>
 *   <li>标签数量限制在合理范围内</li>
 * </ul>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
public record Tags(List<String> tags) {

    /**
     * 最大标签数量限制
     */
    public static final int MAX_TAG_COUNT = 20;

    /**
     * 标签最大长度限制
     */
    public static final int MAX_TAG_LENGTH = 50;

    /**
     * 构造标签值对象
     *
     * @param tags 标签列表
     * @throws IllegalArgumentException 当标签无效时
     */
    public Tags {
        if (tags == null) {
            tags = Collections.emptyList();
        } else {
            // 验证和清理标签
            tags = validateAndCleanTags(tags);
        }
    }

    /**
     * 检查是否包含指定标签
     *
     * @param tag 要检查的标签
     * @return true如果包含该标签，false否则
     */
    public boolean contains(String tag) {
        if (!StringUtils.hasText(tag)) {
            return false;
        }
        return tags.contains(tag.trim());
    }

    /**
     * 创建空标签
     *
     * @return 空标签实例
     */
    public static Tags createEmptyTags() {
        return new Tags(Collections.emptyList());
    }

    /**
     * 创建标签实例
     *
     * @param tagNameList 标签名称列表
     * @return 标签实例
     * @throws IllegalArgumentException 当标签无效时
     */
    public static Tags create(List<String> tagNameList) {
        return new Tags(tagNameList);
    }

    /**
     * 从字符串数组创建标签
     *
     * @param tags 标签数组
     * @return 标签实例
     */
    public static Tags of(String... tags) {
        return new Tags(Arrays.asList(tags));
    }

    /**
     * 从逗号分隔的字符串创建标签
     *
     * @param tagsString 逗号分隔的标签字符串
     * @return 标签实例
     */
    public static Tags fromString(String tagsString) {
        if (!StringUtils.hasText(tagsString)) {
            return createEmptyTags();
        }

        List<String> tagList = Arrays.stream(tagsString.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        return new Tags(tagList);
    }

    /**
     * 添加新标签
     *
     * @param newTag 新标签
     * @return 包含新标签的Tags实例
     * @throws IllegalArgumentException 当标签无效时
     */
    public Tags add(@NonNull String newTag) {
        if (!StringUtils.hasText(newTag)) {
            throw new IllegalArgumentException("标签不能为空");
        }

        String cleanTag = newTag.trim();
        if (contains(cleanTag)) {
            return this; // 已存在，返回当前实例
        }

        List<String> newTags = new ArrayList<>(this.tags);
        newTags.add(cleanTag);
        return new Tags(newTags);
    }

    /**
     * 移除指定标签
     *
     * @param tagToRemove 要移除的标签
     * @return 移除标签后的Tags实例
     */
    public Tags remove(String tagToRemove) {
        if (!StringUtils.hasText(tagToRemove) || !contains(tagToRemove)) {
            return this; // 不存在，返回当前实例
        }

        List<String> newTags = this.tags.stream()
                .filter(tag -> !tag.equals(tagToRemove.trim()))
                .collect(Collectors.toList());

        return new Tags(newTags);
    }

    /**
     * 获取标签数量
     *
     * @return 标签数量
     */
    public int size() {
        return tags.size();
    }

    /**
     * 检查是否为空
     *
     * @return true如果没有标签，false否则
     */
    public boolean isEmpty() {
        return tags.isEmpty();
    }

    /**
     * 获取标签的不可变副本
     *
     * @return 标签列表的不可变副本
     */
    public List<String> asList() {
        return Collections.unmodifiableList(tags);
    }

    /**
     * 验证和清理标签列表
     *
     * @param rawTags 原始标签列表
     * @return 清理后的标签列表
     * @throws IllegalArgumentException 当标签无效时
     */
    private static List<String> validateAndCleanTags(List<String> rawTags) {
        if (rawTags.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> cleanedTags = rawTags.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(StringUtils::hasText)
                .distinct()
                .sorted()
                .toList();

        // 验证标签数量
        if (cleanedTags.size() > MAX_TAG_COUNT) {
            throw new IllegalArgumentException(
                    String.format("标签数量不能超过%d个，当前：%d个", MAX_TAG_COUNT, cleanedTags.size())
            );
        }

        // 验证每个标签的长度
        for (String tag : cleanedTags) {
            if (tag.length() > MAX_TAG_LENGTH) {
                throw new IllegalArgumentException(
                        String.format("标签长度不能超过%d个字符，当前标签：%s", MAX_TAG_LENGTH, tag)
                );
            }
        }

        return Collections.unmodifiableList(cleanedTags);
    }
}
