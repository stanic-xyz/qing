package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.auth.domain.platform.PlatformId;
import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.role.dto.creator.RoleCreator;
import cn.chenyunlong.qing.auth.domain.role.dto.request.RoleCreateRequest;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.BaseEntityMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity;
import org.mapstruct.*;

@Mapper(uses = {CustomMapper.class, DateMapper.class, AggregateMapper.class, BaseEntityMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    RoleCreator request2Dto(RoleCreateRequest request);

    default PlatformId map(Long value) {
        return new PlatformId(value);
    }

    RoleEntity domain2Entity(Role domain);

    Role entity2Domain(RoleEntity roleEntity);

    default RoleId map2RoleId(Long value) {
        return RoleId.of(value);
    }
}
