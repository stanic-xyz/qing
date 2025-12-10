package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRoleId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.UserRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        DateMapper.class,
        AggregateMapper.class
})
public interface UserRoleMapper {
    UserRoleEntity toEntity(UserRole domain);

    UserRole toDomain(UserRoleEntity userRoleEntity);

    default UserRoleId map(Long value) {
        return new UserRoleId(value);
    }

    default UserId mapUserId(Long value) {
        return new UserId(value);
    }

    /**
     * 将Long类型的值映射为UserId对象
     * 这是一个默认方法，用于将Long类型的值转换为UserId类型的实例
     *
     * @param value 需要转换的Long类型值
     * @return 返回一个新的UserId实例，使用传入的value作为参数
     */
    default RoleId mapRoleId(Long value) {    // 定义一个默认方法，用于将Long类型转换为UserId类型
        return new RoleId(value);    // 创建并返回一个新的UserId对象，使用传入的value作为构造参数
    }
}
