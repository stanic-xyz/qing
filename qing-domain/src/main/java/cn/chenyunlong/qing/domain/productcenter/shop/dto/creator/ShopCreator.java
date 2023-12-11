package cn.chenyunlong.qing.domain.productcenter.shop.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class ShopCreator {
    @Schema(
            title = "name",
            description = "店名"
    )
    private String name;

    @Schema(
            title = "starLevel",
            description = "星级"
    )
    private Integer starLevel;

    @Schema(
            title = "onlineTime",
            description = "上线时间"
    )
    private Long onlineTime;

    @Schema(
            title = "contactUserPhone",
            description = "联系人手机号"
    )
    private String contactUserPhone;

    @Schema(
            title = "contactUser",
            description = "联系人"
    )
    private String contactUser;
}
