package cn.chenyunlong.qing.domain.district.dto.updater;

import cn.chenyunlong.qing.domain.district.District;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Long;
import java.lang.String;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class DistrictUpdater {
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

    private Long id;

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

    public void updateDistrict(District param) {
        Optional.ofNullable(getCode()).ifPresent(param::setCode);
        Optional.ofNullable(getName()).ifPresent(param::setName);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
