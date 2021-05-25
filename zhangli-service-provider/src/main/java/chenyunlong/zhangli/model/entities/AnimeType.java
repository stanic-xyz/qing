package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 动漫类型信息
 */
@Data
public class AnimeType extends BaseEntity {
    private Long id;
    @ApiModelProperty("类型名称")
    private String name;
    @ApiModelProperty("类型描述")
    private String description;
}
