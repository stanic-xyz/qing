// ---Auto Generated by Qing-Generator ---
package cn.chenyunlong.qing.domain.system.version.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class VersionCreator {
    @Schema(
            title = "Code"
    )
    private String code;

    @Schema(
            title = "Name"
    )
    private String name;

    @Schema(
            title = "Description"
    )
    private String description;
}