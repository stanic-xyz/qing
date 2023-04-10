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

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
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
@GenController
@GenRepository
@GenService
@GenServiceImpl
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenFeign
@GenMapper
@Entity
@Table(name = "activity")
@Data
@EqualsAndHashCode(callSuper = true)
public class Activity extends BaseEntity {

    @FieldDesc(description = "活动ID")
    private String activityId;

    private String username;
    private String activityName;
    private String activityContent;
    private String attachmentAddress;
    private Integer attachmentType;
    private Integer publishState;
}
