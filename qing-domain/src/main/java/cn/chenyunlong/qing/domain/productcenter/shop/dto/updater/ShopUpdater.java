package cn.chenyunlong.qing.domain.productcenter.shop.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.shop.Shop;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class ShopUpdater {

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

    private Long id;

    public void updateShop(Shop param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getStarLevel()).ifPresent(param::setStarLevel);
        Optional.ofNullable(getOnlineTime()).ifPresent(param::setOnlineTime);
        Optional.ofNullable(getContactUserPhone()).ifPresent(param::setContactUserPhone);
        Optional.ofNullable(getContactUser()).ifPresent(param::setContactUser);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
