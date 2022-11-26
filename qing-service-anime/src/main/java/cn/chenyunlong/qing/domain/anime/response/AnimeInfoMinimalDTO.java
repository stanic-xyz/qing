/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.qing.domain.anime.response;

import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.infrastructure.model.dto.base.OutputConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author Stan
 */
@Data
@ToString
@EqualsAndHashCode
public class AnimeInfoMinimalDTO implements OutputConverter<AnimeInfoMinimalDTO, AnimeInfo> {

    private Long id;
    private String name;
    private String coverUrl;
    private String typeName;
    private String originalName;
    private String otherName;
    private String author;
    private String company;
    private LocalDate premiereDate;
    private String playStatus;
    private String plotType;
    private Boolean isNew = false;
}
