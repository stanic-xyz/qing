package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.entities.Permission;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class MyUserdeatailService implements UserDetailsService {

    private final UserService userService;

    public MyUserdeatailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(s);

        if (user == null)
            throw new UsernameNotFoundException("没找到该用户");
        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                //TODO 这里还需要进行权限验证
                List<Permission> permissionList = userService.getPermissionByUsername(user.getUsername());

                return AuthorityUtils.NO_AUTHORITIES;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getPassword();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
        return userDetails;
    }
}
