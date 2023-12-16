package cn.chenyunlong.qing.domain.auth.user.domainservice;

import cn.chenyunlong.qing.domain.auth.user.User;
import cn.chenyunlong.qing.domain.auth.user.service.IUserService;
import cn.chenyunlong.security.service.UmsUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
public class UmsUserDetailsServiceImpl implements UmsUserDetailsService {
    private IUserService userService;

    @Override
    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public List<Boolean> existedByUsernames(String... usernames) {
        return Arrays.stream(usernames).map(username -> false).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("用户名未找到!");
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), emptyList());
    }
}
