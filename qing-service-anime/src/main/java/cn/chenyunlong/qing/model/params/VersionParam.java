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

import cn.chenyunlong.qing.model.entities.Version;
import cn.chenyunlong.qing.model.dto.base.InputConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VersionParam implements InputConverter<Version> {

    @NotBlank
    @ApiModelProperty("版本代码")
    private String code;

    @NotBlank
    @ApiModelProperty("版本名称")
    private String name;

    @NotBlank
    @ApiModelProperty("版本描述信息")
    private String description;
}
