package chenyunlong.zhangli.anthentication;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class MyAuthTokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;  // 我们之前自定义的 token功能类
    private UserDetailsService detailsService;// 也是我实现的UserDetailsService
    private AccessDeniedHandler myAccessDeniedHandler;//当用户不具有权限的时候

    public MyAuthTokenConfigurer(UserDetailsService detailsService, TokenProvider tokenProvider, AccessDeniedHandler myAccessDeniedHandler) {
        this.detailsService = detailsService;
        this.tokenProvider = tokenProvider;
        this.myAccessDeniedHandler = myAccessDeniedHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        MyTokenFilter customFilter = new MyTokenFilter(detailsService, tokenProvider);

        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}