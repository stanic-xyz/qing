package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.service.RbacService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 判断用户是否有权限的接口的实现
 *
 * @author : 陈云龙
 * @date : 2020/6/29
 */
@Component("rbacService")
public class RbacServiceImpl implements RbacService {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        boolean hasPermission = false;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                if (antPathMatcher.match(grantedAuthority.getAuthority(), request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }
}
