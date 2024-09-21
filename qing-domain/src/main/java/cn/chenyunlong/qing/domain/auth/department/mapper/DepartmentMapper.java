package cn.chenyunlong.qing.domain.auth.department.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.auth.department.Department;
import cn.chenyunlong.qing.domain.auth.department.dto.creator.DepartmentCreator;
import cn.chenyunlong.qing.domain.auth.department.dto.query.DepartmentQuery;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentCreateRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentQueryRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentUpdateRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.response.DepartmentResponse;
import cn.chenyunlong.qing.domain.auth.department.dto.updater.DepartmentUpdater;
import cn.chenyunlong.qing.domain.auth.department.dto.vo.DepartmentVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    CustomMapper.class,
    DateMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    Department dtoToEntity(DepartmentCreator dto);

    DepartmentUpdater request2Updater(DepartmentUpdateRequest request);

    DepartmentCreator request2Dto(DepartmentCreateRequest request);

    DepartmentQuery request2Query(DepartmentQueryRequest request);

    DepartmentResponse vo2Response(DepartmentVO vo);

    DepartmentResponse vo2CustomResponse(DepartmentVO vo);
}
