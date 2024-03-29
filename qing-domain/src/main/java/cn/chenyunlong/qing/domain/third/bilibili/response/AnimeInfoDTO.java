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

package cn.chenyunlong.qing.domain.third.bilibili.response;

import cn.chenyunlong.common.model.dto.base.OutputConverter;
import cn.chenyunlong.qing.domain.third.bilibili.BiliAnimeInfoEntity;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class AnimeInfoDTO implements OutputConverter<AnimeInfoDTO, BiliAnimeInfoEntity> {

    private Long id;
    private Long mediaId;
    private String title;
    private Long seasonId;
    private String cover;
    private Integer isFinished;
    private String indexShow;
    private String order;
    private String link;

    @NonNull
    @Override
    public <T extends AnimeInfoDTO> T convertFrom(@NonNull BiliAnimeInfoEntity animeInfo) {
        this.setOrder(animeInfo.getScore() + "分");
        return OutputConverter.super.convertFrom(animeInfo);
    }
}
