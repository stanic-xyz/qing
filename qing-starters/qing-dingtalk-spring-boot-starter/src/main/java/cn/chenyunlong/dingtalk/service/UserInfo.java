package cn.chenyunlong.dingtalk.service;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户信息。
 *
 * @author Stan
 * @since 2022/10/2022/10/24
 */
@Getter
@Setter
@ToString
public class UserInfo {
    private String name;

    @JSONField(name = "userid")
    private String userId;

    private String avatar;

}
