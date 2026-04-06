package cn.chenyunlong.qing.service.llm.dto.counterpayty;

import cn.chenyunlong.qing.service.llm.enums.CounterpartyTypeEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class CounterpartyUpdateDto {

    @Id
    private Long id;

    private String name;        // e.g. 鲜农果蔬

    @Enumerated(EnumType.STRING)
    private CounterpartyTypeEnum type;        // MERCHANT (商户), INDIVIDUAL (个人), CORPORATE (企业)

    private String defaultCategory; // 默认分类，e.g. 餐饮美食

    private String remark;      // 备注

    private Boolean isActive = true;
}
