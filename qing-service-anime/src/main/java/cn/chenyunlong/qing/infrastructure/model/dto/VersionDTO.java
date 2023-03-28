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

package cn.chenyunlong.qing.infrastructure.model.dto;

import cn.chenyunlong.qing.domain.system.version.Version;
import cn.chenyunlong.qing.infrastructure.model.dto.base.OutputConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VersionDTO implements OutputConverter<VersionDTO, Version> {

    @Schema(name = "版本ID")
    private Long vid;

    @Schema(name = "版本代码")
    private String code;

    @Schema(name = "版本名称")
    private String name;

    @Schema(name = "版本描述信息")
    private String description;
}
