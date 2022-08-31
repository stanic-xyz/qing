package chenyunlong.zhangli.model.params;

import chenyunlong.zhangli.model.dto.base.InputConverter;
import chenyunlong.zhangli.model.entities.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VersionParam implements InputConverter<Version> {

    @NotBlank
    @ApiModelProperty("版本代码")
    private String code;

    @NotBlank
    @ApiModelProperty("版本名称")
    private String name;

    @NotBlank
    @ApiModelProperty("版本描述信息")
    private String description;
}
