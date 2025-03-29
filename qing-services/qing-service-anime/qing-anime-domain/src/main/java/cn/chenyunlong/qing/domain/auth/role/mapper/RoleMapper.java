package cn.chenyunlong.qing.domain.auth.role.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.domain.auth.role.dto.creator.RoleCreator;
import cn.chenyunlong.qing.domain.auth.role.dto.query.RoleQuery;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleCreateRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleQueryRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleUpdateRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.response.RoleResponse;
import cn.chenyunlong.qing.domain.auth.role.dto.updater.RoleUpdater;
import cn.chenyunlong.qing.domain.auth.role.dto.vo.RoleVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleUpdater request2Updater(RoleUpdateRequest request);

    RoleCreator request2Dto(RoleCreateRequest request);

    RoleQuery request2Query(RoleQueryRequest request);

    RoleResponse vo2Response(RoleVO vo);

    RoleResponse vo2CustomResponse(RoleVO vo);

    Role dtoToEntity(RoleCreator creator);
}
