package chenyunlong.zhangli.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "zhangli-service-provider", fallback = MyFeignFallBack.class)
public interface RemoteHello {

    @GetMapping("activity/list")
    String getUserInfo(@RequestParam String keyword);
}
