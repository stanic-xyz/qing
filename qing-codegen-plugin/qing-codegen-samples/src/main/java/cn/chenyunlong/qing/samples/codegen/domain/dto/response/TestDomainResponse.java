package cn.chenyunlong.qing.samples.codegen.domain.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class TestDomainResponse extends AbstractJpaResponse {
    static {
    }

    @Schema(
        title = "username",
        description = "username"
    )
    private String username;

    @Schema(
        title = "password",
        description = "password"
    )
    private String password;
}
