package cn.chenyunlong.qing.service.llm.dto.imports;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class QianjiRecordDTO {
    private String originalId;
    private LocalDateTime time;
    private String category1;
    private String category2;
    private String typeText;
    private BigDecimal amount;
    private String currency;
    private String account1;
    private String account2;
    private String remark;
    private String reimbursed;
    private BigDecimal fee;
    private BigDecimal coupon;
    private String bookkeeper;
    private String billMark;
    private String tags;
    private String billImage;
    private String linkedBillId;
}
