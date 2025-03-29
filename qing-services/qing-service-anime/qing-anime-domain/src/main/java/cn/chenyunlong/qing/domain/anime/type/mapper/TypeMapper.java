package cn.chenyunlong.qing.domain.anime.type.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.anime.type.Type;
import cn.chenyunlong.qing.domain.anime.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.domain.anime.type.dto.query.TypeQuery;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeCreateRequest;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeQueryRequest;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.type.dto.response.TypeResponse;
import cn.chenyunlong.qing.domain.anime.type.dto.updater.TypeUpdater;
import cn.chenyunlong.qing.domain.anime.type.dto.vo.TypeVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TypeMapper {

    TypeMapper INSTANCE = Mappers.getMapper(TypeMapper.class);

    Type dtoToEntity(TypeCreator dto);

    TypeUpdater request2Updater(TypeUpdateRequest request);

    TypeCreator request2Dto(TypeCreateRequest request);

    TypeQuery request2Query(TypeQueryRequest request);

    TypeResponse vo2CustomResponse(TypeVO vo);

    TypeResponse vo2Response(TypeVO vo);

    TypeVO entityToVo(Type type);
}
