package chenyunlong.zhangli.entities;


import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public class User implements Serializable {

    private Long userID;
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

    public User(Long userID, String username, String password, String phone, String openId, String email) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.openId = openId;
        this.email = email;
    }

    public User(String subject, String s, Collection<? extends GrantedAuthority> authorities) {
        this.username = subject;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", openId='" + openId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
