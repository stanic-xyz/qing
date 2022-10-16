package cn.chenyunlong.zhangli.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: 陈云龙
 * @date: 2020/6/29
 * @description 判断是否有权限的接口
 */
public interface RbacService {
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
