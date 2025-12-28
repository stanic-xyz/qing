package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String nickname;

    private String avatar;

    private String phone;

    private String email;

    private String description;

}
