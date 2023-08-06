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

// ---Auto Generated by Qing-Generator --

package cn.chenyunlong.qing.samples.codegen.domain.mapper;

import cn.chenyunlong.qing.samples.codegen.domain.TestDomain;
import cn.chenyunlong.qing.samples.codegen.domain.creator.TestDomainCreator;
import cn.chenyunlong.qing.samples.codegen.domain.query.TestDomainQuery;
import cn.chenyunlong.qing.samples.codegen.domain.request.TestDomainCreateRequest;
import cn.chenyunlong.qing.samples.codegen.domain.request.TestDomainQueryRequest;
import cn.chenyunlong.qing.samples.codegen.domain.request.TestDomainUpdateRequest;
import cn.chenyunlong.qing.samples.codegen.domain.response.TestDomainResponse;
import cn.chenyunlong.qing.samples.codegen.domain.updater.TestDomainUpdater;
import cn.chenyunlong.qing.samples.codegen.domain.vo.TestDomainVO;
import cn.hutool.core.bean.BeanUtil;

public interface TestDomainMapper {
    TestDomainMapper INSTANCE = new TestDomainMapper() {
    };

    default TestDomain dtoToEntity(TestDomainCreator dto) {
        return BeanUtil.copyProperties(dto, TestDomain.class);
    }

    default TestDomainUpdater request2Updater(TestDomainUpdateRequest request) {
        return BeanUtil.copyProperties(request, TestDomainUpdater.class);
    }

    default TestDomainCreator request2Dto(TestDomainCreateRequest request) {
        return BeanUtil.copyProperties(request, TestDomainCreator.class);
    }

    default TestDomainQuery request2Query(TestDomainQueryRequest request) {
        return BeanUtil.copyProperties(request, TestDomainQuery.class);
    }

    default TestDomainResponse vo2Response(TestDomainVO vo) {
        return BeanUtil.copyProperties(vo, TestDomainResponse.class);
    }

    default TestDomainResponse vo2CustomResponse(TestDomainVO vo) {
        return vo2Response(vo);
    }
}