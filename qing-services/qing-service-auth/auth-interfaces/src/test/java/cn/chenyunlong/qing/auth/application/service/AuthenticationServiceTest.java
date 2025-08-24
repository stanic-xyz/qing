package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.application.dto.AuthenticationResult;
import cn.chenyunlong.qing.auth.application.dto.LoginRequest;
import cn.chenyunlong.qing.auth.application.utils.AuthJwtTokenUtil;
import cn.chenyunlong.qing.auth.domain.authentication.Authentication;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.service.AuthenticationDomainService;
import cn.chenyunlong.qing.auth.domain.authentication.service.TokenDomainService;
import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.QingUserId;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationDomainService authenticationDomainService;

    @Mock
    private TokenDomainService tokenDomainService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthJwtTokenUtil authJwtTokenUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    private LoginRequest loginRequest;
    private QingUser mockUser;
    private Authentication mockAuthentication;
    private AuthenticationToken mockToken;
    private String testIp;
    private String testUserAgent;

    @BeforeEach
    void setUp() {
        // 准备登录请求
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        // 准备测试IP和用户代理
        testIp = "127.0.0.1";
        testUserAgent = "Mozilla/5.0 Test";

        // 准备模拟用户
        mockUser = mock(QingUser.class);
        QingUserId userId = QingUserId.of(1L);
        lenient().when(mockUser.getId()).thenReturn(userId);

        // 准备模拟认证
        mockAuthentication = mock(Authentication.class);

        // 准备模拟令牌
        mockToken = mock(AuthenticationToken.class);
        lenient().when(mockToken.getTokenValue()).thenReturn("test-token");
    }

    @Test
    @DisplayName("登录成功测试")
    void loginSuccess() {
        // 模拟认证成功
        lenient().when(mockAuthentication.isSuccessful()).thenReturn(true);
        QingUserId authUserId = QingUserId.of(1L);
        lenient().when(mockAuthentication.getUserId()).thenReturn(authUserId);

        // 模拟领域服务认证成功
        lenient().when(authenticationDomainService.authenticateByUsernamePassword(
                        "testuser", "password", testIp, testUserAgent))
                .thenReturn(mockAuthentication);

        // 模拟用户存在
        lenient().when(userRepository.findById(any(QingUserId.class))).thenReturn(Optional.of(mockUser));

        // 模拟JWT生成
        lenient().when(authJwtTokenUtil.generateToken(any())).thenReturn("test-token");
        lenient().when(authJwtTokenUtil.getExpiration()).thenReturn(3600000L);

        // 模拟令牌创建
        lenient().when(tokenDomainService.createJwtToken(any(QingUserId.class), anyString(), any(LocalDateTime.class)))
                .thenReturn(mockToken);

        // 执行登录
        AuthenticationResult result = authenticationService.login(loginRequest, testIp, testUserAgent);

        // 验证结果
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getUser()).isEqualTo(mockUser);
        assertThat(result.getTokenInfo().getToken()).isEqualTo("test-token");
        assertThat(result.getTokenInfo().getTokenType()).isEqualTo("Bearer");
        assertThat(result.getTokenInfo().getExpiresIn()).isEqualTo(3600);

        // 验证调用了正确的方法
        verify(authenticationDomainService).authenticateByUsernamePassword(
                "testuser", "password", testIp, testUserAgent);
        verify(userRepository).findById(any(QingUserId.class));
        verify(authJwtTokenUtil).generateToken(new QingUser());
        verify(tokenDomainService).createJwtToken(any(QingUserId.class), eq("test-token"), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("登录失败测试 - 认证失败")
    void loginFailureAuthentication() {
        // 模拟认证失败
        lenient().when(mockAuthentication.isSuccessful()).thenReturn(false);
        lenient().when(mockAuthentication.getFailureReason()).thenReturn("用户名或密码错误");
        lenient().when(authenticationDomainService.authenticateByUsernamePassword(
                        "testuser", "password", testIp, testUserAgent))
                .thenReturn(mockAuthentication);

        // 执行登录
        AuthenticationResult result = authenticationService.login(loginRequest, testIp, testUserAgent);

        // 验证结果
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getFailureReason()).isEqualTo("用户名或密码错误");
        assertThat(result.getUser()).isNull();
        assertThat(result.getTokenInfo()).isNull();

        // 验证没有调用后续方法
        verify(userRepository, never()).findById(any(QingUserId.class));
        verify(authJwtTokenUtil, never()).generateToken(any(QingUser.class));
        verify(tokenDomainService, never()).createJwtToken(any(QingUserId.class), anyString(), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("验证令牌成功测试")
    void validateTokenSuccess() {
        // 模拟JWT验证成功
        lenient().when(authJwtTokenUtil.getExpiration()).thenReturn(3600000L);
        lenient().when(authJwtTokenUtil.validateToken("test-token")).thenReturn(true);
        lenient().when(authJwtTokenUtil.getUserIdFromToken("test-token")).thenReturn(1L);

        // 模拟领域服务认证成功
        lenient().when(mockAuthentication.isSuccessful()).thenReturn(true);
        QingUserId authUserId = QingUserId.of(1L);
        lenient().when(mockAuthentication.getUserId()).thenReturn(authUserId);
        lenient().when(authenticationDomainService.authenticateByJwtToken(1L, testIp, testUserAgent))
                .thenReturn(mockAuthentication);

        // 模拟用户存在
        when(userRepository.findById(any(QingUserId.class))).thenReturn(Optional.of(mockUser));

        // 执行验证
        AuthenticationResult result = authenticationService.validateToken("test-token", testIp, testUserAgent);

        // 验证结果
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getUser()).isEqualTo(mockUser);
        assertThat(result.getTokenInfo().getToken()).isEqualTo("test-token");
        assertThat(result.getTokenInfo().getTokenType()).isEqualTo("Bearer");
        assertThat(result.getTokenInfo().getExpiresIn()).isEqualTo(3600);

        // 验证调用了正确的方法
        verify(authJwtTokenUtil).validateToken("test-token");
        verify(authJwtTokenUtil).getUserIdFromToken("test-token");
        verify(authenticationDomainService).authenticateByJwtToken(1L, testIp, testUserAgent);
        verify(userRepository).findById(any(QingUserId.class));
    }

    @Test
    @DisplayName("验证令牌失败测试 - JWT无效")
    void validateTokenFailureInvalidJwt() {
        // 模拟JWT验证失败
        lenient().when(authJwtTokenUtil.validateToken("test-token")).thenReturn(false);

        // 执行验证
        AuthenticationResult result = authenticationService.validateToken("test-token", testIp, testUserAgent);

        // 验证结果
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getFailureReason()).isEqualTo("无效的令牌");
        assertThat(result.getUser()).isNull();
        assertThat(result.getTokenInfo()).isNull();

        // 验证没有调用后续方法
        verify(authJwtTokenUtil, never()).getUserIdFromToken(anyString());
        verify(authenticationDomainService, never()).authenticateByJwtToken(anyLong(), anyString(), anyString());
        verify(userRepository, never()).findById(any(QingUserId.class));
    }

    @Test
    @DisplayName("注销测试")
    void logout() {
        // 执行注销
        authenticationService.logout("test-token");

        // 验证调用了撤销令牌方法
        verify(tokenDomainService).revokeToken("test-token", "用户注销");
    }
}
