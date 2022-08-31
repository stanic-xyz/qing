package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Stan
 */
@TableName(value = "user_third")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserThird extends BaseEntity<UserThird> {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账号ID
     */
    private Long userId;

    /**
     * 用户登录的账号，长度为十位字符
     */
    private String uid;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 用户界面上展示的昵称
     */
    private String accessToken;

    /**
     * 用户的头像地址
     */
    private Integer accessExpire;
}