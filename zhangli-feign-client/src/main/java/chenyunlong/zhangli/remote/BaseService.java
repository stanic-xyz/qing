package chenyunlong.zhangli.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Stan
 */
@FeignClient(name = "base-platform", url = "192.168.129.1:30103", fallback = BaseServiceFallBack.class)
public interface BaseService {

    /**
     * 获取系统的基本信息
     *
     * @param appid        应用ID
     * @param access_token 访问密钥
     * @param sysIDs       系统ID列表
     * @param schoolId     学校ID
     * @return 学校的基本信息
     */
    @GetMapping("BaseApi/Global/GetSubSystemsInfoBySchool")
    String getUserInfo(@RequestParam String appid,
                       @RequestParam String access_token,
                       @RequestParam String sysIDs,
                       @RequestParam String schoolId);
}
