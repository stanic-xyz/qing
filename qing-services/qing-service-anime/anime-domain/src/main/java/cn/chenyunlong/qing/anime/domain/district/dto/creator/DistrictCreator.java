package cn.chenyunlong.qing.anime.domain.district.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class DistrictCreator {

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
