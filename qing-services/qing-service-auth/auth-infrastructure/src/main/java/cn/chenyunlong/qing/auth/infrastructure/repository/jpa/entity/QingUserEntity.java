package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * 用户表数据库对象
 */
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
    private Instant expireTime;
    private MFAType mfaType;
    private String mfaKey;

    private boolean active;

    /**
     * 激活时间
     */
    private Instant activationTime;

    /**
     * 激活码
     */
    private String activationCode;

    /**
     * 激活码过期时间
     */
    private Instant activeCodeExpireAt;

    /**
     * 是否锁定
     */
    private boolean locked;

    /**
     * 已终止
     */
    private boolean isSuspended;

    /**
     * 已删除
     */
    private boolean isDeleted;

    /**
     * 尝试次数
     */
    private int loginAttempts = 0;

    /**
     * 锁定时间
     */
    private Instant lockedUntil;

    /**
     * 注册时间
     */
    private Instant registeredAt;

    /**
     * 上次登录时间
     */
    private Instant lastLoginAt;

    /**
     * 上次登录ip
     */
    private String lastLoginIp;
}
