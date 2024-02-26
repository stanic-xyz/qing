package cn.chenyunlong.qing.domain.productcenter.inoutrecord.mapper;

import cn.chenyunlong.qing.domain.productcenter.inoutrecord.InOutRecordDetail;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.creator.InOutRecordDetailCreator;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.query.InOutRecordDetailQuery;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordDetailCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordDetailQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordDetailUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.response.InOutRecordDetailResponse;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater.InOutRecordDetailUpdater;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo.InOutRecordDetailVO;
import cn.hutool.core.bean.BeanUtil;

public interface InOutRecordDetailMapper {

    InOutRecordDetailMapper INSTANCE = new InOutRecordDetailMapper() {

    };

    default InOutRecordDetail dtoToEntity(InOutRecordDetailCreator dto) {
        return BeanUtil.copyProperties(dto, InOutRecordDetail.class);
    }

    default InOutRecordDetailUpdater request2Updater(InOutRecordDetailUpdateRequest request) {
        return BeanUtil.copyProperties(request, InOutRecordDetailUpdater.class);
    }

    default InOutRecordDetailCreator request2Dto(InOutRecordDetailCreateRequest request) {
        return BeanUtil.copyProperties(request, InOutRecordDetailCreator.class);
    }

    default InOutRecordDetailQuery request2Query(InOutRecordDetailQueryRequest request) {
        return BeanUtil.copyProperties(request, InOutRecordDetailQuery.class);
    }

    default InOutRecordDetailResponse vo2Response(InOutRecordDetailVO vo) {
        return BeanUtil.copyProperties(vo, InOutRecordDetailResponse.class);
    }

    default InOutRecordDetailResponse vo2CustomResponse(InOutRecordDetailVO vo) {
        return vo2Response(vo);
    }
}
