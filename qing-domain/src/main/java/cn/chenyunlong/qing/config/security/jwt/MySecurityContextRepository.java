package cn.chenyunlong.qing.config.security.jwt;

import cn.chenyunlong.qing.config.security.utils.JwtTokenUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

@RequiredArgsConstructor
public class MySecurityContextRepository implements SecurityContextRepository {

    @Resource
    private final JwtTokenUtil jwtTokenUtil;
    @Resource
    private final UserDetailsService userService;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        // 从请求头中读取token
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(token) || !StrUtil.startWith(token, "Bearer ")) {
            return securityContext;
        }
        token = token.replaceFirst("Bearer ", "");
        // 拿到 用户名
        String username = jwtTokenUtil.getUserNameFromToken(token);
        if (StrUtil.isBlank(username)) {
            return securityContext;
        }
        // 查询用户
        UserDetails user = userService.loadUserByUsername(username);
        if (user == null) {
            return securityContext;
        }
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(user.getUsername(), user.getPassword(), user.getAuthorities());
        authenticationToken.setDetails(user);
        securityContext.setAuthentication(authenticationToken);
        // 返回
        return securityContext;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // 不用保存, 只要前端持续发送token就好
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtTokenUtil.isTokenExpired(token)) {
            return false;
        }
        return StrUtil.isNotBlank(token);
    }
}
