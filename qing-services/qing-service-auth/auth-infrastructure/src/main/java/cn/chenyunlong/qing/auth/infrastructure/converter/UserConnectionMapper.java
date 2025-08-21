package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.auth.domain.user.QingConnectionData;
import cn.chenyunlong.qing.auth.domain.user.UserConnection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    DateMapper.class,
    GenericEnumMapper.class
})
public interface UserConnectionMapper {

    UserConnectionMapper INSTANCE = Mappers.getMapper(UserConnectionMapper.class);

    QingConnectionData entityToConnectionData(UserConnection connection);
}
