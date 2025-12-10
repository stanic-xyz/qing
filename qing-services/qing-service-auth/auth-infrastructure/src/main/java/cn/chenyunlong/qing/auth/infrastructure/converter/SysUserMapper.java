package cn.chenyunlong.qing.auth.infrastructure.converter;


import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Email;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.DateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class, AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserMapper {

    SysUserMapper INSTANCE = Mappers.getMapper(SysUserMapper.class);

    default String map(Username value) {
        return value != null ? value.value() : null;
    }

    default String map(Email value) {
        return value != null ? value.value() : null;
    }
}
