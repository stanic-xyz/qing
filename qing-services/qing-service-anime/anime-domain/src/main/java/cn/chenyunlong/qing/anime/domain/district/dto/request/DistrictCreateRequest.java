package cn.chenyunlong.qing.anime.domain.district.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class DistrictCreateRequest implements Request {

    @Schema(title = "code", description = "地区")
    private String code;

    @Schema(title = "name", description = "名称")
    private String name;

}
