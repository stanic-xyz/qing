package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Data
public class Sign extends BaseEntity {

    private Long id;
    private Long userId;
    private LocalDate dateMonth;
    private Integer mask;
    private Integer continueSignMonth;

}
