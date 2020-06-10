package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.service.UserService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final ZhangliProperties zhangliProperties;
    private final RedisTemplate redisTemplate;
    private TokenProvider tokenProvider;
    @Autowired
    private UserService userService;

    private UserDetailsService userDetailsService;

    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFeilureHandler, RedisTemplate redisTemplate, ZhangliProperties zhangliProperties, MyAccessDeniedHandler myAccessDeniedHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFeilureHandler;
        this.redisTemplate = redisTemplate;
        this.zhangliProperties = zhangliProperties;
        this.tokenProvider = new TokenProvider(zhangliProperties);
        this.myAccessDeniedHandler = myAccessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        禁用CSRF 开启跨域
        http.csrf().disable();
        http.cors();

//        开起无状态
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/authrize/**", "/actuator/**", "/file/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs").permitAll()
                .antMatchers("/login", "/css/**", "/image/*").permitAll()
                .antMatchers("/static/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated();
//                .and()
//                .formLogin();
//                .successHandler(authenticationSuccessHandler)
//                .failureHandler(authenticationFailureHandler)

        http.apply(securityConfigurerAdapter());
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


    public MyAuthTokenConfigurer securityConfigurerAdapter() {
        return new MyAuthTokenConfigurer(new MyUserdeatailService(userService), tokenProvider, myAccessDeniedHandler);
    }
}
