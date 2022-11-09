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

package cn.chenyunlong.qing.model.params;

import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.model.dto.base.InputConverter;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class AnimeInfoParam implements InputConverter<AnimeInfo> {

    @NotBlank(message = "动漫名称不能为空")
    @Size(max = 255, message = "文章标题的字符长度不能超过 {max}")
    private String name;
    private String instruction;
    private String district;
    private String coverUrl;
    private String type;
    @NotBlank(message = "原始名称不能为空")
    private String originalName;
    private String otherName;
    @NotBlank(message = "作者不能为空")
    private String author;
    @NotBlank(message = "公司名称不能为空")
    private String company;
    @NotEmpty(message = "出版日期不能为空")
    private LocalDate premiereDate;
    @NotEmpty(message = "播放状态不能为空")
    private String playStatus;
    private String plotType;
    private String tags;
    private String officialWebsite;
}
