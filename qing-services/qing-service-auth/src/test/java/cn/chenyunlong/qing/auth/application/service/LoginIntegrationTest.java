package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.BaseJpaIntegrationTest;
import cn.chenyunlong.qing.auth.application.dto.AuthenticationResultDTO;
import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenCacheRepository;
import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenRepository;
import cn.chenyunlong.qing.auth.domain.authentication.service.UserDomainService;
import cn.chenyunlong.qing.auth.domain.event.DomainEventPublisher;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionType;
import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.permission.command.CreatePermissionCommand;
import cn.chenyunlong.qing.auth.domain.rbac.permission.repository.PermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission.RolePermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.repository.UserRoleRepository;
import cn.chenyunlong.qing.auth.domain.role.command.CreateRoleCommand;
import cn.chenyunlong.qing.auth.domain.role.command.RoleAssignPermissionsCommand;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.command.AssignRoleCommand;
import cn.chenyunlong.qing.auth.domain.user.command.AuthenticationByUsernamePasswordCommand;
import cn.chenyunlong.qing.auth.domain.user.command.UserRegistrationCommand;
import cn.chenyunlong.qing.auth.domain.user.port.SecurityPolicyPort;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.auth.domain.user.specification.UserAuthenticationSpecification;
import cn.chenyunlong.qing.auth.domain.user.specification.UserRegistrationSpecification;
import cn.chenyunlong.qing.auth.infrastructure.repository.redis.RedisSessionRepository;
import cn.hutool.core.collection.CollUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 登录功能集成测试
 * 验证用户登录后JWT令牌中包含准确的角色和权限信息
 */
class LoginIntegrationTest extends BaseJpaIntegrationTest {

    @Autowired
    private AuthApplicationService authApplicationService;

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private UserAuthenticationSpecification authenticationSpecification;

    @Autowired
    private UserRoleAssignmentService userRoleAssignmentService;

    @MockitoBean
    private RedisSessionRepository redisSessionRepository;

    @MockitoBean
    private TokenCacheRepository tokenCacheRepository;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @MockitoBean
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private SecurityPolicyPort securityPolicyPort;

    @Autowired
    private UserRegistrationSpecification userRegistrationSpecification;

    @Autowired
    private UserAuthenticationSpecification userAuthenticationSpecification;

    @MockitoBean
    private ValueOperations<String, String> stringSetOperations;
    @MockitoBean
    private SetOperations<String, String> setOperations;

    @BeforeEach
    void setUp() {
        Mockito.when(stringRedisTemplate.opsForValue()).thenReturn(stringSetOperations);
        Mockito.when(stringRedisTemplate.opsForSet()).thenReturn(setOperations);
    }

    @Test
    @DisplayName("用户登录成功 - JWT令牌包含正确的角色和权限信息")
    void loginSuccess_JwtTokenContainsCorrectRoleAndPermissionInfo() {

        // 1. 创建测试用户
        UserRegistrationCommand registrationCommand = UserRegistrationCommand.create(
                "test_user",
                "Password123",
                "test@example.com",
                "13800138000",
                "Test User"
        );
        User user = userDomainService.register(registrationCommand);

        // 激活用户
        authApplicationService.activateUser(user.getUsername().value(), user.getActivationCode());

        // 2. 创建角色和权限
        Role role = authApplicationService.createRole(CreateRoleCommand.create("admin", "管理员", "系统管理员角色"));

        Permission readPermission = authApplicationService.createPermission(CreatePermissionCommand.builder()
                .code("user:read")
                .name("用户读取")
                .type(PermissionType.OPERATION)
                .resource("/api/users")
                .action("GET")
                .build());

        Permission writePermission = authApplicationService.createPermission(CreatePermissionCommand.builder()
                .code("user:write")
                .name("用户写入")
                .type(PermissionType.OPERATION)
                .resource("/api/users")
                .action("POST")
                .build());

        // 4. 为角色分配权限
        userRoleAssignmentService.assignRole(AssignRoleCommand.create(user.getId().id(), role.getId().id()));

        RoleAssignPermissionsCommand permissionsCommand = RoleAssignPermissionsCommand.builder()
                .roleId(role.getId())
                .permissionIds(CollUtil.toList(readPermission.getId(), writePermission.getId()))
                .build();

        authApplicationService.assignPermissionsToRole(permissionsCommand);

        // 5. 用户登录
        AuthenticationByUsernamePasswordCommand loginCommand = AuthenticationByUsernamePasswordCommand.create(
                "test_user",
                "Password123",
                "127.0.0.1",
                "JUnit Test"
        );

        AuthenticationResultDTO result = authApplicationService.login(loginCommand);

        // 6. 验证登录结果
        assertTrue(result.success());
        assertNotNull(result.tokenInfo());
        assertNotNull(result.tokenInfo().getTokenValue());

        String accessToken = result.tokenInfo().getTokenValue();

        // 7. 解析JWT令牌并验证角色权限信息
        Claims claims = jwtTokenService.parseToken(accessToken);

        // 验证基本信息
        assertEquals("test_user", claims.getSubject());
        assertEquals(user.getId().id(), claims.get("userId", Long.class));

        // 验证角色信息
        List<String> roles = claims.get("roles", List.class);
        assertNotNull(roles);
        assertTrue(roles.contains("admin"));

        // 验证权限信息
        List<String> permissions = claims.get("permissions", List.class);
        assertNotNull(permissions);
        assertTrue(permissions.contains("user:read"));
        assertTrue(permissions.contains("user:write"));

        // 8. 验证令牌类型
        assertEquals("access", claims.get("type", String.class));
    }

    @Test
    @DisplayName("用户登录失败 - 密码错误")
    void loginFailure_WrongPassword() {
        // 1. 创建测试用户
        UserRegistrationCommand registrationCommand = UserRegistrationCommand.create(
                "test_user",
                "Password123",
                "test@example.com",
                "13800138000",
                "Test User"
        );

        User register = userDomainService.register(registrationCommand);

        // 2. 使用错误密码登录
        AuthenticationByUsernamePasswordCommand loginCommand = AuthenticationByUsernamePasswordCommand.create(
                "test_user",
                "Wrongpassword",
                "127.0.0.1",
                "JUnit Test"
        );

        // 3. 验证登录失败
        assertThrows(AuthenticationException.class, () -> {
            authApplicationService.login(loginCommand);
        });
    }

    @Test
    @DisplayName("用户登录失败 - 用户不存在")
    void loginFailure_UserNotFound() {
        // 1. 尝试登录不存在的用户
        AuthenticationByUsernamePasswordCommand loginCommand = AuthenticationByUsernamePasswordCommand.create(
                "nonexistent",
                "password123",
                "127.0.0.1",
                "JUnit Test"
        );

        // 2. 验证登录失败
        assertThrows(AuthenticationException.class, () -> {
            authApplicationService.login(loginCommand);
        });
    }
}
