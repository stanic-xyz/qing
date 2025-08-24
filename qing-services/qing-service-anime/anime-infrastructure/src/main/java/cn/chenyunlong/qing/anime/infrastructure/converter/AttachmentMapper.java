package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.anime.domain.attachement.Attachment;
import cn.chenyunlong.qing.anime.domain.attachement.AttachmentId;
import cn.chenyunlong.qing.anime.domain.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.anime.domain.attachement.dto.query.AttachmentQuery;
import cn.chenyunlong.qing.anime.domain.attachement.dto.request.AttachmentCreateRequest;
import cn.chenyunlong.qing.anime.domain.attachement.dto.request.AttachmentQueryRequest;
import cn.chenyunlong.qing.anime.domain.attachement.dto.request.AttachmentUpdateRequest;
import cn.chenyunlong.qing.anime.domain.attachement.dto.response.AttachmentResponse;
import cn.chenyunlong.qing.anime.domain.attachement.dto.updater.AttachmentUpdater;
import cn.chenyunlong.qing.anime.domain.attachement.dto.vo.AttachmentVO;
import cn.chenyunlong.qing.domain.common.converter.AggregateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CustomMapper.class, DateMapper.class, AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttachmentMapper {

    Attachment dtoToEntity(AttachmentCreator dto);

    AttachmentUpdater request2Updater(AttachmentUpdateRequest request);

    AttachmentCreator request2Dto(AttachmentCreateRequest request);

    AttachmentQuery request2Query(AttachmentQueryRequest request);

    AttachmentResponse vo2CustomResponse(AttachmentVO vo);

    AttachmentResponse vo2Response(AttachmentVO vo);

    AttachmentVO entityToVo(Attachment attachment1);

    default Long map(AttachmentId tagId) {
        return tagId != null ? tagId.getValue() : null;
    }

    default AttachmentId longToTypeId(Long id) {
        return id != null ? AttachmentId.of(id) : null;
    }
}
