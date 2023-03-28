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

package cn.chenyunlong.qing.infrastructure.model.params;

import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.infrastructure.model.dto.base.InputConverter;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddEpisodeParam implements InputConverter<Episode> {
    private Long animeId;
    @NotBlank(message = "视频名称不能为空")
    private String name;
    private String uploaderName;
    private Long uploaderId;
    @NotBlank(message = "播放地址不能为空")
    private String url;
    private Integer orderNo;
}
