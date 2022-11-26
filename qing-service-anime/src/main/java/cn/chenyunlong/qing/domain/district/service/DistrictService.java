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

package cn.chenyunlong.qing.domain.district.service;

import cn.chenyunlong.qing.domain.district.District;
import cn.chenyunlong.qing.infrastructure.model.dto.DistrictDTO;
import cn.chenyunlong.qing.infrastructure.service.base.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 * @date 2020/0124
 */
@Service
public interface DistrictService extends CrudService<District> {
    /**
     * 获取所有的地区信息
     *
     * @return 所有的地区信息
     */
    List<DistrictDTO> getAllDistrict();
}
