package cn.chenyunlong.zhangli.security.support;

import cn.chenyunlong.zhangli.model.entities.User;
import cn.chenyunlong.zhangli.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class MyUserDetailService implements UserDetailsService {

    private final UserService userService;


    public MyUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名未找到！");
        }


        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("admin");
        grantedAuthorities.add(authority);

        return new UserDetail(user, grantedAuthorities);
    }
}
