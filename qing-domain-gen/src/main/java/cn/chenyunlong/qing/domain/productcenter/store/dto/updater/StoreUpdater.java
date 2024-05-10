package cn.chenyunlong.qing.domain.productcenter.store.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.store.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class StoreUpdater {

    @Schema(
        title = "name",
        description = "仓库名称"
    )
    private String name;

    @Schema(
        title = "description",
        description = "备注"
    )
    private String description;

    private Long id;

    public void updateStore(Store param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getDescription()).ifPresent(param::setDescription);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
