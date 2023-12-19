package cn.chenyunlong.qing.domain.auth.department.mapper;

import cn.chenyunlong.qing.domain.auth.department.Department;
import cn.chenyunlong.qing.domain.auth.department.dto.creator.DepartmentCreator;
import cn.chenyunlong.qing.domain.auth.department.dto.query.DepartmentQuery;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentCreateRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentQueryRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentUpdateRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.response.DepartmentResponse;
import cn.chenyunlong.qing.domain.auth.department.dto.updater.DepartmentUpdater;
import cn.chenyunlong.qing.domain.auth.department.dto.vo.DepartmentVO;
import cn.hutool.core.bean.BeanUtil;

public interface DepartmentMapper {
    DepartmentMapper INSTANCE = new DepartmentMapper() {
    };

    default Department dtoToEntity(DepartmentCreator dto) {
        return BeanUtil.copyProperties(dto, Department.class);
    }

    default DepartmentUpdater request2Updater(DepartmentUpdateRequest request) {
        return BeanUtil.copyProperties(request, DepartmentUpdater.class);
    }

    default DepartmentCreator request2Dto(DepartmentCreateRequest request) {
        return BeanUtil.copyProperties(request, DepartmentCreator.class);
    }

    default DepartmentQuery request2Query(DepartmentQueryRequest request) {
        return BeanUtil.copyProperties(request, DepartmentQuery.class);
    }

    default DepartmentResponse vo2Response(DepartmentVO vo) {
        return BeanUtil.copyProperties(vo, DepartmentResponse.class);
    }

    default DepartmentResponse vo2CustomResponse(DepartmentVO vo) {
        return vo2Response(vo);
    }
}
