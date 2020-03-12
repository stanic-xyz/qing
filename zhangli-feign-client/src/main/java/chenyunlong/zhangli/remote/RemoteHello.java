package chenyunlong.zhangli.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "zhangli-service-provider", fallback = MyFeignFallBack.class)
public interface RemoteHello {

    @GetMapping("authrize/getUserInfo")
    String getUserList(@RequestParam String name);
}
