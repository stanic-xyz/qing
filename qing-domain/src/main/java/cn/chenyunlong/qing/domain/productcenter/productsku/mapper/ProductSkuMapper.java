package cn.chenyunlong.qing.domain.productcenter.productsku.mapper;

import cn.chenyunlong.qing.domain.productcenter.productsku.ProductSku;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.creator.ProductSkuCreator;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.query.ProductSkuQuery;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.request.ProductSkuCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.request.ProductSkuQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.request.ProductSkuUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.response.ProductSkuResponse;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.updater.ProductSkuUpdater;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.vo.ProductSkuVO;
import cn.hutool.core.bean.BeanUtil;

public interface ProductSkuMapper {

    ProductSkuMapper INSTANCE = new ProductSkuMapper() {

    };

    default ProductSku dtoToEntity(ProductSkuCreator dto) {
        return BeanUtil.copyProperties(dto, ProductSku.class);
    }

    default ProductSkuUpdater request2Updater(ProductSkuUpdateRequest request) {
        return BeanUtil.copyProperties(request, ProductSkuUpdater.class);
    }

    default ProductSkuCreator request2Dto(ProductSkuCreateRequest request) {
        return BeanUtil.copyProperties(request, ProductSkuCreator.class);
    }

    default ProductSkuQuery request2Query(ProductSkuQueryRequest request) {
        return BeanUtil.copyProperties(request, ProductSkuQuery.class);
    }

    default ProductSkuResponse vo2Response(ProductSkuVO vo) {
        return BeanUtil.copyProperties(vo, ProductSkuResponse.class);
    }

    default ProductSkuResponse vo2CustomResponse(ProductSkuVO vo) {
        return vo2Response(vo);
    }
}
