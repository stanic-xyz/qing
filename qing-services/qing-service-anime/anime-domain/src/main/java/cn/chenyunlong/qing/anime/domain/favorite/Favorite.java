/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.anime.domain.favorite;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 收藏夹聚合根
 * 管理用户对动漫的收藏
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class Favorite extends BaseAggregate {

    @FieldDesc(description = "用户ID")
    private Long userId;

    @FieldDesc(description = "动漫ID")
    private Long animeId;

    @FieldDesc(description = "收藏时间")
    private LocalDateTime favoriteTime;

    @FieldDesc(description = "收藏分组")
    private String favoriteGroup;

    @FieldDesc(description = "备注")
    private String remark;

    @FieldDesc(description = "是否公开")
    private Boolean isPublic;

    /**
     * 受保护的构造函数
     */
    protected Favorite() {}

    /**
     * 创建收藏记录
     */
    public static Favorite create(Long userId, Long animeId, String favoriteGroup, String remark, Boolean isPublic) {
        Favorite favorite = new Favorite();
        favorite.userId = userId;
        favorite.animeId = animeId;
        favorite.favoriteTime = LocalDateTime.now();
        favorite.favoriteGroup = favoriteGroup != null ? favoriteGroup : "默认分组";
        favorite.remark = remark;
        favorite.isPublic = isPublic != null ? isPublic : false;
        return favorite;
    }

    /**
     * 更新收藏分组
     */
    public void updateFavoriteGroup(String favoriteGroup) {
        this.favoriteGroup = favoriteGroup;
    }

    /**
     * 更新备注
     */
    public void updateRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 设置公开状态
     */
    public void setPublicStatus(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}