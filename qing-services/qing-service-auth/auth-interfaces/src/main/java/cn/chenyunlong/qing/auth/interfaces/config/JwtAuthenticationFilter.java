//package cn.chenyunlong.qing.auth.interfaces.config;
//
//import cn.chenyunlong.qing.auth.application.service.UserService;
//import cn.chenyunlong.qing.auth.domain.user.QingUser;
//import cn.chenyunlong.qing.auth.application.utils.AuthJwtTokenUtil;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Optional;
//
/// **
// * JWT认证过滤器
// * 用于拦截请求并验证JWT令牌
// */
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final AuthJwtTokenUtil authJwtTokenUtil;
//    private final UserService userService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//        throws ServletException, IOException {
//
//        final String requestTokenHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwtToken = null;
//
//        // 从Authorization头中获取JWT令牌
//        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//            jwtToken = requestTokenHeader.substring(7);
//            try {
//                // 从JWT令牌中获取用户ID
//                Long userId = authJwtTokenUtil.getUserIdFromToken(jwtToken);
//
//                // 根据用户ID获取用户信息
//                Optional<QingUser> userOptional = userService.loadUserById(userId);
//
//                if (userOptional.isPresent()) {
//                    QingUser user = userOptional.get();
//                    username = user.getUsername().value();
//
//                    // 验证JWT令牌
//                    if (authJwtTokenUtil.validateToken(jwtToken, user)) {
//                        // 创建认证令牌
//                        UsernamePasswordAuthenticationToken authenticationToken =
//                            new UsernamePasswordAuthenticationToken(
//                                user,
//                                null,
//                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
//
//                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                        // 设置安全上下文
//                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                    }
//                }
//            } catch (Exception e) {
//                log.error("无法设置用户认证: {}", e.getMessage());
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
