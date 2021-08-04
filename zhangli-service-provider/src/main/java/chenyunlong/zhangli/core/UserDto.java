package chenyunlong.zhangli.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 登录用户信息
 *
 * @author Stan
 * @date 2021/01/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String username;
    private String password;
    private Integer status;
    private String clientId;
    private List<String> roles;

}
