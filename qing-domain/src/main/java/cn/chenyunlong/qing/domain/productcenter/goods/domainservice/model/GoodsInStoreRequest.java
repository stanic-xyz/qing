package cn.chenyunlong.qing.domain.productcenter.goods.domainservice.model;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class GoodsInStoreRequest implements Request {

    @NotEmpty(message = "商品编码不能为空！")
    private List<String> goodsCodes;

    @Schema(title = "仓库id")
    private String storeId;

}
