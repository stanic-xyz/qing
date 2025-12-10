package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermissionId;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RolePermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = {CustomMapper.class, DateMapper.class, AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RolePermissionMapper {
    RolePermissionEntity toEntity(RolePermission domain);

    RolePermission toDomain(RolePermissionEntity rolePermissionEntity);

    default RolePermissionId map2Id(Long value) {
        return new RolePermissionId(value);
    }

    default RoleId mapRoleId(Long value) {
        return new RoleId(value);
    }

    default PermissionId mapPermissionId(Long value) {
        return new PermissionId(value);
    }
}
