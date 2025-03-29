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

package cn.chenyunlong.qing.domain.anime.anime.models;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import cn.chenyunlong.qing.domain.anime.anime.events.AnimeEvents;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;

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

    @Nonnull
    @Override
    public Collection<Object> domainEvents() {
        return super.domainEvents();
    }
}
