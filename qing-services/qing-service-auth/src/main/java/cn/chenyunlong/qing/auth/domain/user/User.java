/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.auth.domain.user;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.UserLockReason;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.user.enums.EncoderType;
import cn.chenyunlong.qing.auth.domain.user.event.*;
import cn.chenyunlong.qing.auth.domain.user.valueObject.*;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWT;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;

/**
 * 用户信息
 *
 * @author 陈云龙
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class User extends BaseSimpleBusinessEntity<UserId> {

    /**
     * 用户唯一ID
     */
    @FieldDesc(name = "用户唯一ID")
    private Long uid;

    /**
     * 用户名，用于前端显示
     */
    @FieldDesc(name = "用户名", description = "用户（唯一），用于前端显示！")
    private Username username;

    /**
     * 昵称，用户名称（唯一），用于前端显示
     */
    @FieldDesc(name = "昵称", description = "用户名称（唯一），用于前端显示！")
    private String nickname;

    /**
     * 密码（加密之后的密码）
     */
    @FieldDesc(name = "密码（加密之后的密码）")
    private EncryptedPassword encodedPassword;

    /**
     * 密码编码类型
     */
    private EncoderType encoderType;

    /**
     * 密码是否过期
     */
    @FieldDesc(name = "密码过期")
    private Boolean credentialsExpired;

    /**
     * 用户是否激活
     */
    private boolean active;

    /**
     * 激活码
     */
    private String activationCode;

    /**
     * 激活码过期时间
     */
    private Instant activeCodeExpireAt;

    /**
     * 用户是否被锁定
     */
    private boolean locked;

    /**
     * 用户是否被暂停
     */
    private boolean isSuspended;

    /**
     * 用户是否被删除
     */
    private boolean isDeleted;

    /**
     * 用户锁定原因
     */
    private UserLockReason userLockReason;

    /**
     * 用户手机号
     */
    private PhoneNumber phone;

    /**
     * 用户邮箱
     */
    private Email email;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户描述
     */
    private String description;

    /**
     * 过期时间
     */
    private Instant expireTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 登录尝试次数
     */
    private int loginAttempts = 0;

    /**
     * 锁定截止时间
     */
    private Instant lockedUntil;

    /**
     * 注册时间
     */
    private Instant registeredAt;

    /**
     * 最后登录时间
     */
    private Instant lastLoginAt;

    /**
     * 多因素认证类型
     */
    private MFAType mfaType;

    /**
     * 多因素认证密钥
     */
    private String mfaKey;

    /**
     * 用户连接列表
     */
    private List<UserConnection> userConnections;

    /**
     * 用户令牌列表
     */
    private List<AuthenticationToken> userTokens;

    /**
     * 用户角色列表
     */
    private List<UserRole> roles;

    /**
     * 版本号，用于乐观锁
     */
    private Integer version = 0;

    /**
     * 注册时校验用户名唯一性（依赖领域服务）
     *
     * @param userId      用户ID
     * @param username    用户名
     * @param rawPassword 密码
     * @param nickname    昵称
     * @return 用户
     */
    public static User create(UserId userId, Username username, RawPassword rawPassword, String nickname) {
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setNickname(nickname);
        user.setEncodedPassword(EncryptedPassword.of(rawPassword.getValue())); // 密码加密
        user.setActive(false);
        user.setLocked(false);
        return user;
    }

    /**
     * 注册新用户的方法
     * 此方法负责创建新用户并设置其基本信息，同时生成激活代码并注册用户注册事件
     *
     * @param userId      用户的唯一标识符
     * @param username    用户的用户名
     * @param rawPassword 用户的密码
     * @param phoneNumber 用户的手机号码
     * @param email       用户的电子邮箱
     * @return 返回创建并配置好的 QingUser 对象
     */
    // 注册时校验用户名唯一性（依赖领域服务）
    public static User register(UserId userId, Username username, RawPassword rawPassword, PhoneNumber phoneNumber, Email email, String nickname) {
        // 创建用户对象，传入用户ID、用户名和密码值
        User user = create(userId, username, rawPassword, nickname);

        // 设置用户的手机号码
        user.setPhone(phoneNumber);
        // 设置用户的电子邮箱
        user.setEmail(email);
        // 设置用户注册时间为当前时间
        user.setRegisteredAt(Instant.now());

        // 为用户生成激活代码
        user.generateActivationCode();

        // 注册用户已注册事件，用于领域事件处理
        user.registerEvent(new UserRegistered(user));
        // 返回创建并配置完成的用户对象
        return user;
    }

    /**
     * 根据用户 ID 和用户名恢复用户信息的静态方法
     *
     * @param id       用户ID对象，包含用户的唯一标识信息
     * @param username 用户名对象，包含用户的名称信息
     * @param avatar   头像
     * @return 返回一个恢复后的QingUser对象，包含传入的ID和用户名信息
     */
    public static User reconstruct(UserId id,
                                   Long uid,
                                   Username username,
                                   EncryptedPassword encodedPassword,
                                   EncoderType encoderType,
                                   Email email,
                                   PhoneNumber phone,
                                   String nickname,
                                   boolean active,
                                   boolean locked,
                                   Instant registeredAt,
                                   Instant lastLoginAt,
                                   String lastLoginIp,
                                   MFAType mfaType,
                                   String mfaKey,
                                   List<UserRole> roles,
                                   String activationCode, Instant activeCodeExpireAt,
                                   String avatar) {
        User domain = User.builder()
                .uid(uid)
                .username(username)
                .encodedPassword(encodedPassword)
                .encoderType(encoderType)
                .email(email)
                .phone(phone)
                .nickname(nickname)
                .active(active)
                .locked(locked)
                .registeredAt(registeredAt)
                .lastLoginAt(lastLoginAt)
                .lastLoginIp(lastLoginIp)
                .mfaType(mfaType)
                .mfaKey(mfaKey)
                .roles(roles)
                .activationCode(activationCode)
                .activeCodeExpireAt(activeCodeExpireAt)
                .avatar(avatar)
                .build();
        domain.setId(id);
        return domain;
    }

    // 用户登录
    public void login(String ip, String userAgent) {
        if (isLocked()) {
            throw new IllegalStateException("User account is locked");
        }

        this.lastLoginAt = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        this.lastLoginIp = ip;
        this.loginAttempts = 0;
        //        DomainEventPublisher.publish(new UserLoginEvent(this, ip, userAgent));
    }

    /**
     * 生成6位的纯数字的激活码
     */
    public void generateActivationCode() {
        String activeCode = RandomStringUtils.secure().nextNumeric(6);
        setActivationCode(activeCode);
        setActiveCodeExpireAt(Instant.now().plusSeconds(60 * 5));
    }

    /**
     * 修改密码
     *
     * @param newPasswordHash 新密码的哈希值
     */
    public void changePassword(EncryptedPassword newPasswordHash) {
        if (newPasswordHash == null) {
            throw new IllegalArgumentException("新密码不能为空");
        }
        this.encodedPassword = newPasswordHash;
        registerEvent(new UserPasswordChanged(getId()));
    }

    /**
     * 重置密码（忘记密码场景）
     */
    public void resetPassword(EncryptedPassword newPassword) {
        validateNotLocked();
        this.encodedPassword = newPassword;
        resetLoginAttempts();
        // 重置登录尝试次数
        this.loginAttempts = 0;

        registerEvent(new PasswordReset(this.id));
    }

    private void validateNotLocked() {
        if (isLocked()) {
            throw new IllegalArgumentException("用户账户已被锁定，请联系管理员解锁！");
        }
    }

    public void activateBySelf() {
        if (active) {
            throw new IllegalArgumentException("用户已激活，无需再次激活！");
        }
        this.active = true;
        registerEvent(new UserActivated(getId()));
    }

    public void activateByAdmin() {
        if (active) {
            throw new IllegalArgumentException("用户已激活，无需再次激活！");
        }
        this.active = true;
        registerEvent(new UserActivated(getId()));
    }

    public void deactivateByAdmin() {
        this.active = false;
        registerEvent(new UserDeactivated(getId()));
    }

    public void lockAccount(UserLockReason userLockReason, Duration lockDuration) {
        if (this.isLocked()) {
            throw new IllegalStateException("账户已被锁定");
        }

        setLocked(true);
        setUserLockReason(userLockReason);
        setLockedUntil(LocalDateTime.now().plus(lockDuration).toInstant(ZoneOffset.UTC));
        registerEvent(new UserLocked(getId()));
    }

    // 恢复账号（解锁）
    public void unlock() {
        if (!this.isLocked()) {
            throw new IllegalStateException("账户未被锁定");
        }
        this.locked = false;
        this.registerEvent(new UserUnlocked(getId()));
    }

    /**
     * 绑定第三方用户信息
     *
     * @param connection 第三方用户0
     */
    public void addConnection(UserConnection connection) {
        List<UserConnection> connectionList = getUserConnections();
        boolean hasBound = connectionList.stream().noneMatch(userConnection ->
                userConnection.equals(connection));
        if (hasBound) {
            return;
        }
        connectionList.add(connection);
        registerEvent(new UserConnectionAdded(getId(), connection));
    }


    // 生成JWT
    public String generateJwt() {
        HashMap<String, Object> payloads = MapUtil.newHashMap();

        payloads.put("username", username.value());
        payloads.put("email", email.value());
        //        payloads.put("roles", roles.stream().map(UserRole::getCode).toList());
        //        payloads.put("permissions", permissions.stream().map(Enum::name).toList());

        return JWT.create()
                .addPayloads(payloads)
                .setExpiresAt(new java.util.Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000)) // 2小时过期
                .sign();
    }


    @Override
    public void init() {
        super.init();
    }

    public boolean isPasswordExpired() {
        return false;
    }

    /**
     * 使用 BCrypt 对比明文密码与存储的加密密码
     *
     * @param password 登录输入的密码（以值对象形式承载明文）
     * @return 是否校验通过
     */
    public boolean verifyPassword(EncryptedPassword password) {
        return DigestUtil.bcryptCheck(password.value(), encodedPassword.value());
    }

    public void recordLoginSuccess(IpAddress clientIpAdds, String userAgent) {
        resetLoginAttempts();
        setLastLoginIp(clientIpAdds.getValue());
        setLastLoginAt(Instant.now());
    }


    public void incrementLoginAttempts() {
        loginAttempts++;
    }

    public void resetLoginAttempts() {
        loginAttempts = 0;
    }
}
