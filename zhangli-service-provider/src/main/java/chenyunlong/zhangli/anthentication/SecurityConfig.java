package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.service.UserService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final ZhangliProperties zhangliProperties;
    private final RedisTemplate redisTemplate;
    private TokenProvider tokenProvider;
    private final UserService userService;
    @Autowired
    private MyUserdeatailService myUserdeatailService;
    @Autowired
    private CorsFilter corsFilter;
    @Autowired
    private SecurityProblemSupport problemSupport;


    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFeilureHandler, RedisTemplate redisTemplate, ZhangliProperties zhangliProperties, MyAccessDeniedHandler myAccessDeniedHandler, UserService userService) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFeilureHandler;
        this.redisTemplate = redisTemplate;
        this.zhangliProperties = zhangliProperties;
        this.tokenProvider = new TokenProvider(zhangliProperties);
        this.myAccessDeniedHandler = myAccessDeniedHandler;
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserdeatailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        禁用CSRF 开启跨域
        http.csrf().disable();
        http.cors();

//        开起无状态
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .addFilter(new MyAuthenticationProcessingFilter(zhangliProperties))
                .csrf()
                .disable()
                .addFilterBefore(corsFilter, MyAuthenticationProcessingFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/authrize/**", "/actuator/**", "/file/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs").permitAll()
                .antMatchers("/login", "/css/**", "/image/*").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .anyRequest().authenticated();

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
