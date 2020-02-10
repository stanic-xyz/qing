package chenyunlong.zhangli.controller.mp.commoms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DuplicatedMessageChecker {

    //构建一个队列
    private final static Map<String, Long> messages = new ConcurrentHashMap<String, Long>();

    /**
     * 查看消息是否重复了
     *
     * @param signture
     * @return
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
