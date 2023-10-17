package cn.chenyunlong.dingtalk.controller;

import cn.chenyunlong.dingtalk.controller.interceptor.TokenRequired;
import cn.chenyunlong.dingtalk.controller.model.MessageCardModel;
import cn.chenyunlong.dingtalk.controller.model.ServiceResponse;
import cn.chenyunlong.dingtalk.controller.model.TextModel;
import cn.chenyunlong.dingtalk.controller.model.TopCardModel;
import cn.chenyunlong.dingtalk.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 钉钉机器人控制器。
 *
 * @author Stan
 * @since 2022/10/2022/10/18
 */
@Slf4j
@TokenRequired
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private final RobotGroupMessagesService robotGroupMessagesService;
    private final RobotInteractiveCardsService robotInteractiveCardsService;
    private final InteractiveCardsInstanceService interactiveCardsInstanceService;
    private final UserService userService;

    /**
     * 发送文本消息。
     *
     * @return Response 200 : OK
     */
    @PostMapping(value = "/sendText")
    public String sendText(@RequestBody TextModel textModel) throws Exception {
        String messageId =
            robotGroupMessagesService.send(textModel.getOpenConversationId(), textModel.getTxt());
        log.info("sendText success {}, messageId={}", textModel, messageId);
        return "OK";
    }

    /**
     * 发送消息卡片。
     *
     * @param messageCardModel 消息卡片结构体
     * @return OK
     * @throws Exception Tea异常 & 内部异常
     */
    @PostMapping(value = "/sendMessageCard")
    public String sendMessageCard(@RequestBody MessageCardModel messageCardModel) throws Exception {
        String message =
            robotInteractiveCardsService.send(messageCardModel.getOpenConversationId());
        log.info("sendMessageCard success {},发送消息：{}", messageCardModel, message);
        return "OK";
    }

    /**
     * 发送置顶卡片。
     *
     * @param topCardModel 吊顶卡片model
     * @return OK
     * @throws Exception 内部的一些异常
     */
    @PostMapping(value = "/sendTopCard")
    public String sendTopCard(@RequestBody TopCardModel topCardModel) throws Exception {
        log.info("sendTopCard {}", topCardModel);
        Boolean success = interactiveCardsInstanceService.createAndDeliverBox(
            topCardModel.getOpenConversationId());
        log.info("sendTopCard success {}, model={}", success, topCardModel);
        return "OK";
    }

    /**
     * 获取用户信息。
     *
     * @param authCode 免等授权码
     * @return ServiceResponse isSuccess说明是正常，否则异常，异常可以通过True Or False来体现
     * @throws Exception 内部异常
     */
    @GetMapping(value = "/getUserInfo")
    public ServiceResponse<UserInfo> getUserInfo(@RequestParam("requestAuthCode") String authCode)
        throws Exception {
        UserInfo userInfo = userService.getUserInfo(authCode);
        log.info("getUserInfo, md5AuthCode={}, userInfo={}",
            DigestUtils.md5DigestAsHex(authCode.getBytes()), userInfo);
        return ServiceResponse.buildSuccessServiceResponse(userInfo);
    }
}
