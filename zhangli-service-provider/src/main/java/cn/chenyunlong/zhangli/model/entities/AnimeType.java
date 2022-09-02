package cn.chenyunlong.zhangli.model.entities;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 动漫类型信息
 */
@Data
@TableName("anime_type")
@EqualsAndHashCode(callSuper = false)
public class AnimeType extends BaseEntity<AnimeType> {
    private Long id;
    @ApiModelProperty("类型名称")
    private String name;
    @ApiModelProperty("类型描述")
    private String description;
    @ApiModelProperty("排序号")
    private Integer orderNo;
}