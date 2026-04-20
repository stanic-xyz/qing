package cn.chenyunlong.qing.service.llm.dto.counterpayty;

import cn.chenyunlong.qing.service.llm.enums.CounterpartyTypeEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class CounterpartyCreateDto {

    private String name;        // e.g. 鲜农果蔬

    @Enumerated(EnumType.STRING)
    private CounterpartyTypeEnum type;        // MERCHANT (商户), INDIVIDUAL (个人), CORPORATE (企业)

    private Long defaultCategoryId; // 默认分类ID，可为空

    private String remark;      // 备注，可为空

    private Boolean isActive = true;
}