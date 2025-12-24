/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions username the Mulan PSL v2.
 * You may obtain a copy username Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.application.dto.AuthenticationResultDTO;
import cn.chenyunlong.qing.auth.application.dto.dto.UserDTO;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.chenyunlong.qing.auth.domain.authentication.repository.AuthenticationRepository;
import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenCacheRepository;
import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenRepository;
import cn.chenyunlong.qing.auth.domain.authentication.service.TokenDomainService;
import cn.chenyunlong.qing.auth.domain.authentication.service.UserDomainService;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenId;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenType;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.permission.command.CreatePermissionCommand;
import cn.chenyunlong.qing.auth.domain.rbac.permission.command.DeletePermissionCommand;
import cn.chenyunlong.qing.auth.domain.rbac.permission.command.UpdatePermissionCommand;
import cn.chenyunlong.qing.auth.domain.rbac.permission.exception.PermissionDuplicateException;
import cn.chenyunlong.qing.auth.domain.rbac.permission.exception.PermissionNotFoundException;
import cn.chenyunlong.qing.auth.domain.rbac.permission.repository.PermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.command.RemovePermissionFromRoleCommand;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission.RolePermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.repository.UserRoleRepository;
import cn.chenyunlong.qing.auth.domain.role.command.CreateRoleCommand;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.command.AuthenticationByUsernamePasswordCommand;
import cn.chenyunlong.qing.auth.domain.user.command.UserActiveCommand;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.auth.domain.user.specification.UserAuthenticationSpecification;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证应用服务
 * 负责处理认证相关的应用层逻辑
 *
 * @author 陈云龙
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final TokenDomainService tokenDomainService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserDomainService userDomainService;
    private final JwtTokenService jwtTokenService;
    private final UserAuthenticationSpecification authenticationSpecification;
    private final AuthenticationRepository authenticationRepository;
    private final TokenRepository userTokenRepository;
    private final TokenCacheRepository tokenCacheRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * 用户登录
     *
     * @param command 登录请求
     * @return 认证结果
     */
    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResultDTO login(AuthenticationByUsernamePasswordCommand command) {
        // 2. 查找用户
        User user = userRepository.findByUsername(command.getUsername()).orElseThrow(() -> new AuthenticationException("用户不存在"));

        // 规约收敛：状态、锁定、密码校验
        authenticationSpecification.check(user, command.getPassword(), command.getClientIpAddress(), command.getUserAgent());
        user.resetLoginAttempts();
        userRepository.save(user);


        // 4. 认证成功处理
        log.info("用户认证成功: username={}, user={}", command.getUsername(), user.getId());

        List<UserRole> userRoles = user.getRoles();
        Set<Permission> permissionSet = userRoles.stream().flatMap(userRole -> {
            Set<Permission> rolePermissions = userRole.getPermissions();
            List<Permission> objectArrayList = CollUtil.toList();
            return rolePermissions == null ? objectArrayList.stream() : rolePermissions.stream();
        }).collect(Collectors.toSet());
        List<Role> roleList = userRoles.stream().map(UserRole::getRole).toList();
        List<String> permissionCodes = permissionSet.stream().map(Permission::getCode).distinct().toList();
        List<String> roleCodes = roleList.stream().map(Role::getCode).distinct().toList();

        // 生成 JWT 令牌
        String accessToken = jwtTokenService.generateAccessToken(user, null, roleCodes, permissionCodes);
        String refreshToken = jwtTokenService.generateRefreshToken(user);

        user.recordLoginSuccess(command.getClientIpAddress(), command.getUserAgent());

        AuthenticationToken authenticationToken = AuthenticationToken.create(TokenId.generate(), accessToken,
                TokenType.JWT, refreshToken, user.getId(), Instant.now(), command.getUserAgent());
        userTokenRepository.save(authenticationToken);

        return AuthenticationResultDTO.success(authenticationToken, user);
    }

    /**
     * 激活用户
     */
    @Transactional
    public void activateUser(String username, String activeCode) {
        userDomainService.active(UserActiveCommand.create(username, activeCode));
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @return 认证结果
     */
    @Transactional
    public UserDTO getUserInfoByToken(String token) {
        // 验证JWT令牌
        String usernameFromToken = jwtTokenService.getUsernameFromToken(token);
        return userRepository.findByUsername(Username.of(usernameFromToken)).map(UserDTO::from)
                .orElseThrow();
    }

    /**
     * 注销登录
     *
     * @param token 令牌
     */
    @Transactional
    public void logout(String token) {
        try {
            // 撤销令牌
            tokenDomainService.revokeToken(token, "用户注销");
            // 发送
        } catch (Exception exception) {
            log.error("注销失败", exception);
            throw new AuthenticationException("注销失败: " + exception.getMessage());
        }
    }

    /**
     * 使用刷新令牌刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    public String refreshToken(@NotBlank(message = "刷新令牌不能为空") String refreshToken) {
        Optional<AuthenticationToken> tokenOpt = userTokenRepository.findByRefreshTokenValue(refreshToken);
        if (tokenOpt.isEmpty()) {
            throw new AuthenticationException("令牌不存在");
        }

        // 判断当前的token是否已注销
        if (tokenCacheRepository.isBlacklisted(refreshToken)) {
            throw new AuthenticationException("刷新令牌已注销");
        }

        boolean token = jwtTokenService.validateRefreshToken(refreshToken);
        Assert.isTrue(token, "刷新令牌无效");

        // 1. 验证刷新令牌
        Claims claims = jwtTokenService.parseToken(refreshToken);
        String username = claims.getSubject();

        User user = userRepository.findByUsername(Username.of(username))
                .orElseThrow(() -> new AuthenticationException("用户不存在"));

        String accessToken = jwtTokenService.refreshAccessToken(refreshToken, user);
        log.info("刷新令牌成功: {}", accessToken);

        return accessToken;
    }

    public void createRole(CreateRoleCommand command) {

        if (roleRepository.existsByCode(command.getCode())) {
            throw new AuthenticationException("角色编码重复");
        }

        if (roleRepository.existsByName(command.getName())) {
            throw new AuthenticationException("角色已存在");
        }

        Role role = Role.create(command.getName(), command.getCode(), command.getDescription(), "system");
        roleRepository.save(role);
    }

    /**
     * 创建权限
     *
     * @param command 创建权限命令
     */
    public void createPermission(CreatePermissionCommand command) {
        if (permissionRepository.existsByCode(command.getCode())) {
            throw new PermissionDuplicateException("权限编码重复");
        }
        if (permissionRepository.existsByName(command.getName())) {
            throw new PermissionDuplicateException("权限名称重复");
        }
        Permission permission = Permission.create(
                command.getName(),
                command.getCode(),
                command.getType(),
                command.getResource(),
                command.getAction(),
                "system");
        permissionRepository.save(permission);
    }

    /**
     * 更新权限
     *
     * @param command 更新权限命令
     */
    public void updatePermission(UpdatePermissionCommand command) {
        Permission permission = permissionRepository.findById(command.getId())
                .orElseThrow(() -> new PermissionNotFoundException("权限不存在"));
        permission.updateInfo(command.getName(), command.getDescription(), command.getResource(), command.getAction(),
                "system");
        permission.setSortOrder(command.getSortOrder());
        permissionRepository.save(permission);
    }

    /**
     * 删除权限
     *
     * @param command 删除权限命令
     */
    public void deletePermission(DeletePermissionCommand command) {
        Permission permission = permissionRepository.findById(command.getId())
                .orElseThrow(() -> new PermissionNotFoundException("权限不存在"));
        if (!permission.canDelete()) {
            throw new AuthenticationException("权限包含子权限，无法删除");
        }
        // 领域仓储接口没有删除方法，采用标记删除或由基础设施层实现
        // 这里简化为禁用
        permission.disable("system");
        permissionRepository.save(permission);
    }

    /**
     * 为角色批量关联权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     */
    @Transactional
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // 校验角色存在
        roleRepository.findById(RoleId.of(roleId))
                .orElseThrow(() -> new AuthenticationException("角色不存在"));

        // 校验权限ID列表有效
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new AuthenticationException("权限ID列表不能为空");
        }

        List<Permission> permissions = permissionRepository.findByIds(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new AuthenticationException("存在无效的权限ID");
        }

        // 逐个建立关联（去重处理）
        for (Long pid : permissionIds) {
            if (!rolePermissionRepository.existsByRoleIdAndPermissionId(RoleId.of(roleId), PermissionId.of(pid))) {
                RolePermission entity = RolePermission.create(RoleId.of(roleId), PermissionId.of(pid));
                rolePermissionRepository.save(entity);
            }
        }

        log.info("角色[{}]已关联权限:{}", roleId, permissionIds);
    }

    /**
     * 取消角色的单个权限关联
     *
     */
    @Transactional
    public void removePermissionFromRole(RemovePermissionFromRoleCommand command) {
        // 校验角色与权限存在
        roleRepository.findById(command.roleId())
                .orElseThrow(() -> new AuthenticationException("角色不存在"));
        permissionRepository.findById(command.permissionId())
                .orElseThrow(() -> new PermissionNotFoundException("权限不存在"));

        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(command.roleId(), command.permissionId())) {
            throw new AuthenticationException("关联不存在");
        }
        rolePermissionRepository.deleteByRoleIdAndPermissionId(command.roleId(), command.permissionId());
        log.info("角色[{}]已取消权限关联:{}", command.roleId().id(), command.permissionId().id());
    }
}
