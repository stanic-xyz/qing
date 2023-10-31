package cn.chenyunlong.qing.domain.district.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class DistrictResponse extends AbstractJpaResponse {
    static {
    }

    @Schema(
        title = "code",
        description = "地区"
    )
    private String code;

    @Schema(
        title = "name",
        description = "名称"
    )
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
