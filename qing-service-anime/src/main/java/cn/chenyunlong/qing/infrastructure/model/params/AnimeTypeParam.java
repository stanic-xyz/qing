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

import cn.chenyunlong.qing.domain.anime.type.AnimeType;
import cn.chenyunlong.qing.infrastructure.model.dto.base.InputConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnimeTypeParam implements InputConverter<AnimeType> {

    @Schema(name = "类型名称")
    @Size(max = 10, message = "长度不能超过{max}个字符")
    @NotBlank(message = "类型名称不能为空")
    private String name;
    @Schema(name = "类型描述")
    private String description;
    @Schema(name = "排序号")
    private Integer orderNo = 0;
}
