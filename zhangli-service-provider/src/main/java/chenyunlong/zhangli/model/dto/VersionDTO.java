package chenyunlong.zhangli.model.dto;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VersionDTO implements OutputConverter<VersionDTO, Version> {

    @ApiModelProperty("版本ID")
    private Long vid;

    @ApiModelProperty("版本代码")
    private String code;

    @ApiModelProperty("版本名称")
    private String name;

    @ApiModelProperty("版本描述信息")
    private String description;
}
