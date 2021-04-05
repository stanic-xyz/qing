package chenyunlong.zhangli.model.entities;


import chenyunlong.zhangli.model.enums.MFAType;
import chenyunlong.zhangli.utils.DateUtils;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 用户信息
 *
 * @author Stan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    private Long userId;
    private String username;
    private String nickname;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private String description;
    private Date expireTime;
    private MFAType mfaType;
    private String mfaKey;

    @Override
    public void prePersist() {
        super.prePersist();
        if (email == null) {
            email = "";
        }
        if (avatar == null) {
            avatar = "";
        }
        if (description == null) {
            description = "";
        }
        if (expireTime == null) {
            expireTime = DateUtils.now();
        }
    }
}
