package cn.chenyunlong.qing.domain.auth.user.mapper;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserCreateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserQueryRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserUpdateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.response.UserResponse;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    GenericEnumMapper.class,
    DateMapper.class,
    CustomMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    QingUser dtoToEntity(UserCreator dto);

    UserUpdater request2Updater(UserUpdateRequest request);

    @Mapping(source = "password.password", target = "password")
    UserCreator request2Dto(UserCreateRequest request);

    UserQuery request2Query(UserQueryRequest request);

    UserResponse vo2CustomResponse(UserVO vo);

    UserResponse vo2Response(UserVO vo);
}
