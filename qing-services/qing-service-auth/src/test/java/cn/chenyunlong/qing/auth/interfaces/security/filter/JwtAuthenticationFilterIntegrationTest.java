package cn.chenyunlong.qing.auth.interfaces.security.filter;

import cn.chenyunlong.qing.auth.application.service.JwtTokenService;
import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenCacheRepository;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import cn.chenyunlong.qing.auth.infrastructure.email.EmailServiceAdapter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class JwtAuthenticationFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private TokenCacheRepository tokenCacheRepository;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @MockitoBean
    private RedisTemplate<String, Object> redisTemplate;

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @MockitoBean
    private RedisKeyValueAdapter redisKeyValueAdapter;

    @MockitoBean
    private EmailServiceAdapter emailServiceAdapter;

    @Test
    @DisplayName("无Token访问受保护接口 - 返回401")
    void accessProtectedResource_NoToken_Unauthorized() throws Exception {
        // 假设 /api/v1/users 是受保护的
        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("有效Token访问受保护接口 - 返回200/403")
    // 注意：这里我们测试Filter是否成功解析Token并设置Context。
    // 如果接口本身还需要特定权限（如@PreAuthorize），可能会返回403。
    // 我们只要确保不是401（未认证）即可证明Filter工作了。
    void accessProtectedResource_ValidToken_Authenticated() throws Exception {
        // 构造用户
        User user = new User();
        user.setId(UserId.of(1L));
        user.setUsername(new Username("testuser"));

        // 生成Token
        String token = jwtTokenService.generateAccessToken(user, "device-1", List.of("USER"), List.of("user:read"));
        System.out.println("Generated Token: " + token);

        // Mock TokenCacheRepository (isBlacklisted return false)
        when(tokenCacheRepository.isBlacklisted(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/v1/users/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                // 如果用户有权限，应该是200；如果无权限但已认证，是403。
                // 这里的重点是 Filter 能够解析 Token。
                // 由于 ControllerSecurityIntegrationTest 中 /api/v1/users/1 需要 user:read 权限，我们给了这个权限。
                // 但是 Controller 可能会查数据库，如果数据库没数据，可能返回 404 或其他。
                // 为了避免 Controller 逻辑干扰，我们主要看 status 不是 401。
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status == 401) {
                        throw new AssertionError("Should be authenticated but got 401");
                    }
                });
    }

    @Test
    @DisplayName("黑名单Token访问受保护接口 - 返回401")
    void accessProtectedResource_BlacklistedToken_Unauthorized() throws Exception {
        User user = new User();
        user.setId(UserId.of(1L));
        user.setUsername(new Username("testuser"));
        String token = jwtTokenService.generateAccessToken(user);
        when(tokenCacheRepository.isBlacklisted(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/v1/users/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
