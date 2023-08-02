/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.app.web.system;

import cn.chenyunlong.qing.domain.third.wechat.service.WeChatMessageService;
import com.riversoft.weixin.common.decrypt.AesException;
import com.riversoft.weixin.common.decrypt.MessageDecryption;
import com.riversoft.weixin.common.decrypt.SHA1;
import com.riversoft.weixin.common.event.EventRequest;
import com.riversoft.weixin.common.exception.WxRuntimeException;
import com.riversoft.weixin.common.message.MsgType;
import com.riversoft.weixin.common.message.XmlMessageHeader;
import com.riversoft.weixin.common.message.xml.TextXmlMessage;
import com.riversoft.weixin.common.oauth2.AccessToken;
import com.riversoft.weixin.common.oauth2.OpenUser;
import com.riversoft.weixin.mp.base.AppSetting;
import com.riversoft.weixin.mp.care.CareMessages;
import com.riversoft.weixin.mp.message.MpXmlMessages;
import com.riversoft.weixin.mp.oauth2.MpOAuth2s;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 对接微信公众平台
 *
 * @author Stan
 * @date 2022/11/05
 */
@Tag(name = "微信公众平台对接")
@RestController
@RequestMapping("api/wx/mp")
public class WxMpController {

    private final Logger log = LoggerFactory.getLogger(WxMpController.class);

    private final WeChatMessageService weChatMessageService;

    public WxMpController(WeChatMessageService weChatMessageService) {
        this.weChatMessageService = weChatMessageService;
    }

    @GetMapping("oauth/mp")
    public void mp(@RequestParam(value = "code") String code,
                   @RequestParam(value = "state", required = false) String state) {

        log.info("code:{}, state:{}", code, state);
        AccessToken accessToken = MpOAuth2s.defaultOAuth2s().getAccessToken(code);
        OpenUser openUser = MpOAuth2s.defaultOAuth2s()
            .userInfo(accessToken.getAccessToken(), accessToken.getOpenId());
        //do your logic
        log.debug("这里就是相应的消息了！");
        log.debug("登录{}", openUser.getNickName());
    }


    /**
     * 公众号回调接口
     * 这里为了演示方便使用单个固定URL，实际使用的时候一个一个系统可能有多个公众号，这样的话需要有个分发逻辑：
     * 比如callback url可以定义为  /wx/mp/[公众号的appId]，通过appId构造不同的AppSetting
     *
     * @param signature    签名
     * @param msgSignature 消息签名
     * @param timestamp    时间
     * @param nonce        随机字符串
     * @param echoStr      回应字符串
     * @param encryptType  加密类自行
     * @param content      内容
     * @return 回复给微信后台的内容
     */
    @RequestMapping("/wx/mp")
    @ResponseBody
    public String mp(@RequestParam(value = "signature") String signature,
                     @RequestParam(value = "msg_signature", required = false) String msgSignature,
                     @RequestParam(value = "timestamp") String timestamp,
                     @RequestParam(value = "nonce") String nonce,
                     @RequestParam(value = "echostr", required = false) String echoStr,
                     @RequestParam(value = "encrypt_type", required = false) String encryptType,
                     @RequestBody(required = false) String content) {

        log.info(
            "signature={}, msg_signature={}, timestamp={}, nonce={}, echostr={}, encrypt_type={}",
            signature, msgSignature, timestamp, nonce, echoStr, encryptType);

        AppSetting appSetting = AppSetting.defaultSettings();
        try {
            if (!SHA1.getSHA1(appSetting.getToken(), timestamp, nonce).equals(signature)) {
                log.warn("非法请求.");
                return "非法请求.";
            }
        } catch (AesException e) {
            log.error("check signature failed:", e);
            return "非法请求.";
        }

        if (!StringUtils.hasText(echoStr)) {
            return echoStr;
        }

        XmlMessageHeader xmlRequest;
        if ("aes".equals(encryptType)) {
            try {
                MessageDecryption messageDecryption =
                    new MessageDecryption(appSetting.getToken(), appSetting.getAesKey(),
                        appSetting.getAppId());
                xmlRequest = MpXmlMessages.fromXml(
                    messageDecryption.decrypt(msgSignature, timestamp, nonce, content));
                XmlMessageHeader xmlResponse = mpDispatch(xmlRequest);

                if (xmlResponse != null) {
                    try {
                        return messageDecryption.encrypt(MpXmlMessages.toXml(xmlResponse),
                            timestamp, nonce);
                    } catch (WxRuntimeException e) {
                        log.error("发生了议程", e);
                    }
                }
            } catch (AesException e) {
                log.error("发生了议程", e);
            }
        } else {
            xmlRequest = MpXmlMessages.fromXml(content);
            XmlMessageHeader xmlResponse = mpDispatch(xmlRequest);
            if (xmlResponse != null) {
                try {
                    return MpXmlMessages.toXml(xmlResponse);
                } catch (WxRuntimeException e) {
                    log.error("发生了异常", e);
                }
            }
        }
        return "success";
    }

    private XmlMessageHeader mpDispatch(XmlMessageHeader xmlRequest) {
        try {
            if (!weChatMessageService.isDuplicated(
                xmlRequest.getFromUser() + xmlRequest.getCreateTime().getTime())) {
                String welcome = "您好:" + xmlRequest.getFromUser();
                log.info("后台收到了消息：" + welcome);
                CareMessages.defaultCareMessages().text(xmlRequest.getFromUser(), welcome);

                if (xmlRequest instanceof EventRequest eventRequest) {
                    log.debug("事件请求[{}]", eventRequest.getEventType().name());
                    CareMessages
                        .defaultCareMessages()
                        .text(xmlRequest.getFromUser(),
                            "事件请求:" + eventRequest.getEventType().name());
                } else {
                    log.debug("消息请求[{}]", xmlRequest.getMsgType().name());
                    CareMessages
                        .defaultCareMessages()
                        .text(xmlRequest.getFromUser(),
                            "消息请求:" + xmlRequest.getMsgType().name());
                }
            } else {
                log.warn("Duplicated message: {} @ {}", xmlRequest.getMsgType(),
                    xmlRequest.getFromUser());
            }
        } catch (Exception exp) {
            return null;
        }

        TextXmlMessage messageHeader = new TextXmlMessage();
        messageHeader.setContent("你好" + xmlRequest.getFromUser());
        messageHeader.setCreateTime(new Date());
        messageHeader.setFromUser(xmlRequest.getToUser());
        messageHeader.setToUser(xmlRequest.getFromUser());
        messageHeader.setMsgType(MsgType.text);

        //需要同步返回消息（被动回复）给用户则构造一个XmlMessageHeader类型，比较鸡肋，因为处理逻辑如果比较复杂响应太慢会影响用户感知，建议直接返回null；
        //要发送消息给用户可以参考上面的例子使用客服消息接口进行异步发送
        return messageHeader;
    }
}
