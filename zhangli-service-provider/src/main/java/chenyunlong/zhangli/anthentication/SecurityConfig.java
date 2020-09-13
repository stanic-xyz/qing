package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import chenyunlong.zhangli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author Stan
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final ZhangliProperties zhangliProperties;
    private final RedisTemplate redisTemplate;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final SecurityProblemSupport problemSupport;


    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler,
                          AuthenticationFailureHandler authenticationFeilureHandler,
                          RedisTemplate redisTemplate,
                          ZhangliProperties zhangliProperties,
                          MyAccessDeniedHandler myAccessDeniedHandler,
                          UserService userService,
                          TokenProvider tokenProvider,
                          UserDetailsService userDetailsService,
                          SecurityProblemSupport problemSupport) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFeilureHandler;
        this.redisTemplate = redisTemplate;
        this.zhangliProperties = zhangliProperties;
        this.myAccessDeniedHandler = myAccessDeniedHandler;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.problemSupport = problemSupport;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        禁用CSRF 开启跨域
        http.csrf().disable();
        http.cors();

//        开起无状态
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .csrf()
                .disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(problemSupport)
//                .accessDeniedHandler(problemSupport)
//                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .authorizeRequests()
                .antMatchers("/authrize/**", "/actuator/**", "/file/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs").permitAll()
                .antMatchers("/easyui/**", "detail/**").permitAll()
                .antMatchers("/js/**", "/css/**", "/img/*").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/natcross/**").permitAll()
                .antMatchers("/management/health").permitAll()
                .antMatchers("/management/info").permitAll()
                .antMatchers("/management/**").permitAll()
                .anyRequest().permitAll();

//        http.apply(securityConfigurerAdapter());
    }

    public MyAuthTokenConfigurer securityConfigurerAdapter() {
        return new MyAuthTokenConfigurer(new MyUserdeatailService(userService), tokenProvider);
    }
}
