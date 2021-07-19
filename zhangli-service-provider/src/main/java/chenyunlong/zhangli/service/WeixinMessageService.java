package chenyunlong.zhangli.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeixinMessageService {
    private final Logger log = LoggerFactory.getLogger(WeixinMessageService.class);
    //构建一个队列
    private final static Map<String, Long> messages = new ConcurrentHashMap<String, Long>();

    /**
     * 查看消息是否重复了
     *
     * @param signture 消息签名
     * @return 消息是否重复
     */
    public boolean isDuplicated(String signture) {

        boolean isDuplicated = messages.containsKey(signture);
        log.debug("是否被处理：" + isDuplicated);
        if (!isDuplicated) {
            messages.put(signture, System.currentTimeMillis());
        }

        Long currentMills = System.currentTimeMillis();

        for (String key : messages.keySet()) {
            Long aLong = messages.get(key);
            if (currentMills - aLong > 10000) {
                messages.remove(key);
            }
        }
        return isDuplicated;
    }
}
