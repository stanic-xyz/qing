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
import cn.chenyunlong.qing.domain.district.mapper.DistrictMapper;
import cn.chenyunlong.qing.infrastructure.model.dto.DistrictDTO;
import cn.chenyunlong.qing.infrastructure.service.base.AbstractCrudService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stan
 */
@Service
public class DistrictServiceImpl extends AbstractCrudService<DistrictMapper, District> implements DistrictService {

    public DistrictServiceImpl(DistrictMapper districtMapper) {
    }

    @Override
    public List<DistrictDTO> getAllDistrict() {
        return lambdaQuery().list().stream().map(domain -> {
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.convertFrom(domain);
            return districtDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean removeById(@NonNull Serializable id) {
        District district = mustExistById(id);
        return super.removeById(district);
    }
}
