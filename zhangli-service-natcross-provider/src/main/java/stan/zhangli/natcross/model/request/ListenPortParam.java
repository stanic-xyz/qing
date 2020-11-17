package stan.zhangli.natcross.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Stan
 */
@Data
@ApiModel(value = "监听端口信息", description = "端口监听信息")
public class ListenPortParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("远程端口，外网可访问的端口信息")
    private Integer listenPort;

    @ApiModelProperty("服务描述")
    private String portDescribe;

    @ApiModelProperty("目标ip（本地IP，客户端可访问到的）")
    private String destIp;

    @ApiModelProperty("目标端口（本地服务的端口）")
    private Integer destPort;

    @ApiModelProperty("是否开机自启")
    private Boolean onStart;

    @ApiModelProperty("端口类型")
    private Integer portType;

    @JsonIgnore
    @ApiModelProperty("证书路径")
    private String certPath;

    @JsonIgnore
    @ApiModelProperty("证书密码")
    private String certPassword;
}
