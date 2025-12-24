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

package cn.chenyunlong.qing.anime.domain.watchhistory;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 观看历史聚合根
 * 记录用户观看动漫的历史记录
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class WatchHistory extends BaseSimpleBusinessEntity<WatchHistoryId> {

    @FieldDesc(description = "用户ID")
    private Long userId;

    @FieldDesc(description = "动漫ID")
    private Long animeId;

    @FieldDesc(description = "剧集ID")
    private Long episodeId;

    @FieldDesc(description = "观看时间")
    private LocalDateTime watchTime;

    @FieldDesc(description = "观看时长(秒)")
    private Integer watchDuration;

    @FieldDesc(description = "设备类型")
    private String deviceType;

    @FieldDesc(description = "IP地址")
    private String ipAddress;

    @FieldDesc(description = "用户代理")
    private String userAgent;

    /**
     * 受保护的构造函数
     */
    protected WatchHistory() {}

    /**
     * 创建观看历史记录
     */
    public static WatchHistory create(Long userId, Long animeId, Long episodeId,
                                      Integer watchDuration, String deviceType,
                                      String ipAddress, String userAgent) {
        WatchHistory history = new WatchHistory();
        history.userId = userId;
        history.animeId = animeId;
        history.episodeId = episodeId;
        history.watchTime = LocalDateTime.now();
        history.watchDuration = watchDuration;
        history.deviceType = deviceType;
        history.ipAddress = ipAddress;
        history.userAgent = userAgent;
        return history;
    }

    /**
     * 更新观看时长
     */
    public void updateWatchDuration(Integer additionalDuration) {
        if (additionalDuration > 0) {
            this.watchDuration += additionalDuration;
        }
    }
}
