package cn.chenyunlong.zhangli.security.support;

import cn.chenyunlong.zhangli.model.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetail implements UserDetails {

    private transient User currentUser;
    private transient Collection<GrantedAuthority> permissionList;

    public UserDetail(User user, Collection<GrantedAuthority> authorities) {
        if (user != null) {
            currentUser = user;
            this.permissionList = authorities;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissionList;
    }

    @Override
    public String getPassword() {
        return currentUser.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUser.getPassword();
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
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}