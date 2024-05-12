package cn.chenyunlong.qing.domain.example.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
