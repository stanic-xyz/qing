package chenyunlong.zhangli.model.params;

import chenyunlong.zhangli.model.dto.base.InputConverter;
import chenyunlong.zhangli.model.entities.AnimeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AnimeTypeParam implements InputConverter<AnimeType> {

    @ApiModelProperty("类型名称")
    @Size(max = 10, message = "长度不能超过{max}个字符")
    @NotBlank(message = "类型名称不能为空")
    private String name;
    @ApiModelProperty("类型描述")
    private String description;
    @ApiModelProperty("排序号")
    private Integer orderNo = 0;
}
