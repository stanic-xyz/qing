package chenyunlong.zhangli.model.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class Sign extends BaseEntity {

    private Long id;
    private Long userId;
    private LocalDate dateMonth;
    private Integer mask;
    private Integer continueSignMonth;

}
