package cn.chenyunlong.qing.domain.anime.district.dto.updater;

import cn.chenyunlong.qing.domain.anime.district.District;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class DistrictUpdater {

    @Schema(title = "code", description = "地区")
    private String code;

    @Schema(title = "name", description = "名称")
    private String name;

    private Long id;

    public void updateDistrict(District param) {
        Optional.ofNullable(getCode()).ifPresent(param::setCode);
        Optional.ofNullable(getName()).ifPresent(param::setName);
    }

}
