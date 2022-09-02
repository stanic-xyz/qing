package cn.chenyunlong.zhangli.remote;

import org.springframework.stereotype.Component;

/**
 * @author Stan
 */
@Component
public class MyFeignFallBack implements RemoteHello {
    @Override
    public String getUserInfo(String name) {
        String faildStr = "失败了";
        System.out.print(faildStr);
        return faildStr;
    }
}