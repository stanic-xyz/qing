package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.qing.auth.domain.user.UserConnection;
import cn.chenyunlong.qing.auth.domain.user.UserConnectionData;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.GenericEnumMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    DateMapper.class,
    GenericEnumMapper.class
})
public interface UserConnectionMapper {

    UserConnectionMapper INSTANCE = Mappers.getMapper(UserConnectionMapper.class);

    UserConnectionData entityToConnectionData(UserConnection connection);
}
