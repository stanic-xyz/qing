package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserCreator userCreator;
    private QingUser mockUser;

    @BeforeEach
    void setUp() {
        // 准备用户创建请求
        userCreator = new UserCreator();
        userCreator.setUsername("testuser");
        userCreator.setPassword("password");
        userCreator.setEmail("test@example.com");

        // 准备模拟用户
        mockUser = mock(QingUser.class);
    }

    @Test
    @DisplayName("注册新用户成功")
    void registerSuccess() {
        // 模拟用户名和邮箱不存在
        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        
        // 模拟创建用户成功
        when(userRepository.save(any(QingUser.class))).thenReturn(mockUser);

        // 执行注册
        Optional<QingUser> result = userService.register(userCreator);

        // 验证结果
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(mockUser);

        // 验证调用了保存方法
        verify(userRepository).save(any(QingUser.class));
    }

    @Test
    @DisplayName("注册失败 - 用户名已存在")
    void registerFailureUsernameExists() {
        // 模拟用户名已存在
        when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        // 执行注册并验证异常
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(userCreator);
        });

        // 验证异常消息
        assertThat(exception.getMessage()).contains("用户名已存在");

        // 验证没有调用保存方法
        verify(userRepository, never()).save(any(QingUser.class));
    }

    @Test
    @DisplayName("注册失败 - 邮箱已存在")
    void registerFailureEmailExists() {
        // 模拟用户名不存在但邮箱已存在
        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        // 执行注册并验证异常
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(userCreator);
        });

        // 验证异常消息
        assertThat(exception.getMessage()).contains("邮箱已存在");

        // 验证没有调用保存方法
        verify(userRepository, never()).save(any(QingUser.class));
    }

    @Test
    @DisplayName("根据用户名查找用户")
    void findByUsername() {
        // 模拟根据用户名查找
        when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        // 执行查找
        Optional<QingUser> result = userService.findByUsername("testuser");

        // 验证结果
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(mockUser);
    }

    @Test
    @DisplayName("根据邮箱查找用户")
    void findByEmail() {
        // 模拟根据邮箱查找
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        // 执行查找
        Optional<QingUser> result = userService.findByEmail("test@example.com");

        // 验证结果
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(mockUser);
    }

    @Test
    @DisplayName("根据ID加载用户")
    void loadUserById() {
        // 模拟根据ID查找
        when(userRepository.findById(any(AggregateId.class))).thenReturn(Optional.of(mockUser));

        // 执行查找
        Optional<QingUser> result = userService.loadUserById(1L);

        // 验证结果
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(mockUser);

        // 验证传入了正确的ID
        ArgumentCaptor<AggregateId> idCaptor = ArgumentCaptor.forClass(AggregateId.class);
        verify(userRepository).findById(idCaptor.capture());
        assertThat(idCaptor.getValue().getId()).isEqualTo(1L);
    }
}
