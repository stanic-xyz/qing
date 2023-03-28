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

package cn.chenyunlong.qing.domain.activity;

import cn.chenyunlong.codegen.processor.GenBase;
import cn.chenyunlong.codegen.processor.api.*;
import cn.chenyunlong.codegen.processor.controller.GenController;
import cn.chenyunlong.codegen.processor.creator.GenCreator;
import cn.chenyunlong.codegen.processor.mapper.GenMapper;
import cn.chenyunlong.codegen.processor.query.GenQuery;
import cn.chenyunlong.codegen.processor.repository.GenRepository;
import cn.chenyunlong.codegen.processor.service.GenService;
import cn.chenyunlong.codegen.processor.service.GenServiceImpl;
import cn.chenyunlong.codegen.processor.updater.GenUpdater;
import cn.chenyunlong.codegen.processor.vo.GenVo;
import cn.chenyunlong.qing.infrastructure.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Stan
 */
@GenBase(basePackage = "cn.chenyunlong.qing.domain.activity")
@GenVo
@GenCreator
@GenUpdater
@GenRepository
@GenService
@GenServiceImpl
@GenController(name = "活动信息")
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenFeign
@GenMapper
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "activity")
public class Activity extends BaseEntity {
    private String activityId;
    private String username;
    private String activityName;
    private String activityContent;
    private String attachmentAddress;
    private Integer attachmentType;
    private Integer publishState;
}
