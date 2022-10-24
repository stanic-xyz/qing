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

package cn.chenyunlong.codegen.test;

import cn.chenyunlong.codegen.processor.api.*;
import cn.chenyunlong.codegen.processor.controller.GenController;
import cn.chenyunlong.codegen.processor.creator.GenCreator;
import cn.chenyunlong.codegen.processor.creator.IgnoreCreator;
import cn.chenyunlong.codegen.processor.mapper.GenMapper;
import cn.chenyunlong.codegen.processor.query.GenQuery;
import cn.chenyunlong.codegen.processor.repository.GenRepository;
import cn.chenyunlong.codegen.processor.service.GenService;
import cn.chenyunlong.codegen.processor.service.GenServiceImpl;
import cn.chenyunlong.codegen.processor.updater.GenUpdater;
import cn.chenyunlong.codegen.processor.updater.IgnoreUpdater;
import cn.chenyunlong.codegen.processor.vo.GenVo;
import cn.chenyunlong.common.constants.ValidStatus;
import com.only4play.jpa.converter.ValidStatusConverter;
import com.only4play.jpa.support.BaseJpaAggregate;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@GenVo(pkgName = "cn.chenyunlong.codegen.test.vo")
@GenCreator(pkgName = "cn.chenyunlong.codegen.test.creator")
@GenUpdater(pkgName = "cn.chenyunlong.codegen.test.updater")
@GenRepository(pkgName = "cn.chenyunlong.codegen.test.repository")
@GenService(pkgName = "cn.chenyunlong.codegen.test.service")
@GenServiceImpl(pkgName = "cn.chenyunlong.codegen.test.service")
@GenQuery(pkgName = "cn.chenyunlong.codegen.test.query")
@GenMapper(pkgName = "cn.chenyunlong.codegen.test.mapper")
@GenController(pkgName = "cn.chenyunlong.codegen.test.controller")
@GenCreateRequest(pkgName = "cn.chenyunlong.codegen.test.api.request", sourcePath = Constants.GEN_API_SOURCE)
@GenUpdateRequest(pkgName = "cn.chenyunlong.codegen.test.api.request", sourcePath = Constants.GEN_API_SOURCE)
@GenQueryRequest(pkgName = "cn.chenyunlong.codegen.test.api.request", sourcePath = Constants.GEN_API_SOURCE)
@GenResponse(pkgName = "cn.chenyunlong.codegen.test.api.response", sourcePath = Constants.GEN_API_SOURCE)
@GenFeign(pkgName = "cn.chenyunlong.codegen.test.api.service", sourcePath = Constants.GEN_API_SOURCE, serverName = "srv")
@Entity
@Table(name = "")
@Data
public class Student extends BaseJpaAggregate {

    @Convert(converter = ValidStatusConverter.class)
    @IgnoreUpdater
    @IgnoreCreator
    private ValidStatus validStatus;

    public void init() {
        setValidStatus(ValidStatus.VALID);
    }

    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }
}
