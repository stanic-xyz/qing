package chenyunlong.zhangli.controller.system;


import chenyunlong.zhangli.common.DeferredResultResponse;
import chenyunlong.zhangli.mail.MailService;
import chenyunlong.zhangli.service.DeferredResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stan
 */
@Api(tags = "异步任务请求")
@Slf4j
@RestController
@RequestMapping("async")
public class MailController {

    private final DeferredResultService deferredResultService;
    private final MailService mailService;
    /**
     * 为了方便测试，简单模拟一个
     * 多个请求用同一个requestId会出问题
     */
    private final String requestId = "haha";

    @Autowired
    public MailController(DeferredResultService deferredResultService, MailService mailService) {
        this.deferredResultService = deferredResultService;
        this.mailService = mailService;
    }

    @ApiOperation(value = "deferedResult请求", notes = "timeout参数用于指定延迟时间")
    @GetMapping(value = "/get")
    public DeferredResult<DeferredResultResponse> get(@RequestParam(value = "timeout", required = false, defaultValue = "3000") Long timeout) {
        DeferredResult<DeferredResultResponse> deferredResult = new DeferredResult<>(timeout);

        log.debug("接收到请求");
        deferredResultService.process(requestId, deferredResult);

        return deferredResult;
    }

    /**
     * 设置DeferredResult对象的result属性，模拟异步操作
     *
     * @param desired 意图
     * @return 返回x响应结果
     */
    @ApiOperation(value = "异步消费者", notes = "这一步是用来结束前面的一部操作额")
    @GetMapping(value = "/result")
    public String settingResult(@RequestParam(value = "desired", required = false, defaultValue = "成功") String desired) {
        DeferredResultResponse deferredResultResponse = new DeferredResultResponse();
        if (DeferredResultResponse.Msg.SUCCESS.getDesc().equals(desired)) {
            deferredResultResponse.setCode(HttpStatus.OK.value());
            deferredResultResponse.setMsg(desired);
        } else {
            deferredResultResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            deferredResultResponse.setMsg(DeferredResultResponse.Msg.FAILED.getDesc());
        }
        deferredResultService.settingResult(requestId, deferredResultResponse);

        return "Done";
    }

    @GetMapping("mailto")
    public String sendMail(String to, String subject, String message) {
        mailService.sendTextMail(to, subject, message);
        return "发送成功";
    }

    @GetMapping("templateMailto")
    public String sendTemplateMail(String to, String subject, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("from", "1576302867@qq.com");
        mailService.sendTemplateMail(to, subject, data, "register.ftl");
        return "发送成功";
    }
}
