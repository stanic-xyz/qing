package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenId;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.auth.domain.user.dto.request.UserTokenCreateRequest;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.GenericEnumMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.TokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = {
        GenericEnumMapper.class,
        DateMapper.class,
        CustomMapper.class, AggregateMapper.class
}, unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserTokenMapper {

    UserTokenCreator request2Dto(UserTokenCreateRequest request);

    TokenEntity domainToEntity(AuthenticationToken entity);

    AuthenticationToken entityToDomain(TokenEntity byTokenValue);

    default TokenId map(Long value) {
        return TokenId.of(value);
    }

    default UserId mapUserId(Long value) {
        return UserId.of(value);
    }
}
