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
import cn.chenyunlong.qing.auth.domain.user.enums.EncoderType;
import cn.chenyunlong.qing.auth.domain.user.event.UserConnectionAdded;
import cn.chenyunlong.qing.auth.domain.user.event.UserRegistered;
import cn.chenyunlong.qing.auth.domain.user.event.UserUnlocked;
import cn.chenyunlong.qing.auth.domain.user.exception.UserAlreadyExistsException;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.hutool.crypto.digest.BCrypt;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class QingUser extends BaseAggregate<QingUserId> {

    @FieldDesc(name = "用户唯一ID")
    private Long uid;

    @FieldDesc(name = "用户名", description = "用户（唯一），用于前端显示！")
    private String username;

    @FieldDesc(name = "昵称", description = "用户名称（唯一），用于前端显示！")
    private String nickname;

    @FieldDesc(name = "密码（加密之后的密码）")
    private String encodedPassword;

    private EncoderType encoderType;

    @FieldDesc(name = "密码过期")
    private Boolean credentialsExpired;

    private boolean active;

    private boolean locked;

    private String phone;
    private String email;
    private String avatar;
    private String description;

    private LocalDateTime expireTime;

    private MFAType mfaType;

    private String mfaKey;

    private List<UserConnection> userConnections;

    private List<UserToken> userTokens;

    // 注册时校验用户名唯一性（依赖领域服务）
    public static QingUser register(QingUserId qingUserId, String username, String password, UserRepository userRepository) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("用户名已存在: " + username);
        }
        QingUser user = new QingUser();
        user.setId(qingUserId);
        user.setUsername(username);
        user.setEncodedPassword(encodePassword(password)); // 密码加密
        user.setActive(false);
        user.setLocked(false);
        user.registerEvent(new UserRegistered(qingUserId));
        return user;
    }

    // 密码加密
    private static String encodePassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    // 恢复账号（解锁）
    public void unlock() {
        if (!this.isLocked()) {
            throw new IllegalStateException("用户未被锁定，无法恢复");
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
}
