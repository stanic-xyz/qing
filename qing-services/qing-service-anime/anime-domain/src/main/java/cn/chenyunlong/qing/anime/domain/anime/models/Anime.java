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
import cn.chenyunlong.qing.anime.domain.anime.events.AnimeEvents;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 动漫信息。
 *
 * @author 陈云龙
 */

@Getter
@Setter
public class Anime extends BaseAggregate {

    // 最大不超过十个标签
    private static final int MAX_TAGS = 10;
    @FieldDesc(description = "聚合根标识")
    private AnimeId animeId;

    @FieldDesc(description = "分类")
    private AnimeCategory animeCategory;
    @FieldDesc(description = "名称")
    private String name;
    @FieldDesc(description = "介绍")
    private String instruction;
    @FieldDesc(description = "区域Id")
    private AnimeDistrict district;
    @FieldDesc(description = "封面地址")
    private String cover;
    @FieldDesc(description = "类型")
    private AnimeType type;
    @FieldDesc(description = "类型名称")
    private String typeName;
    @FieldDesc(description = "原始名称")
    private String originalName;
    @FieldDesc(description = "其他名称")
    private String otherName;
    @FieldDesc(description = "作者")
    private String author;
    @FieldDesc(description = "公司")
    private Company company;
    @FieldDesc(description = "发布日期")
    private PremiereDate premiereDate;
    @FieldDesc(description = "播放状态")
    private PlayStatus playStatus;
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
    private boolean isOnShelf; // 是否上架
    private boolean isDeleted; // 是否删除

    /**
     * 受保护的构造函数，防止外部直接实例化
     */
    public Anime() {}

    public void create() {
        registerEvent(new AnimeEvents.AnimeCreated(getAnimeId()));
    }

    public void update(String name, String instruction) {
        setName(name);
        setInstruction(instruction);
        setLastUpdateTime(LocalDateTime.now());
    }

    public void update(String name,
                       String instruction,
                       AnimeDistrict district,
                       AnimeCategory category,
                       List<String> tags,
                       String cover,
                       String originalName,
                       String otherName,
                       String author,
                       Long companyId,
                       String companyName,
                       LocalDate premiereDate,
                       PlayStatus playStatus,
                       String plotType,
                       String officialWebsite,
                       String playHeat,
                       Integer orderNo) {
        // 1. 校验输入合法性
        validateTags(tags);

        // 2. 更新字段
        this.name = name;
        this.instruction = instruction;
        this.district = district;
        this.tags = Tags.create(tags); // 转换为值对象
        this.cover = cover;
        this.originalName = originalName;
        this.otherName = otherName;
        this.author = author;
        this.playStatus = playStatus;
        this.officialWebsite = officialWebsite;
        this.playHeat = playHeat;
        this.orderNo = orderNo;

        // 3. 触发领域事件
        registerEvent(new AnimeEvents.AnimeUpdated(this.getAnimeId()));
    }

    private void validateTags(List<String> tags) {
        if (tags == null || tags.size() > MAX_TAGS) {
            throw new IllegalArgumentException("标签数量超出限制");
        }
    }

    public void addTag(String tag) {
        List<String> newTags = new ArrayList<>(this.getTags().tags());
        newTags.add(tag);
        this.tags = new Tags(newTags);
    }

    public void removeTag(String tag) {
        List<String> newTags = new ArrayList<>(this.getTags().tags());
        newTags.remove(tag);
        this.tags = new Tags(newTags);
    }

    public void changePlayStatus(PlayStatus playStatus) {
        setPlayStatus(playStatus);
        setLastUpdateTime(LocalDateTime.now());
    }

    // 上架动漫
    public void putOnShelf() {
        if (this.isDeleted) {
            throw new IllegalStateException("已删除的动漫不能上架");
        }
        this.isOnShelf = true;
        this.registerEvent(new AnimeEvents.AnimePutOn(getAnimeId()));
    }

    // 下架动漫
    public void takeOffShelf() {
        this.isOnShelf = false;
        this.registerEvent(new AnimeEvents.AnimeTakenOff(getAnimeId()));
    }

    // 删除动漫
    public void delete() {
        if (this.isOnShelf) {
            throw new IllegalStateException("已上架的动漫不能删除");
        }
        this.isDeleted = true;
        this.registerEvent(new AnimeEvents.AnimeDeleted(getAnimeId()));
    }

    @NonNull
    @Override
    public Collection<Object> domainEvents() {
        return super.domainEvents();
    }
}
