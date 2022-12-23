// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.sign.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.domain.sign.Sign;
import cn.chenyunlong.qing.domain.sign.creator.SignCreator;
import cn.chenyunlong.qing.domain.sign.query.SignQuery;
import cn.chenyunlong.qing.domain.sign.request.SignCreateRequest;
import cn.chenyunlong.qing.domain.sign.request.SignQueryRequest;
import cn.chenyunlong.qing.domain.sign.request.SignUpdateRequest;
import cn.chenyunlong.qing.domain.sign.response.SignResponse;
import cn.chenyunlong.qing.domain.sign.updater.SignUpdater;
import cn.chenyunlong.qing.domain.sign.vo.SignVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(
        uses = {
                GenericEnumMapper.class,
                DateMapper.class
        }
)
public interface SignMapper {
  SignMapper INSTANCE = Mappers.getMapper(SignMapper.class);

  Sign dtoToEntity(SignCreator dto);

  SignUpdater request2Updater(SignUpdateRequest request);

  SignCreator request2Dto(SignCreateRequest request);

  SignQuery request2Query(SignQueryRequest request);

  SignResponse vo2Response(SignVO vo);

  default SignResponse vo2CustomResponse(SignVO vo) {
    SignResponse response = vo2Response(vo);
    return response;
  }
}