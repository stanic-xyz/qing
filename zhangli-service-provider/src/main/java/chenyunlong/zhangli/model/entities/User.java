package chenyunlong.zhangli.model.entities;


import com.baomidou.mybatisplus.annotation.TableId;
import com.stan.zhangli.core.core.domain.BaseEntity;
import com.stan.zhangli.core.enums.MFAType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @TableId
    private Long userId;
    private String username;
    private String nickname;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private String description;
    private LocalDateTime expireTime;
    private MFAType mfaType;
    private String mfaKey;
}
