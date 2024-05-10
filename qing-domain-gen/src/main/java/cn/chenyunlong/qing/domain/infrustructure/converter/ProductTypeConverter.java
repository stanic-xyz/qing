package cn.chenyunlong.qing.domain.infrustructure.converter;

import cn.chenyunlong.qing.domain.productcenter.product.ProductType;
import jakarta.persistence.AttributeConverter;

public class ProductTypeConverter implements AttributeConverter<ProductType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProductType productType) {
        return productType.getValue();
    }

    @Override
    public ProductType convertToEntityAttribute(Integer code) {
        return ProductType.of(code).orElse(null);
    }
}
