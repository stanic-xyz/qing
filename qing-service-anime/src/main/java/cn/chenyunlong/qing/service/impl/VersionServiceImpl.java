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

package cn.chenyunlong.qing.service.impl;

import cn.chenyunlong.qing.mapper.VersionMapper;
import cn.chenyunlong.qing.model.dto.VersionDTO;
import cn.chenyunlong.qing.model.entities.Version;
import cn.chenyunlong.qing.model.params.VersionParam;
import cn.chenyunlong.qing.service.VersionService;
import cn.chenyunlong.qing.service.base.AbstractCrudService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (Version)表服务实现类
 *
 * @author makejava
 * @since 2022-05-21 23:24:50
 */
@Service
public class VersionServiceImpl extends AbstractCrudService<VersionMapper, Version> implements VersionService {

    @Override
    public List<VersionDTO> getAllVersions() {
        return toListDto(lambdaQuery().list(), VersionDTO.class);
    }

    @Override
    public VersionDTO getDetailById(Long versionId) {
        return toDto(getById(versionId), VersionDTO.class);
    }

    @Transactional
    @Override
    public void updateBy(VersionParam param, String id) {
        Version version = mustExistById(id);
        param.update(version);
        updateById(version);
    }

    @Override
    public VersionDTO createBy(@NonNull Version version) {
        return toDto(super.create(version), VersionDTO.class);
    }
}

