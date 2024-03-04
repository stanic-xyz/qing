package cn.chenyunlong.qing.domain.auth.connection.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import cn.chenyunlong.qing.domain.auth.connection.dto.creator.UserConnectionCreator;
import cn.chenyunlong.qing.domain.auth.connection.dto.query.UserConnectionQuery;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionCreateRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionQueryRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionUpdateRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.response.UserConnectionResponse;
import cn.chenyunlong.qing.domain.auth.connection.dto.updater.UserConnectionUpdater;
import cn.chenyunlong.qing.domain.auth.connection.dto.vo.UserConnectionVO;
import cn.chenyunlong.security.entity.ConnectionData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    DateMapper.class,
    GenericEnumMapper.class
})
public interface UserConnectionMapper {

    UserConnectionMapper INSTANCE = Mappers.getMapper(UserConnectionMapper.class);

    UserConnection dtoToEntity(UserConnectionCreator dto);

    UserConnectionUpdater request2Updater(UserConnectionUpdateRequest request);

    UserConnectionCreator request2Dto(UserConnectionCreateRequest request);

    UserConnectionQuery request2Query(UserConnectionQueryRequest request);

    ConnectionData entityToConnectionData(UserConnection userConnection);

    UserConnectionResponse vo2Response(UserConnectionVO vo);

    UserConnectionResponse vo2CustomResponse(UserConnectionVO vo);
}
