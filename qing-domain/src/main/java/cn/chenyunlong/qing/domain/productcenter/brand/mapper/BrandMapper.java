package cn.chenyunlong.qing.domain.productcenter.brand.mapper;

import cn.chenyunlong.qing.domain.productcenter.brand.Brand;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.creator.BrandCreator;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.query.BrandQuery;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.request.BrandCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.request.BrandQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.request.BrandUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.response.BrandResponse;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.updater.BrandUpdater;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.vo.BrandVO;
import cn.hutool.core.bean.BeanUtil;

public interface BrandMapper {

    BrandMapper INSTANCE = new BrandMapper() {

    };

    default Brand dtoToEntity(BrandCreator dto) {
        return BeanUtil.copyProperties(dto, Brand.class);
    }

    default BrandUpdater request2Updater(BrandUpdateRequest request) {
        return BeanUtil.copyProperties(request, BrandUpdater.class);
    }

    default BrandCreator request2Dto(BrandCreateRequest request) {
        return BeanUtil.copyProperties(request, BrandCreator.class);
    }

    default BrandQuery request2Query(BrandQueryRequest request) {
        return BeanUtil.copyProperties(request, BrandQuery.class);
    }

    default BrandResponse vo2Response(BrandVO vo) {
        return BeanUtil.copyProperties(vo, BrandResponse.class);
    }

    default BrandResponse vo2CustomResponse(BrandVO vo) {
        return vo2Response(vo);
    }
}
