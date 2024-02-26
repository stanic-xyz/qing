package cn.chenyunlong.qing.domain.productcenter.product.mapper;

import cn.chenyunlong.qing.domain.productcenter.product.Product;
import cn.chenyunlong.qing.domain.productcenter.product.dto.creator.ProductCreator;
import cn.chenyunlong.qing.domain.productcenter.product.dto.query.ProductQuery;
import cn.chenyunlong.qing.domain.productcenter.product.dto.request.ProductCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.product.dto.request.ProductQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.product.dto.request.ProductUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.product.dto.response.ProductResponse;
import cn.chenyunlong.qing.domain.productcenter.product.dto.updater.ProductUpdater;
import cn.chenyunlong.qing.domain.productcenter.product.dto.vo.ProductVO;
import cn.hutool.core.bean.BeanUtil;

public interface ProductMapper {

    ProductMapper INSTANCE = new ProductMapper() {

    };

    default Product dtoToEntity(ProductCreator dto) {
        return BeanUtil.copyProperties(dto, Product.class);
    }

    default ProductUpdater request2Updater(ProductUpdateRequest request) {
        return BeanUtil.copyProperties(request, ProductUpdater.class);
    }

    default ProductCreator request2Dto(ProductCreateRequest request) {
        return BeanUtil.copyProperties(request, ProductCreator.class);
    }

    default ProductQuery request2Query(ProductQueryRequest request) {
        return BeanUtil.copyProperties(request, ProductQuery.class);
    }

    default ProductResponse vo2Response(ProductVO vo) {
        return BeanUtil.copyProperties(vo, ProductResponse.class);
    }

    default ProductResponse vo2CustomResponse(ProductVO vo) {
        return vo2Response(vo);
    }
}
