package chenyunlong.zhangli.entities;

import chenyunlong.zhangli.entities.common.BaseEntitiy;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author zhuzhe
 * @date 2018/6/3 23:27
 * @email 1529949535@qq.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity  // 该注解声明一个实体类，与数据库中的表对应
public class UserInfo extends BaseEntitiy {

    @Id   // 表明id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //  自动生成
    private Long id;
    private String name;
    private byte gender;
    private String ImgName;
    private String ImgUrl;
    @OneToOne
    private User user;
}