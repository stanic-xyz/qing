package cn.chenyunlong.qing.domain.attachement.mapper;

import cn.chenyunlong.qing.domain.attachement.Attachment;
import cn.chenyunlong.qing.domain.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.attachement.dto.query.AttachmentQuery;
import cn.chenyunlong.qing.domain.attachement.dto.request.AttachmentCreateRequest;
import cn.chenyunlong.qing.domain.attachement.dto.request.AttachmentQueryRequest;
import cn.chenyunlong.qing.domain.attachement.dto.request.AttachmentUpdateRequest;
import cn.chenyunlong.qing.domain.attachement.dto.response.AttachmentResponse;
import cn.chenyunlong.qing.domain.attachement.dto.updater.AttachmentUpdater;
import cn.chenyunlong.qing.domain.attachement.dto.vo.AttachmentVO;
import cn.hutool.core.bean.BeanUtil;

public interface AttachmentMapper {
    AttachmentMapper INSTANCE = new AttachmentMapper() {
    };

    default Attachment dtoToEntity(AttachmentCreator dto) {
        return BeanUtil.copyProperties(dto, Attachment.class);
    }

    default AttachmentUpdater request2Updater(AttachmentUpdateRequest request) {
        return BeanUtil.copyProperties(request, AttachmentUpdater.class);
    }

    default AttachmentCreator request2Dto(AttachmentCreateRequest request) {
        return BeanUtil.copyProperties(request, AttachmentCreator.class);
    }

    default AttachmentQuery request2Query(AttachmentQueryRequest request) {
        return BeanUtil.copyProperties(request, AttachmentQuery.class);
    }

    default AttachmentResponse vo2CustomResponse(AttachmentVO vo) {
        return vo2Response(vo);
    }

    default AttachmentResponse vo2Response(AttachmentVO vo) {
        return BeanUtil.copyProperties(vo, AttachmentResponse.class);
    }
}
