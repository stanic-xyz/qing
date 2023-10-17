/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.samples.codegen.domain;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import cn.chenyunlong.qing.samples.codegen.Constants;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@GenVo
@GenCreator
@GenUpdater
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenMapper(sourcePath = Constants.GEN_API_SOURCE)
@GenRepository(sourcePath = Constants.GEN_API_SOURCE)
@GenService(sourcePath = Constants.GEN_API_SOURCE)
@GenServiceImpl(sourcePath = Constants.GEN_API_SOURCE)
@GenFeign(sourcePath = Constants.GEN_API_SOURCE, serverName = "stanic")
@GenController(sourcePath = Constants.GEN_API_SOURCE)
@ToString
@RequiredArgsConstructor
@Entity
public class TestDomain extends BaseEntity {

    private String username;

    private String password;
}

