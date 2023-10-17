package cn.chenyunlong.dingtalk.service;

import com.aliyun.dingtalkworkflow_1_0.Client;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceHeaders;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceRequest;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 钉钉机器人消息服务。
 *
 * @author Stan
 * @since 2022/10/2022/10/19
 */
@Slf4j
@Service
public class LeaveService {
    private final AccessTokenService accessTokenService;
    private Client client;

    @Value("${leave.code}")
    private String leaveCode;

    @Value("${coolApp.code}")
    private String coolAppCode;

    @Autowired
    public LeaveService(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }


    /**
     * 初始化配置信息。
     *
     * @throws Exception 坑的异常
     */
    @PostConstruct
    public void init() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        client = new Client(config);
    }

    /**
     * 内部群发消息。
     *
     * @return 加密消息id
     */
    public String startProcessInstance() {
        StartProcessInstanceHeaders startProcessInstanceHeaders = new StartProcessInstanceHeaders();
        startProcessInstanceHeaders.xAcsDingtalkAccessToken = accessTokenService.getAccessToken();
        StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues
            formComponentValues0 =
            new StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues()
                .setName("请假理由")
                .setValue("测试理由")
                .setBizAlias("123");
        StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners
            targetSelectActioners0 =
            new StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners().setActionerKey(
                "cc");
        StartProcessInstanceRequest.StartProcessInstanceRequestApprovers requestApprovers =
            new StartProcessInstanceRequest.StartProcessInstanceRequestApprovers().setUserIds(
                List.of("091669630637635952"));
        StartProcessInstanceRequest startProcessInstanceRequest = new StartProcessInstanceRequest()
            .setApprovers(Collections.singletonList(requestApprovers))
            .setOriginatorUserId("091669630637635952")
            .setProcessCode("PROC-C529B8E3-92D4-42F6-93A0-5CCDE0BABA0A")
            .setTargetSelectActioners(Collections.singletonList(targetSelectActioners0))
            .setFormComponentValues(Collections.singletonList(formComponentValues0));
        try {
            StartProcessInstanceResponse processInstanceResponse =
                client.startProcessInstanceWithOptions(startProcessInstanceRequest,
                    startProcessInstanceHeaders, new RuntimeOptions());
            return processInstanceResponse.getBody().getInstanceId();
        } catch (TeaException err) {
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        } catch (Exception exception) {
            TeaException err = new TeaException(exception.getMessage(), exception);
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.info("发生了异常", exception);
            }
        }
        return null;
    }
}
