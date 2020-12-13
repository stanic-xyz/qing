package chenyunlong.zhangli.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author 动漫类型信息
 */
@Data
public class AnimeType implements Serializable {
    private Long id;
    @NotBlank
    @Length(max = 10)
    @ApiModelProperty("类型名称")
    private String name;
    @Nullable
    @ApiModelProperty("类型描述")
    private String description;
}
