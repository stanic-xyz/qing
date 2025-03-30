package cn.chenyunlong.qing.anime.domain.district.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema
@Data
public class DistrictUpdateRequest implements Request {

    @NotNull(message = "id不能为空")
    private Long id;

    @Schema(title = "code", description = "地区编码")
    private String code;

    @Schema(title = "name", description = "名称")
    private String name;


}
