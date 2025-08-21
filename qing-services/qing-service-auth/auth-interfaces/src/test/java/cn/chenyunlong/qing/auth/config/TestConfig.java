package cn.chenyunlong.qing.auth.config;

import cn.chenyunlong.qing.auth.application.utils.AuthJwtTokenUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 测试配置类
 * 提供测试环境所需的Bean配置
 */
@TestConfiguration
public class TestConfig {

    /**
     * 提供密码编码器
     */
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 提供JWT工具类
     */
    @Bean
    @Primary
    public AuthJwtTokenUtil authJwtTokenUtil() {
        AuthJwtTokenUtil tokenUtil = new AuthJwtTokenUtil();
        // 设置测试环境的JWT配置
        tokenUtil.setSecret("test-secret-key-for-jwt-token-authentication-in-unit-tests");
        tokenUtil.setExpiration(3600000L); // 1小时
        return tokenUtil;
    }
}