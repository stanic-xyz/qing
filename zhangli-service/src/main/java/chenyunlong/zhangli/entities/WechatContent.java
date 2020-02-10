package chenyunlong.zhangli.entities;

import chenyunlong.zhangli.enums.MessageType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class WechatContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String username;
    private String headImgUrl;
    private String content;
    private MessageType messageType;
    @OneToMany
    private List<WechatImages> wechatImages;
}
