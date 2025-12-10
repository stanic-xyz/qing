package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.UserConnection;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.auth.domain.user.dto.query.UserQuery;
import cn.chenyunlong.qing.auth.domain.user.dto.request.UserCreateRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.request.UserQueryRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.request.UserUpdateRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.auth.domain.user.enums.EncoderType;
import cn.chenyunlong.qing.auth.domain.user.valueObject.*;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.GenericEnumMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.QingUserEntity;
import cn.hutool.core.collection.CollUtil;
import org.mapstruct.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {
        GenericEnumMapper.class,
        DateMapper.class,
        CustomMapper.class,
        AggregateMapper.class,
}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserUpdater request2Updater(UserUpdateRequest request);

    @Mapping(source = "password.password", target = "password")
    UserCreator request2Dto(UserCreateRequest request);

    UserQuery request2Query(UserQueryRequest request);

    default User entityToDomain(QingUserEntity userEntity) {
        // 转换地址（JSON字符串到值对象）
        return toDomainObject(userEntity);
    }

    default String map(Username value) {
        return value != null ? value.value() : null;
    }

    default String map(Email value) {
        return value != null ? value.value() : null;
    }


    // ==================== 从数据库对象到领域对象 ====================

    /**
     * 从数据库实体复原完整领域对象
     *
     * @param entity 数据库实体
     * @return 领域对象
     */
    default User toDomainObject(QingUserEntity entity) {
        if (entity == null) {
            return null;
        }
        ArrayList<UserRole> userRoles = CollUtil.newArrayList();

        User domain = User.reconstruct(
                UserId.of(entity.getId()),
                entity.getUid(),
                Username.of(entity.getUsername()),
                EncryptedPassword.ofEncrypted(entity.getPassword()),
                EncoderType.BCrypt,
                Email.of(entity.getEmail()),
                PhoneNumber.fromE164(entity.getPhone()),
                entity.getNickname(),
                entity.isActive(),
                entity.isLocked(),
                entity.getRegisteredAt(),
                entity.getLastLoginAt(),
                entity.getLastLoginIp(),
                entity.getMfaType(),
                entity.getMfaKey(),
                userRoles,
                entity.getActivationCode(), entity.getActiveCodeExpireAt(), entity.getAvatar()
        );

        // 7. 应用后置处理
        afterToDomainObject(domain, entity);

        return domain;
    }

    /**
     * 批量转换数据库实体到领域对象
     */
    default List<User> toDomainObjects(List<QingUserEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toDomainObject)
                .collect(Collectors.toList());
    }

    /**
     * 从数据库实体复原领域对象（包含关联数据）
     */
    default User toDomainObjectWithAssociations(
            QingUserEntity entity,
            List<UserConnection> connections,
            List<AuthenticationToken> tokens,
            List<Role> roles,
            List<Permission> permissions) {

        User domain = toDomainObject(entity);

        if (domain != null) {
            // 设置关联数据
            domain.setUserConnections(connections != null ? connections : Collections.emptyList());
            domain.setUserTokens(tokens != null ? tokens : Collections.emptyList());
        }

        return domain;
    }

    // ==================== 从领域对象到数据库对象 ====================

    /**
     * 将领域对象转换为数据库实体
     */
    default QingUserEntity toDataObject(User domain) {
        if (domain == null) {
            return null;
        }

        QingUserEntity entity = new QingUserEntity();

        // 1. 基础标识转换
        UserId domainId = domain.getId();
        entity.setId(domainId != null ? domainId.id() : null);
        entity.setUid(domain.getUid());

        // 2. 值对象拆箱
        entity.setUsername(domain.getUsername() != null ? domain.getUsername().value() : null);
        entity.setEmail(domain.getEmail() != null ? domain.getEmail().value() : null);

        // 3. 密码相关
        entity.setPassword(domain.getEncodedPassword() != null ?
                domain.getEncodedPassword().value() : null);

        // 4. 基础字段映射
        entity.setNickname(domain.getNickname());
        entity.setAvatar(domain.getAvatar());
        entity.setDescription(domain.getDescription());
        PhoneNumber phone = domain.getPhone();

        entity.setPhone(phone != null ? phone.normalized() : null);

        entity.setCredentialsExpired(domain.getCredentialsExpired());
        entity.setExpireTime(domain.getExpireTime());
        entity.setMfaType(domain.getMfaType());
        entity.setMfaKey(domain.getMfaKey());

        // 5. 状态字段持久化
        entity.setActive(domain.isActive());
        entity.setLocked(domain.isLocked());
        entity.setDeleted(domain.isDeleted());
        entity.setSuspended(domain.isSuspended());

        ValidStatus validStatus = domain.getValidStatus();
        entity.setValidStatus(validStatus);

        // 6. 时间字段
        if (domain.getRegisteredAt() != null) {
            entity.setCreatedAt(domain.getRegisteredAt());
        }

        entity.setLastLoginAt(domain.getLastLoginAt());
        entity.setLastLoginIp(domain.getLastLoginIp());
        entity.setLoginAttempts(domain.getLoginAttempts());
        entity.setLockedUntil(domain.getLockedUntil());

        entity.setActivationCode(domain.getActivationCode());
        entity.setActiveCodeExpireAt(domain.getActiveCodeExpireAt());

        // 7. 应用后置处理
        afterToDataObject(entity, domain);

        return entity;
    }

    // ==================== 辅助方法 ====================

    /**
     * 判断用户是否活跃（从数据库状态复原）
     */
    private boolean isActive(QingUserEntity entity) {
        return entity.isActive();
    }

    /**
     * 判断用户是否锁定（从数据库状态复原）
     */
    private boolean isLocked(QingUserEntity entity) {
        return entity.isLocked();
    }

    /**
     * 恢复领域状态（处理领域事件等）
     */
    private void restoreDomainState(User domain, QingUserEntity entity) {
        // 如果有存储领域事件，可以在这里恢复
        // 例如：从额外字段解析领域事件并重新注册
    }

    // ==================== 后置处理方法 ====================

    /**
     * 转换为领域对象后的处理
     */
    @AfterMapping
    default void afterToDomainObject(@MappingTarget User domain, QingUserEntity source) {
        // 1. 设置值对象的默认值
        if (domain.getEncoderType() == null) {
            domain.setEncoderType(EncoderType.BCrypt); // 默认加密方式
        }

        // 2. 恢复业务状态
        if (domain.getLockedUntil() != null &&
                domain.getLockedUntil().isBefore(Instant.now())) {
            // 锁定已过期，自动解锁
            domain.setLocked(false);
            domain.setLockedUntil(null);
        }

        if (source.getVersion() != null) {
            domain.setVersion(source.getVersion());
        }
    }

    /**
     * 转换为数据库对象后的处理
     */
    @AfterMapping
    default void afterToDataObject(@MappingTarget QingUserEntity entity, User source) {
        // 1. 设置审计信息
        if (entity.getCreatedAt() == null && source.getRegisteredAt() != null) {
            //            entity.setCreatedAt(java.sql.Timestamp.valueOf(source.getRegisteredAt()));
        }

        // 2. 设置更新时间
        //        entity.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        // 3. 处理密码过期逻辑
        if (source.getCredentialsExpired() == null) {
            entity.setCredentialsExpired(false);
        }

        // 4. 设置版本控制（如果支持乐观锁）
        entity.setVersion(source.getVersion());
    }

    // ==================== 部分更新转换 ====================

    /**
     * 部分更新：将领域对象的变更应用到数据库实体
     */
    default void updateDataObject(@MappingTarget QingUserEntity target, User source) {
        // 只更新非空字段

        if (source.getUsername() != null) {
            target.setUsername(source.getUsername().value());
        }

        if (source.getEmail() != null) {
            target.setEmail(source.getEmail().value());
        }

        if (source.getEncodedPassword() != null) {
            target.setPassword(source.getEncodedPassword().value());
        }

        if (source.getNickname() != null) {
            target.setNickname(source.getNickname());
        }

        if (source.getPhone() != null) {
            target.setPhone(source.getPhone().value());
        }

        if (source.getAvatar() != null) {
            target.setAvatar(source.getAvatar());
        }

        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }

        if (source.getExpireTime() != null) {
            target.setExpireTime(source.getExpireTime());
        }

        if (source.getMfaType() != null) {
            target.setMfaType(source.getMfaType());
        }

        if (source.getMfaKey() != null) {
            target.setMfaKey(source.getMfaKey());
        }

        // 状态字段
        target.setActive(source.isActive());
        target.setLocked(source.isLocked());
        target.setDeleted(source.isDeleted());
        target.setSuspended(source.isSuspended());

        // 时间字段
        if (source.getLastLoginAt() != null) {
            //            target.setLastLoginAt(source.getLastLoginAt());
        }

        if (source.getLastLoginIp() != null) {
            //            target.setLastLoginIp(source.getLastLoginIp());
        }

        //        target.setLoginAttempts(source.getLoginAttempts());
        //        target.setLockedUntil(source.getLockedUntil());
    }
}

