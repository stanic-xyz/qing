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

package cn.chenyunlong.codegen.test.domain.testobject;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * 测试代码自动生成的结果
 *
 * @author gim 2022/1/11 10:53 下午
 * @date 2022/10/25
 */

@GenBase(basePackage = "cn.chenyunlong.codegen.test.domain.testobject.gen")
@GenController
@GenVo
@GenCreator
@GenCreateRequest
@GenUpdater
@GenUpdateRequest
@GenMapper
@GenFeign
@GenQueryRequest
@GenQuery
@GenResponse
@GenRepository
@GenService
@GenServiceImpl
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class TestQueryObject extends BaseEntity {

    @QueryItem
    @FieldDesc(name = "用户名")
    private String username;


    @QueryItem
    @FieldDesc(name = "用户名")
    private String password;

    @IgnoreCreator
    @FieldDesc(name = "文件名称", description = "文件名称描述信息")
    private String filename;

}