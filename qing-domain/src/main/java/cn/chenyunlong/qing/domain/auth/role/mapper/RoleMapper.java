package cn.chenyunlong.qing.domain.auth.role.mapper;

import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.domain.auth.role.dto.creator.RoleCreator;
import cn.chenyunlong.qing.domain.auth.role.dto.query.RoleQuery;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleCreateRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleQueryRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleUpdateRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.response.RoleResponse;
import cn.chenyunlong.qing.domain.auth.role.dto.updater.RoleUpdater;
import cn.chenyunlong.qing.domain.auth.role.dto.vo.RoleVO;
import cn.hutool.core.bean.BeanUtil;

public interface RoleMapper {

    RoleMapper INSTANCE = new RoleMapper() {

    };

    default Role dtoToEntity(RoleCreator dto) {
        return BeanUtil.copyProperties(dto, Role.class);
    }

    default RoleUpdater request2Updater(RoleUpdateRequest request) {
        return BeanUtil.copyProperties(request, RoleUpdater.class);
    }

    default RoleCreator request2Dto(RoleCreateRequest request) {
        return BeanUtil.copyProperties(request, RoleCreator.class);
    }

    default RoleQuery request2Query(RoleQueryRequest request) {
        return BeanUtil.copyProperties(request, RoleQuery.class);
    }

    default RoleResponse vo2Response(RoleVO vo) {
        return BeanUtil.copyProperties(vo, RoleResponse.class);
    }

    default RoleResponse vo2CustomResponse(RoleVO vo) {
        return vo2Response(vo);
    }
}
