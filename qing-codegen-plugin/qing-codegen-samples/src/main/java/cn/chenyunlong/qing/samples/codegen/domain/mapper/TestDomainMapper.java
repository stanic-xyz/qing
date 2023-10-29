package cn.chenyunlong.qing.samples.codegen.domain.mapper;

import cn.chenyunlong.qing.samples.codegen.domain.TestDomain;
import cn.chenyunlong.qing.samples.codegen.domain.dto.creator.TestDomainCreator;
import cn.chenyunlong.qing.samples.codegen.domain.dto.query.TestDomainQuery;
import cn.chenyunlong.qing.samples.codegen.domain.dto.request.TestDomainCreateRequest;
import cn.chenyunlong.qing.samples.codegen.domain.dto.request.TestDomainQueryRequest;
import cn.chenyunlong.qing.samples.codegen.domain.dto.request.TestDomainUpdateRequest;
import cn.chenyunlong.qing.samples.codegen.domain.dto.response.TestDomainResponse;
import cn.chenyunlong.qing.samples.codegen.domain.dto.updater.TestDomainUpdater;
import cn.chenyunlong.qing.samples.codegen.domain.dto.vo.TestDomainVO;
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
