package chenyunlong.zhangli.model.dto;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.AnimeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnimeTypeDTO implements OutputConverter<AnimeTypeDTO, AnimeType> {

    private Long id;
    @ApiModelProperty("类型名称")
    private String name;
    @ApiModelProperty("类型描述")
    private String description;
    @ApiModelProperty("排序号")
    private Integer orderNo;

}
