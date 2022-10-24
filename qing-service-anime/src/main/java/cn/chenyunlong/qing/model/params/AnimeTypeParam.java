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

import cn.chenyunlong.qing.model.entities.AnimeType;
import cn.chenyunlong.qing.model.dto.base.InputConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AnimeTypeParam implements InputConverter<AnimeType> {

    @ApiModelProperty("类型名称")
    @Size(max = 10, message = "长度不能超过{max}个字符")
    @NotBlank(message = "类型名称不能为空")
    private String name;
    @ApiModelProperty("类型描述")
    private String description;
    @ApiModelProperty("排序号")
    private Integer orderNo = 0;
}