package cn.chenyunlong.qing.domain.productcenter.inoutrecord.mapper;

import cn.chenyunlong.qing.domain.productcenter.inoutrecord.InOutRecord;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.creator.InOutRecordCreator;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.query.InOutRecordQuery;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.response.InOutRecordResponse;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater.InOutRecordUpdater;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo.InOutRecordVO;
import cn.hutool.core.bean.BeanUtil;

public interface InOutRecordMapper {

    InOutRecordMapper INSTANCE = new InOutRecordMapper() {

    };

    default InOutRecord dtoToEntity(InOutRecordCreator dto) {
        return BeanUtil.copyProperties(dto, InOutRecord.class);
    }

    default InOutRecordUpdater request2Updater(InOutRecordUpdateRequest request) {
        return BeanUtil.copyProperties(request, InOutRecordUpdater.class);
    }

    default InOutRecordCreator request2Dto(InOutRecordCreateRequest request) {
        return BeanUtil.copyProperties(request, InOutRecordCreator.class);
    }

    default InOutRecordQuery request2Query(InOutRecordQueryRequest request) {
        return BeanUtil.copyProperties(request, InOutRecordQuery.class);
    }

    default InOutRecordResponse vo2Response(InOutRecordVO vo) {
        return BeanUtil.copyProperties(vo, InOutRecordResponse.class);
    }

    default InOutRecordResponse vo2CustomResponse(InOutRecordVO vo) {
        return vo2Response(vo);
    }
}
