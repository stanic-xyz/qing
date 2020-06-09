package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.service.UserService;
import com.google.inject.internal.cglib.proxy.$UndeclaredThrowableException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
            throw new UsernameNotFoundException("用户名未找到！");
        return new SecurityUser(user);
    }
}
