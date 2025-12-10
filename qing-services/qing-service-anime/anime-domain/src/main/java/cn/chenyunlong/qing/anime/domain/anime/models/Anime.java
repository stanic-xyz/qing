/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.anime.domain.anime.models;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import cn.chenyunlong.qing.anime.domain.anime.enums.District;
import cn.chenyunlong.qing.anime.domain.anime.event.AnimeCreatedEvent;
import cn.chenyunlong.qing.anime.domain.anime.event.AnimeStatusChangedEvent;
import cn.chenyunlong.qing.anime.domain.anime.event.AnimeUpdatedEvent;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 动漫聚合根
 *
 * <p>
 * 动漫是系统的核心聚合根，包含动漫的基本信息、分类、标签、播放状态等。
 * 该聚合根负责维护动漫的完整性和业务规则，包括：
 * </p>
 * <ul>
 * <li>动漫基本信息的管理</li>
 * <li>标签的添加和移除（最多10个）</li>
 * <li>播放状态的变更</li>
 * <li>上架/下架操作</li>
 * <li>删除操作（仅限下架状态）</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Anime extends BaseSimpleBusinessEntity<AnimeId> {

    /**
     * 最大标签数量限制
     */
    private static final int MAX_TAGS = 10;

    @FieldDesc(description = "分类")
    public AnimeCategory animeCategory;

    @FieldDesc(description = "名称")
    public String name;

    @FieldDesc(description = "介绍")
    public String instruction;

    @FieldDesc(description = "区域")
    public District district;

    @FieldDesc(description = "封面地址")
    private String cover;

    @FieldDesc(description = "类型")
    public AnimeType type;

    @FieldDesc(description = "原始名称")
    private String originalName;

    @FieldDesc(description = "其他名称")
    private String otherName;

    @FieldDesc(description = "作者")
    private String author;

    @FieldDesc(description = "公司")
    public Company company;

    @FieldDesc(description = "发布日期")
    public PremiereDate premiereDate;

    @FieldDesc(description = "播放状态")
    public PlayStatus playStatus;

    @FieldDesc(description = "动漫类型")
    private PlotTypes plotTypes;

    @FieldDesc(description = "动漫标签列表")
    private Tags tags;

    @FieldDesc(description = "官网")
    private String officialWebsite;

    @FieldDesc(description = "播放热度")
    private String playHeat;

    @FieldDesc(description = "最后更新时间")
    private LocalDateTime lastUpdateTime;

    @FieldDesc(description = "排序号")
    private Integer orderNo;

    @FieldDesc(description = "是否上架")
    private Boolean isOnShelf;

    @FieldDesc(description = "是否删除")
    private Boolean isDeleted;

    @FieldDesc(description = "最后更新者")
    private Long lastUpdatedBy;

    /**
     * 创建新的动漫聚合根
     *
     * @param animeId       动漫ID
     * @param name          动漫名称
     * @param animeCategory 动漫分类
     * @param instruction   动漫介绍
     * @return 新创建的动漫实例
     */
    public static Anime create(AnimeId animeId, String name, AnimeCategory animeCategory, String instruction) {
        Assert.notNull(animeId, "动漫ID不能为空");
        Assert.hasText(name, "动漫名称不能为空");
        Assert.notNull(animeCategory, "动漫分类不能为空");

        Anime anime = new Anime();
        anime.setId(animeId);
        anime.name = name;
        anime.animeCategory = animeCategory;
        anime.instruction = instruction;
        anime.playStatus = PlayStatus.SERIALIZING;
        anime.tags = Tags.create(new ArrayList<>());
        anime.isOnShelf = false;
        anime.isDeleted = false;
        anime.lastUpdateTime = LocalDateTime.now();
        return anime;
    }

    /**
     * 创建新的动漫聚合根
     *
     * @param animeId       动漫ID
     * @param name          动漫名称
     * @param animeCategory 动漫分类
     * @param instruction   动漫介绍
     * @return 新创建的动漫实例
     */
    public static Anime createMock(AnimeId animeId, String name, AnimeCategory animeCategory, String instruction) {
        Assert.notNull(animeId, "动漫ID不能为空");
        Assert.hasText(name, "动漫名称不能为空");
        Assert.notNull(animeCategory, "动漫分类不能为空");
        return create(animeId, name, animeCategory, instruction);
    }

    /**
     * 重建动漫聚合根（用于从持久化存储重建）
     */
    public static Anime rebuild(AnimeId animeId, String name, AnimeCategory animeCategory,
                                String instruction, District district, String cover,
                                AnimeType type, String originalName, String otherName,
                                String author, Company company, PremiereDate premiereDate,
                                PlayStatus playStatus, PlotTypes plotTypes, Tags tags,
                                String officialWebsite, String playHeat,
                                LocalDateTime lastUpdateTime, Integer orderNo,
                                Boolean isOnShelf, Boolean isDeleted) {
        Anime anime = new Anime();
        anime.setId(animeId);
        anime.name = name;
        anime.animeCategory = animeCategory;
        anime.instruction = instruction;
        anime.district = district;
        anime.cover = cover;
        anime.type = type;
        anime.originalName = originalName;
        anime.otherName = otherName;
        anime.author = author;
        anime.company = company;
        anime.premiereDate = premiereDate;
        anime.playStatus = playStatus;
        anime.plotTypes = plotTypes;
        anime.tags = tags != null ? tags : Tags.create(new ArrayList<>());
        anime.officialWebsite = officialWebsite;
        anime.playHeat = playHeat;
        anime.lastUpdateTime = lastUpdateTime;
        anime.orderNo = orderNo;
        anime.isOnShelf = isOnShelf != null ? isOnShelf : false;
        anime.isDeleted = isDeleted != null ? isDeleted : false;

        return anime;
    }

    /**
     * 初始化动漫（创建后调用）
     *
     * <p>
     * 该方法在动漫创建后调用，用于触发创建事件和执行初始化逻辑
     * </p>
     */
    public void initialize() {
        this.lastUpdateTime = LocalDateTime.now();
        registerEvent(AnimeCreatedEvent.of(this, getId(), name,
                animeCategory != null ? animeCategory.id().id() : null,
                tags, playStatus != null ? playStatus.name() : null,
                lastUpdateTime, 1L)); // 临时使用固定值，后续需要从上下文获取
    }

    /**
     * 更新动漫基本信息
     *
     * @param name            动漫名称
     * @param instruction     动漫介绍
     * @param cover           封面地址
     * @param originalName    原始名称
     * @param otherName       其他名称
     * @param author          作者
     * @param officialWebsite 官方网站
     */
    public void updateInfo(String name,
                           String instruction,
                           String cover,
                           String originalName,
                           String otherName,
                           String author,
                           String officialWebsite) {

        // 验证必要字段
        Assert.hasText(name, "动漫名称不能为空");

        // 更新字段
        this.name = name;
        this.instruction = instruction;
        this.cover = cover;
        this.originalName = originalName;
        this.otherName = otherName;
        this.author = author;
        this.officialWebsite = officialWebsite;
        this.lastUpdateTime = LocalDateTime.now();

        // 触发领域事件
        registerEvent(AnimeUpdatedEvent.basicInfoUpdated(this, getId(), name,
                this.name, name, lastUpdateTime, this.lastUpdatedBy));
    }

    /**
     * 更新动漫详细信息
     *
     * @param district     区域
     * @param type         动漫类型
     * @param company      制作公司
     * @param premiereDate 首播日期
     * @param plotTypes    剧情类型
     * @param playHeat     播放热度
     * @param orderNo      排序号
     */
    public void updateDetails(District district, AnimeType type, Company company,
                              PremiereDate premiereDate, PlotTypes plotTypes,
                              String playHeat, Integer orderNo) {
        this.district = district;
        this.type = type;
        this.company = company;
        this.premiereDate = premiereDate;
        this.plotTypes = plotTypes;
        this.playHeat = playHeat;
        this.orderNo = orderNo;
        this.lastUpdateTime = LocalDateTime.now();

        registerEvent(AnimeUpdatedEvent.detailInfoUpdated(this, getId(), name,
                lastUpdateTime, getLastUpdatedBy()));
    }

    /**
     * 验证标签列表
     *
     * @param tags 标签列表
     * @throws IllegalArgumentException 当标签数量超出限制时
     */
    private void validateTags(List<String> tags) {
        if (tags == null) {
            return;
        }
        if (tags.size() > MAX_TAGS) {
            throw new IllegalArgumentException(String.format("标签数量不能超过%d个，当前数量：%d", MAX_TAGS, tags.size()));
        }
    }

    /**
     * 添加标签
     *
     * @param tag 要添加的标签
     * @throws IllegalArgumentException 当标签为空或标签数量超出限制时
     */
    public void addTag(String tag) {
        Assert.hasText(tag, "标签不能为空");

        List<String> currentTags = this.tags != null ? this.tags.tags() : new ArrayList<>();

        // 检查标签是否已存在
        if (currentTags.contains(tag)) {
            return; // 标签已存在，不重复添加
        }

        List<String> newTags = new ArrayList<>(currentTags);
        newTags.add(tag);

        // 验证标签数量
        validateTags(newTags);

        this.tags = new Tags(newTags);
        this.lastUpdateTime = LocalDateTime.now();
    }

    /**
     * 移除标签
     *
     * @param tag 要移除的标签
     */
    public void removeTag(String tag) {
        if (!StringUtils.hasText(tag) || this.tags == null) {
            return;
        }

        List<String> currentTags = this.tags.tags();
        if (!currentTags.contains(tag)) {
            return; // 标签不存在，无需移除
        }

        List<String> newTags = new ArrayList<>(currentTags);
        newTags.remove(tag);
        this.tags = new Tags(newTags);
        this.lastUpdateTime = LocalDateTime.now();
    }

    /**
     * 批量设置标签
     *
     * @param tagList 标签列表
     */
    public void setTags(List<String> tagList) {
        validateTags(tagList);
        this.tags = Tags.create(tagList != null ? tagList : new ArrayList<>());
        this.lastUpdateTime = LocalDateTime.now();
    }

    /**
     * 更改播放状态
     *
     * @param playStatus 新的播放状态
     */
    public void changePlayStatus(PlayStatus playStatus) {
        Assert.notNull(playStatus, "播放状态不能为空");

        if (Objects.equals(this.playStatus, playStatus)) {
            return; // 状态未变化，无需更新
        }

        PlayStatus oldPlayStatus = this.playStatus;
        this.playStatus = playStatus;
        this.lastUpdateTime = LocalDateTime.now();

        // 触发播放状态变更事件
        registerEvent(AnimeUpdatedEvent.playStatusUpdated(this, getId(), name,
                oldPlayStatus != null ? oldPlayStatus.toString() : null,
                playStatus.toString(), lastUpdateTime, this.lastUpdatedBy));
    }

    /**
     * 上架动漫
     *
     * <p>
     * 将动漫设置为上架状态，使其对用户可见
     * </p>
     *
     * @throws IllegalStateException 当动漫已删除时
     */
    public void putOnShelf() {
        if (Boolean.TRUE.equals(this.isDeleted)) {
            throw new IllegalStateException("已删除的动漫不能上架");
        }

        if (Boolean.TRUE.equals(this.isOnShelf)) {
            return; // 已经上架，无需重复操作
        }

        this.isOnShelf = true;
        this.lastUpdateTime = LocalDateTime.now();
        this.registerEvent(AnimeStatusChangedEvent.putOnShelf(this, getId(), name,
                "管理员上架操作", lastUpdateTime, this.lastUpdatedBy));
    }

    /**
     * 下架动漫
     *
     * <p>
     * 将动漫设置为下架状态，使其对用户不可见
     * </p>
     */
    public void takeOffShelf() {
        if (Boolean.FALSE.equals(this.isOnShelf)) {
            return; // 已经下架，无需重复操作
        }

        this.isOnShelf = false;
        this.lastUpdateTime = LocalDateTime.now();
        this.registerEvent(AnimeStatusChangedEvent.takeOffShelf(this, getId(), name,
                "管理员下架操作", lastUpdateTime, this.lastUpdatedBy));
    }

    /**
     * 删除动漫
     *
     * <p>
     * 软删除动漫，将其标记为已删除状态
     * </p>
     *
     * @throws IllegalStateException 当动漫仍在上架状态时
     */
    public void delete() {
        if (Boolean.TRUE.equals(this.isOnShelf)) {
            throw new IllegalStateException("已上架的动漫不能删除，请先下架");
        }

        if (Boolean.TRUE.equals(this.isDeleted)) {
            return; // 已经删除，无需重复操作
        }

        this.isDeleted = true;
        this.lastUpdateTime = LocalDateTime.now();
        this.registerEvent(AnimeStatusChangedEvent.delete(this, getId(), name,
                "管理员删除操作", lastUpdateTime, this.lastUpdatedBy));
    }

    /**
     * 恢复已删除的动漫
     *
     * <p>
     * 将已删除的动漫恢复为正常状态
     * </p>
     */
    public void restore() {
        if (Boolean.FALSE.equals(this.isDeleted)) {
            return; // 未删除，无需恢复
        }

        this.isDeleted = false;
        this.lastUpdateTime = LocalDateTime.now();
        registerEvent(new cn.chenyunlong.qing.anime.domain.anime.events.AnimeEvents.AnimeUpdated(this.getId()));
    }

    /**
     * 检查动漫是否可以上架
     *
     * @return true如果可以上架，false否则
     */
    public boolean canPutOnShelf() {
        return Boolean.FALSE.equals(this.isDeleted) &&
                StringUtils.hasText(this.name) &&
                this.animeCategory != null;
    }

    /**
     * 检查动漫是否可以删除
     *
     * @return true如果可以删除，false否则
     */
    public boolean canDelete() {
        return Boolean.FALSE.equals(this.isOnShelf);
    }

    // ==================== Status Check Methods ====================

    /**
     * 检查动漫是否已上架
     *
     * @return true如果已上架，false否则
     */
    public boolean isOnShelf() {
        return Boolean.TRUE.equals(this.isOnShelf);
    }

    /**
     * 检查动漫是否已删除
     *
     * @return true如果已删除，false否则
     */
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }

    /**
     * 检查动漫是否处于活跃状态（未删除且已上架）
     *
     * @return true如果处于活跃状态，false否则
     */
    public boolean isActive() {
        return !isDeleted() && isOnShelf();
    }

    /**
     * 检查动漫是否有标签
     *
     * @return true如果有标签，false否则
     */
    public boolean hasTags() {
        return tags != null && !tags.tags().isEmpty();
    }

    /**
     * 获取标签数量
     *
     * @return 标签数量
     */
    public int getTagCount() {
        return tags != null ? tags.tags().size() : 0;
    }

    /**
     * 检查是否包含指定标签
     *
     * @param tag 要检查的标签
     * @return true如果包含该标签，false否则
     */
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }

    // ==================== Business Rule Validation ====================

    /**
     * 验证动漫数据的完整性
     *
     * @throws IllegalStateException 当数据不完整时
     */
    public void validateIntegrity() {
        if (getId() == null) {
            throw new IllegalStateException("动漫ID不能为空");
        }
        if (!StringUtils.hasText(name)) {
            throw new IllegalStateException("动漫名称不能为空");
        }
        if (animeCategory == null) {
            throw new IllegalStateException("动漫分类不能为空");
        }
        if (playStatus == null) {
            throw new IllegalStateException("播放状态不能为空");
        }
    }

    @NonNull
    @Override
    public Collection<Object> domainEvents() {
        return super.domainEvents();
    }
}
