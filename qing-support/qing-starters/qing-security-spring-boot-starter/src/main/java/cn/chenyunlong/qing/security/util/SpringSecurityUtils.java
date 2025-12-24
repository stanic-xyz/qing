package cn.chenyunlong.qing.security.util;

import cn.chenyunlong.qing.security.base.BaseJwtUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SpringSecurityUtils {

    public static String getCurrentUsername() {
        String temp = "";
        try {
            BaseJwtUser jwtUser = getJwtUser();
            if (Objects.nonNull(jwtUser)) {
                return jwtUser.getUsername();
            } else {
                temp = "-";
            }
        } catch (Exception e) {
            temp = "";
        }
        return temp;
    }

    public static BaseJwtUser getJwtUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass().isAssignableFrom(AnonymousAuthenticationToken.class)) {
            return null;
        }
        return (BaseJwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
