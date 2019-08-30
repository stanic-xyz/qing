package chenyunlong.zhangli.entities;

import chenyunlong.zhangli.entities.common.BaseEntitiy;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class WechatImages extends BaseEntitiy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonBackReference
    private WechatContent wechatContent;
    private String imageName;
    private String ImageUrl;
}
