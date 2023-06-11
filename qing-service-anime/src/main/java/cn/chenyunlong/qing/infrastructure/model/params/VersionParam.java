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

import cn.chenyunlong.qing.domain.system.version.Version;
import cn.chenyunlong.qing.infrastructure.model.dto.base.InputConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VersionParam implements InputConverter<Version> {

    @NotBlank
    @Schema(name = "版本代码")
    private String code;

    @NotBlank
    @Schema(name = "版本名称")
    private String name;

    @NotBlank
    @Schema(name = "版本描述信息")
    private String description;
}
