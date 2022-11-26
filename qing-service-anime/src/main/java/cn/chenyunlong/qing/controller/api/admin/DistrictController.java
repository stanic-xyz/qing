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

package cn.chenyunlong.qing.controller.api.admin;

import cn.chenyunlong.qing.domain.district.service.DistrictService;
import cn.chenyunlong.qing.infrastructure.model.ApiResult;
import cn.chenyunlong.qing.infrastructure.model.dto.DistrictDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.chenyunlong.qing.controller.base.ApiController.success;

/**
 * @author Stan
 */
@Validated
@Tag(name = "区域")
@RestController
@RequestMapping("api/districts")
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictService districtService;

    @GetMapping
    public ApiResult<List<DistrictDTO>> getDistrictList() {
        return success(districtService.getAllDistrict());
    }
}
