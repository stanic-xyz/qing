package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.model.JsonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户令牌管理")
@RestController
@Slf4j
@RequestMapping("api/auth/third-party")
@RequiredArgsConstructor
public class ThirdPartyController {


    @GetMapping
    public JsonResult<ThirdPartyResponse> createUserToken(@RequestParam("platform") String platform) {
        log.info("platform:{}", platform);
        ThirdPartyResponse partyResponse = new ThirdPartyResponse();
        partyResponse.setAuthorizeUrl("http://localhost:8080/api/authorize/authing/login");
        return JsonResult.success(partyResponse);
    }
}
