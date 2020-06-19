package chenyunlong.zhangli.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

/**
* @author xuzhipeng
* @date 2018-11-09 16:52:43
* @since 1.0
*/
public interface RbacService {

     boolean hasPermission(HttpServletRequest request,Authentication authentication);

}