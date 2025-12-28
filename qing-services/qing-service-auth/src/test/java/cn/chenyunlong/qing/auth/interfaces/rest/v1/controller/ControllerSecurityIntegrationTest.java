package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.qing.auth.AuthWebApplication;
import cn.chenyunlong.qing.auth.application.service.AuthApplicationService;
import cn.chenyunlong.qing.auth.application.service.UserRoleAssignmentService;
import cn.chenyunlong.qing.auth.domain.authentication.service.UserDomainService;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.PermissionJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RoleJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.UserJpaRepository;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.AssignRoleRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role.CreateRoleRequest;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(
        classes = {AuthWebApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.mail.username=test@example.com"
})
@AutoConfigureMockMvc
class ControllerSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoleJpaRepository roleJpaRepository;

    @MockitoBean
    private UserJpaRepository userJpaRepository;

    @MockitoBean
    private PermissionJpaRepository permissionJpaRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private UserDomainService userDomainService;

    @MockitoBean
    private AuthApplicationService authApplicationService;

    @MockitoBean
    private UserRoleAssignmentService userRoleAssignmentService;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @MockitoBean
    private RedisTemplate<String, Object> redisTemplate;

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @MockitoBean
    private RedisKeyValueAdapter redisKeyValueAdapter;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @Test
    @DisplayName("创建角色 - 无权限 - 返回403")
    @WithMockUser(username = "user", authorities = {"user:read"})
    void createRole_NoAuthority_Forbidden() throws Exception {
        CreateRoleRequest request = new CreateRoleRequest();
        request.setCode("ADMIN");
        request.setName("Administrator");
        request.setDescription("Admin role");

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("创建角色 - 有权限 - 返回200")
    @WithMockUser(username = "admin", authorities = {"role:create"})
    void createRole_HasAuthority_Success() throws Exception {
        CreateRoleRequest request = new CreateRoleRequest();
        request.setCode("ADMIN");
        request.setName("Administrator");
        request.setDescription("Admin role");

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("激活用户 - 无权限 - 返回403")
    @WithMockUser(username = "user", authorities = {"user:read"})
    void activateUser_NoAuthority_Forbidden() throws Exception {
        mockMvc.perform(patch("/api/v1/users/1/activate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("激活用户 - 有权限 - 返回200")
    @WithMockUser(username = "admin", authorities = {"user:active"})
    void activateUser_HasAuthority_Success() throws Exception {
        mockMvc.perform(patch("/api/v1/users/1/activate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("为用户分配角色 - 无权限 - 返回403")
    @WithMockUser(username = "user", authorities = {"user:read"})
    void assignRoleToUser_NoAuthority_Forbidden() throws Exception {
        AssignRoleRequest request = new AssignRoleRequest();
        request.setUserId(1L);
        request.setRoleId(2L);

        mockMvc.perform(post("/api/v1/users/assignRoleToUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("为用户分配角色 - 有权限 - 返回200")
    @WithMockUser(username = "admin", authorities = {"user:assign-role"})
    void assignRoleToUser_HasAuthority_Success() throws Exception {
        AssignRoleRequest request = new AssignRoleRequest();
        request.setUserId(1L);
        request.setRoleId(2L);

        mockMvc.perform(post("/api/v1/users/assignRoleToUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("获取角色 - 无权限 - 返回403")
    @WithMockUser(username = "user", authorities = {"user:read"})
    void getRoleById_NoAuthority_Forbidden() throws Exception {
        mockMvc.perform(get("/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("获取角色 - 有权限 - 返回200")
    @WithMockUser(username = "admin", authorities = {"role:read"})
    void getRoleById_HasAuthority_Success() throws Exception {
        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setCode("ADMIN");
        role.setName("Admin");
        when(roleJpaRepository.findById(1L)).thenReturn(Optional.of(role));

        mockMvc.perform(get("/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("获取用户 - 无权限 - 返回403")
    @WithMockUser(username = "user", authorities = {"role:read"})
    void getUserById_NoAuthority_Forbidden() throws Exception {
        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("获取用户 - 有权限 - 返回200")
    @WithMockUser(username = "admin", authorities = {"user:read"})
    void getUserById_HasAuthority_Success() throws Exception {
        User user = mock(User.class);
        when(userDomainService.loadUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
