package cn.chenyunlong.qing.domain.auth.user.domainservice;

import cn.chenyunlong.qing.domain.auth.user.User;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.service.IUserService;
import cn.chenyunlong.security.entity.AuthUser;
import cn.chenyunlong.security.exception.RegisterUserFailureException;
import cn.chenyunlong.security.service.UmsUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class UmsUserDetailsServiceImpl implements UmsUserDetailsService {
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public UserDetails registerUser(AuthUser authUser, String username, String defaultAuthority, String decodeState) throws RegisterUserFailureException {
        UserCreator creator = new UserCreator();
        creator.setUsername(username);
        creator.setPassword("test");
        creator.setEmail(authUser.getEmail());
        creator.setDescription(authUser.getRemark());
        Long user = userService.createUser(creator);
        String encodedPassword = passwordEncoder.encode("12312");
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(encodedPassword)
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .authorities(Collections.emptyList())
                .build();
    }
}
