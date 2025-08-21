package cn.chenyunlong.codegen.example.domain.mapper;

import cn.chenyunlong.codegen.example.domain.Product;
import cn.chenyunlong.codegen.example.domain.creator.ProductCreator;
import cn.chenyunlong.codegen.example.domain.query.ProductQuery;
import cn.chenyunlong.codegen.example.domain.request.ProductCreateRequest;
import cn.chenyunlong.codegen.example.domain.request.ProductQueryRequest;
import cn.chenyunlong.codegen.example.domain.request.ProductUpdateRequest;
import cn.chenyunlong.codegen.example.domain.response.ProductResponse;
import cn.chenyunlong.codegen.example.domain.updater.ProductUpdater;
import cn.chenyunlong.codegen.example.domain.vo.ProductVO;
import cn.hutool.core.bean.BeanUtil;

public interface ProductMapper {
    ProductMapper INSTANCE = new ProductMapper() {};

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
