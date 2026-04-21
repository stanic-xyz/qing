package cn.chenyunlong.qing.service.llm.dto.counterparty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounterpartyResponseDto {
    private Long id;
    private String name;
    private String type;
    private String defaultCategoryName;
    private String remark;
    private Boolean isActive;
}
