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

package cn.chenyunlong.qing.infrastructure.model.params;

import cn.chenyunlong.qing.domain.anime.AnimeRecommendEntity;
import cn.chenyunlong.qing.infrastructure.model.dto.base.InputConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "查询推荐信息的请求参数信息")
@Data
public class AnimeRecommendParam implements InputConverter<AnimeRecommendEntity> {

    @Schema(title = "动漫ID")
    @NotNull(message = "动漫ID不能为空")
    private Long aid;
    @NotBlank(message = "推荐理由必填")
    @Size(max = 255, message = "推荐理由不能超过{max}个字符")
    @Schema(title = "推荐理由")
    private String reason;
    @Schema(title = "排序号", description = "排序号的说明")
    private Integer orderNo;
}
