package chenyunlong.zhangli.model.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import chenyunlong.zhangli.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 动漫类型信息
 */
@Data
@TableName("anime_type")
@EqualsAndHashCode(callSuper = false)
public class AnimeType extends BaseEntity {
    private Long id;
    @ApiModelProperty("类型名称")
    private String name;
    @ApiModelProperty("类型描述")
    private String description;
    @ApiModelProperty("排序号")
    private Integer orderNo;
}
