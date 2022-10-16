package cn.chenyunlong.zhangli.security.config;

import cn.chenyunlong.zhangli.config.properties.ZhangliProperties;
import cn.chenyunlong.zhangli.security.MyAccessDeniedHandler;
import cn.chenyunlong.zhangli.security.MyAuthenticationEntryPoint;
import cn.chenyunlong.zhangli.security.filter.MyTokenFilter;
import cn.chenyunlong.zhangli.security.support.TokenProvider;
import cn.chenyunlong.zhangli.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Stan
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final ZhangliProperties zhangliProperties;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;


    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler,
                          AuthenticationFailureHandler authenticationFeilureHandler,
                          ZhangliProperties zhangliProperties,
                          MyAccessDeniedHandler myAccessDeniedHandler,
                          UserService userService,
                          TokenProvider tokenProvider,
                          UserDetailsService myUserDetailService,
                          MyAuthenticationEntryPoint myAuthenticationEntryPoint) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFeilureHandler;
        this.zhangliProperties = zhangliProperties;
        this.myAccessDeniedHandler = myAccessDeniedHandler;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = myUserDetailService;
        this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //禁用CSRF 开启跨域
        http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**", "/authorize/**", "/file/**", "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**").permitAll()
                .antMatchers("/easyui/**", "detail/**").permitAll()
                .antMatchers("/js/**", "/css/**", "/img/*").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/natcross/**").permitAll()
                .antMatchers("/management/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new MyTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
