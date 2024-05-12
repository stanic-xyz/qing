package cn.chenyunlong.qing.domain.example.dto.updater;

import cn.chenyunlong.qing.domain.example.TestDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class TestDomainUpdater {

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

    public void updateTestDomain(TestDomain param) {
        Optional.ofNullable(getUsername()).ifPresent(param::setUsername);
        Optional.ofNullable(getPassword()).ifPresent(param::setPassword);
    }

}
