package cn.chenyunlong.zhangli.model.params;

import cn.chenyunlong.zhangli.model.dto.base.InputConverter;
import cn.chenyunlong.zhangli.model.entities.AnimeRecommendEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AnimeRecommendParam implements InputConverter<AnimeRecommendEntity> {

    @ApiModelProperty("动漫ID")
    @NotNull(message = "动漫ID不能为空")
    private Long aid;
    @NotBlank(message = "推荐理由必填")
    @Size(max = 255, message = "推荐理由不能超过{mx}个字符")
    @ApiModelProperty("推荐理由")
    private String reason;
    @ApiModelProperty("排序号")
    private Integer orderNo;
}
