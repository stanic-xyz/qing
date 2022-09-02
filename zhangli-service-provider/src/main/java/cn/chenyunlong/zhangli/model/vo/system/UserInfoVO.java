package cn.chenyunlong.zhangli.model.vo.system;

import cn.chenyunlong.zhangli.model.dto.base.OutputConverter;
import cn.chenyunlong.zhangli.model.entities.User;
import lombok.Data;

@Data
public class UserInfoVO implements OutputConverter<UserInfoVO, User> {

    private String username;
    private String nickname;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private String description;
    private String token;
}