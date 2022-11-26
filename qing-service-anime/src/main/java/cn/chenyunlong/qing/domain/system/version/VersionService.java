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

package cn.chenyunlong.qing.domain.system.version;

import cn.chenyunlong.qing.infrastructure.model.dto.VersionDTO;
import cn.chenyunlong.qing.infrastructure.model.entities.Version;
import cn.chenyunlong.qing.infrastructure.model.params.VersionParam;
import cn.chenyunlong.qing.infrastructure.service.base.CrudService;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author Stan
 */
public interface VersionService extends CrudService<Version> {
    /**
     * 获取版本列表
     *
     * @return 版本列表
     */
    List<VersionDTO> getAllVersions();

    /**
     * 获取服务详情
     *
     * @param versionId 颁布的信息ID
     * @return 服务代码
     */
    VersionDTO getDetailById(Long versionId);

    /**
     * 添加了审批
     *
     * @param param 添加参数
     * @param id    主键ID
     */
    void updateBy(VersionParam param, String id);

    /**
     * 创建服务
     *
     * @param version 创建版本信息
     * @return 备注
     */
    VersionDTO createBy(@NonNull Version version);
}
