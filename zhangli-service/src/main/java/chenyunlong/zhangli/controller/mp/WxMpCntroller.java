package chenyunlong.zhangli.controller.mp;

import chenyunlong.zhangli.controller.mp.commoms.DuplicatedMessageChecker;
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
import com.riversoft.weixin.mp.message.MpXmlMessages;
import com.riversoft.weixin.mp.oauth2.MpOAuth2s;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("wxmp")
public class WxMpCntroller {


    @Autowired
    private DuplicatedMessageChecker duplicatedMessageChecker;

    @GetMapping("oauth/mp")
    public void mp(@RequestParam(value = "code") String code, @RequestParam(value = "state", required = false) String state) {

        log.info("code:{}, state:{}", code, state);
        AccessToken accessToken = MpOAuth2s.defaultOAuth2s().getAccessToken(code);
        OpenUser openUser = MpOAuth2s.defaultOAuth2s().userInfo(accessToken.getAccessToken(), accessToken.getOpenId());
        //do your logic
        log.debug("这里就是相应的消息了！");
    }


    /**
     * 公众号回调接口
     * 这里为了演示方便使用单个固定URL，实际使用的时候一个一个系统可能有多个公众号，这样的话需要有个分发逻辑：
     * 比如callback url可以定义为  /wx/mp/[公众号的appId]，通过appId构造不同的AppSetting
     *
     * @param signature
     * @param msg_signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param encrypt_type
     * @param content
     * @return
     */
    @RequestMapping("/wx/mp")
    @ResponseBody
    public String mp(@RequestParam(value = "signature") String signature,
                     @RequestParam(value = "msg_signature", required = false) String msg_signature,
                     @RequestParam(value = "timestamp") String timestamp,
                     @RequestParam(value = "nonce") String nonce,
                     @RequestParam(value = "echostr", required = false) String echostr,
                     @RequestParam(value = "encrypt_type", required = false) String encrypt_type,
                     @RequestBody(required = false) String content) {

        log.info("signature={}, msg_signature={}, timestamp={}, nonce={}, echostr={}, encrypt_type={}", signature, msg_signature, timestamp, nonce, echostr, encrypt_type);

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

        if (!StringUtils.isEmpty(echostr)) {
            return echostr;
        }

        XmlMessageHeader xmlRequest = null;
        if ("aes".equals(encrypt_type)) {
            try {
                MessageDecryption messageDecryption = new MessageDecryption(appSetting.getToken(), appSetting.getAesKey(), appSetting.getAppId());
                xmlRequest = MpXmlMessages.fromXml(messageDecryption.decrypt(msg_signature, timestamp, nonce, content));
                XmlMessageHeader xmlResponse = mpDispatch(xmlRequest);

                if (xmlResponse != null) {
                    try {
                        return messageDecryption.encrypt(MpXmlMessages.toXml(xmlResponse), timestamp, nonce);
                    } catch (WxRuntimeException e) {

                    }
                }
            } catch (AesException e) {
            }
        } else {
            xmlRequest = MpXmlMessages.fromXml(content);
            XmlMessageHeader xmlResponse = mpDispatch(xmlRequest);
            if (xmlResponse != null) {
                try {
                    return MpXmlMessages.toXml(xmlResponse);
                } catch (WxRuntimeException e) {
                }
            }
        }
        return "success";
    }

    private XmlMessageHeader mpDispatch(XmlMessageHeader xmlRequest) {
        try {
            if (!duplicatedMessageChecker.isDuplicated(xmlRequest.getFromUser() + xmlRequest.getCreateTime().getTime())) {
                String welcome = "您好:" + xmlRequest.getFromUser();
                log.info("后台收到了消息：" + welcome);
//                    + Users.defaultUsers().get(xmlRequest.getFromUser()).getNickName();
//                CareMessages.defaultCareMessages().text(xmlRequest.getFromUser(), welcome);

                if (xmlRequest instanceof EventRequest) {
                    EventRequest eventRequest = (EventRequest) xmlRequest;
                    log.debug("事件请求[{}]", eventRequest.getEventType().name());
//                    CareMessages.defaultCareMessages().text(xmlRequest.getFromUser(), "事件请求:" + eventRequest.getEventType().name());
                } else {
                    log.debug("消息请求[{}]", xmlRequest.getMsgType().name());
//                    CareMessages.defaultCareMessages().text(xmlRequest.getFromUser(), "消息请求:" + xmlRequest.getMsgType().name());
                }
            } else {
                log.warn("Duplicated message: {} @ {}", xmlRequest.getMsgType(), xmlRequest.getFromUser());
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
