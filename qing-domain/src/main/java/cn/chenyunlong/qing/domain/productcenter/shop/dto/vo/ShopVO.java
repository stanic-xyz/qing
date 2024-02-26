package cn.chenyunlong.qing.domain.productcenter.shop.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.shop.Shop;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
@NoArgsConstructor(
    access = AccessLevel.PROTECTED
)
public class ShopVO extends AbstractBaseJpaVo {

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

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;

    public ShopVO(Shop source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setStarLevel(source.getStarLevel());
        this.setOnlineTime(source.getOnlineTime());
        this.setContactUserPhone(source.getContactUserPhone());
        this.setContactUser(source.getContactUser());
        this.setValidStatus(source.getValidStatus());
    }
}
