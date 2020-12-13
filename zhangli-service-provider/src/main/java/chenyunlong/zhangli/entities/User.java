package chenyunlong.zhangli.entities;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * 用户信息
 *
 * @author Stan
 */
@Data
public class User implements Serializable {

    private Long userId;
    private String username;
    private String password;
    private String phone;
    private String openId;
    private String email;

    public User() {
    }

    public User(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public User(Long userId, String username, String password, String phone, String openId, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.openId = openId;
        this.email = email;
    }

    public User(String subject, String s, Collection<? extends GrantedAuthority> authorities) {
        this.username = subject;
    }

}
