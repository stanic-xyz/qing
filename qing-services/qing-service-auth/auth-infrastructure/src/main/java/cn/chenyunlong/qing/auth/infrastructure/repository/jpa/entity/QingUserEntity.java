package cn.chenyunlong.qing.infrastructure.auth.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "sys_user")
public class QingUserEntity extends BaseJpaEntity {

    @Column(unique = true)
    @FieldDesc(name = "用户唯一ID")
    private Long uid;

    @FieldDesc(name = "用户名", description = "用户（唯一），用于前端显示！")
    @Column(unique = true)
    private String username;

    @FieldDesc(name = "昵称", description = "用户名称（唯一），用于前端显示！")
    @Column(unique = true)
    private String nickname;

    private String password;

    @FieldDesc(name = "密码过期")
    private Boolean credentialsExpired;

    private String phone;
    private String email;
    private String avatar;
    private String description;
    private LocalDateTime expireTime;
    private MFAType mfaType;
    private String mfaKey;
}
