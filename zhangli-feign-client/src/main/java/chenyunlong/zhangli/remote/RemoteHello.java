package chenyunlong.zhangli.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Stan
 */
@FeignClient(value = "zhangli-service-provider", fallback = MyFeignFallBack.class)
public interface RemoteHello {

    /**
     * 获取用户信息
     *
     * @param keyword 关键字
     * @return 用户信息
     */
    @GetMapping("activity/list")
    String getUserInfo(@RequestParam String keyword);
}
