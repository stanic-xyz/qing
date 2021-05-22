package chenyunlong.zhangli.model.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Sign extends BaseEntity {

    private Long id;
    private Long userId;
    private Date dateMonth;
    private Integer mask;
    private Integer continueSignMonth;

}
