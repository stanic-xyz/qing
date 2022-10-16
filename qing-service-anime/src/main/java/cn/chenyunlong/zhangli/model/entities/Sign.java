package cn.chenyunlong.zhangli.model.entities;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Data
public class Sign extends BaseEntity<Sign> {

    private Long id;
    private Long userId;
    private LocalDate dateMonth;
    private Integer mask;
    private Integer continueSignMonth;

}
