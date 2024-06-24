package cn.chenyunlong.qing.domain.anime.attachement.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import cn.chenyunlong.qing.domain.anime.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.anime.attachement.dto.query.AttachmentQuery;
import cn.chenyunlong.qing.domain.anime.attachement.dto.request.AttachmentCreateRequest;
import cn.chenyunlong.qing.domain.anime.attachement.dto.request.AttachmentQueryRequest;
import cn.chenyunlong.qing.domain.anime.attachement.dto.request.AttachmentUpdateRequest;
import cn.chenyunlong.qing.domain.anime.attachement.dto.response.AttachmentResponse;
import cn.chenyunlong.qing.domain.anime.attachement.dto.updater.AttachmentUpdater;
import cn.chenyunlong.qing.domain.anime.attachement.dto.vo.AttachmentVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttachmentMapper {

    AttachmentMapper INSTANCE = Mappers.getMapper(AttachmentMapper.class);

    Attachment dtoToEntity(AttachmentCreator dto);

    AttachmentUpdater request2Updater(AttachmentUpdateRequest request);

    AttachmentCreator request2Dto(AttachmentCreateRequest request);

    AttachmentQuery request2Query(AttachmentQueryRequest request);

    AttachmentResponse vo2CustomResponse(AttachmentVO vo);

    AttachmentResponse vo2Response(AttachmentVO vo);

    AttachmentVO entityToVo(Attachment attachment1);
}
