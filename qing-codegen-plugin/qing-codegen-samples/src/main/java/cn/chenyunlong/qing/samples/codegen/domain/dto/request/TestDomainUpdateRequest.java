package cn.chenyunlong.qing.samples.codegen.domain.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class TestDomainUpdateRequest implements Request {
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

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
