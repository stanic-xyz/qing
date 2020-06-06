package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.properties.ZhangliProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private ZhangliProperties zhangliProperties;
    @Autowired
    private RedisTemplate redisTemplate;

    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFeilureHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFeilureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/authrize/**", "/actuator/**", "/file/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs").permitAll()
                .antMatchers("/login", "/css/**", "/image/*").permitAll()
                .antMatchers("/static/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .permitAll();

//        http.addFilterBefore(new AuthInterceptor(zhangliProperties, redisTemplate), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 密码生成策略
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
