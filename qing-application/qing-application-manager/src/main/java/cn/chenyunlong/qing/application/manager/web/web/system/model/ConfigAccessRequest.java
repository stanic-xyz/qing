package cn.chenyunlong.qing.application.manager.web.web.system.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ConfigAccessRequest {

    @Schema(title = "微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。")
    private String signature;

    @Schema(title = "时间戳")
    private String timestamp;

    @Schema(title = "nonce 随机数")
    private String nonce;

    @Schema(title = "随机字符串")
    private String echostr;
}
