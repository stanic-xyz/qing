package cn.chenyunlong.zhangli.remote;

import org.springframework.stereotype.Component;

/**
 * @author Stan
 */
@Component
public class BaseServiceFallBack implements BaseService {

    @Override
    public String getUserInfo(String appid, String access_token, String sysIDs, String schoolId) {
        return "err:failed";
    }
}
