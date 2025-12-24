package cn.chenyunlong.qing.auth.application.dto.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {

    private String username;
    private String password;
    private String email;
}
