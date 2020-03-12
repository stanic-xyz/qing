package chenyunlong.zhangli.remote;

import org.springframework.stereotype.Component;

@Component
public class MyFeignFallBack implements RemoteHello {
    @Override
    public String getUserList(String name) {
        return "失败了";
    }
}
