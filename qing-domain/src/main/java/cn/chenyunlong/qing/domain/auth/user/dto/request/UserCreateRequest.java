package cn.chenyunlong.qing.domain.auth.user.dto.request;

import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema
@Data
public class UserCreateRequest implements Request {
    @Schema(
            title = "uid",
            description = "用户唯一ID"
    )
    private Long uid;

    @Schema(
            title = "username",
            description = "用户名"
    )
    private String username;

    @Schema(
            title = "nickname",
            description = "昵称"
    )
    private String nickname;

    @Schema(
            title = "password",
            description = "password"
    )
    private String password;

    @Schema(
            title = "phone",
            description = "phone"
    )
    private String phone;

    @Schema(
            title = "email",
            description = "email"
    )
    private String email;

    @Schema(
            title = "avatar",
            description = "avatar"
    )
    private String avatar;

    @Schema(
            title = "description",
            description = "description"
    )
    private String description;

    @Schema(
            title = "expireTime",
            description = "expireTime"
    )
    private LocalDateTime expireTime;

    @Schema(
            title = "mfaType",
            description = "mfaType"
    )
    private MFAType mfaType;

    @Schema(
            title = "mfaKey",
            description = "mfaKey"
    )
    private String mfaKey;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public MFAType getMfaType() {
        return mfaType;
    }

    public void setMfaType(MFAType mfaType) {
        this.mfaType = mfaType;
    }

    public String getMfaKey() {
        return mfaKey;
    }

    public void setMfaKey(String mfaKey) {
        this.mfaKey = mfaKey;
    }
}
