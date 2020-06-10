package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.entities.Permission;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.service.UserService;
import com.google.inject.internal.cglib.proxy.$UndeclaredThrowableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

public class MyUserdeatailService implements UserDetailsService {

    private final UserService userService;


    public MyUserdeatailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("用户名未找到！");

//        List<Permission> permissionList = userService.getPermissionByUsername(user.getUsername());

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

//        for (Permission permission : permissionList) {
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
//            grantedAuthorities.add(grantedAuthority);
//        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("admin");
        grantedAuthorities.add(authority);

        return new SecurityUser(user, grantedAuthorities);
    }
}
