package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionStatus;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionType;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PermissionEntity;
import cn.chenyunlong.qing.domain.common.AuditInfo;
import org.mapstruct.*;

/**
 * 权限实体映射器
 */
@Mapper(uses = {CustomMapper.class, DateMapper.class,
        AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {

    /**
     * 领域对象转换为JPA实体
     *
     * @param domain 权限领域对象
     * @return 权限JPA实体
     */
    PermissionEntity toEntity(Permission domain);

    /**
     * JPA实体转换为领域对象
     *
     * @param entity 权限JPA实体
     * @return 权限领域对象
     */
    @Mappings({
            @Mapping(source = "version", target = "version")
    })
    Permission entity2Domain(PermissionEntity entity);

    default PermissionId map2PermissionId(Long value) {
        return value == null ? null : new PermissionId(value);
    }

    /**
     * 实体转领域后的补全映射
     *
     * @param entity 源实体
     * @param domain 目标领域对象
     */
    @AfterMapping
    default void afterToDomain(PermissionEntity entity, @MappingTarget Permission domain) {
        if (domain != null && entity != null) {

            domain.setAuditInfo(AuditInfo.restore(entity.getCreateBy(), entity.getCreatedAt(), entity.getUpdatedBy(), entity.getUpdatedAt()));

            domain.setId(map2PermissionId(entity.getId()));
            domain.setCode(entity.getCode());
            domain.setName(entity.getName());
            domain.setDescription(entity.getDescription());
            domain.setType(PermissionType.valueOf(entity.getType()));
            domain.setStatus(PermissionStatus.valueOf(entity.getStatus()));
            domain.setResource(entity.getResource());
            domain.setAction(entity.getAction());
            domain.setSortOrder(entity.getSortOrder());
            domain.setParent(map2PermissionId(entity.getParentId()));
        }
    }

    /**
     * 领域转实体前的字段填充
     *
     * @param domain 源领域对象
     * @param entity 目标实体
     */
    @BeforeMapping
    default void beforeToEntity(Permission domain, @MappingTarget PermissionEntity entity) {
        if (domain != null && entity != null) {
            entity.setId(domain.getId() != null ? domain.getId().id() : null);
            entity.setCode(domain.getCode());
            entity.setName(domain.getName());
            entity.setDescription(domain.getDescription());
            entity.setType(domain.getType() != null ? domain.getType().name() : null);
            entity.setStatus(domain.getStatus() != null ? domain.getStatus().name() : null);
            entity.setResource(domain.getResource());
            entity.setAction(domain.getAction());
            entity.setSortOrder(domain.getSortOrder());
            entity.setParentId(domain.getParentId() != null ? domain.getParentId().id() : null);
        }
    }
}
