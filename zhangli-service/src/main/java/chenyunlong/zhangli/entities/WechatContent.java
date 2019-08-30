package chenyunlong.zhangli.entities;

import chenyunlong.zhangli.entities.common.BaseEntitiy;
import chenyunlong.zhangli.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class WechatContent extends BaseEntitiy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String username;
    private String headImgUrl;
    private String content;
    private MessageType messageType;
    @OneToMany
    private List<WechatImages> wechatImages;
}
