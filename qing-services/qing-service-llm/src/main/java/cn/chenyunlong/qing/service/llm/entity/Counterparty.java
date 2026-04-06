package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.CounterpartyTypeEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "finance_counterparty")
@Data
public class Counterparty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // e.g. 鲜农果蔬

    @Enumerated(EnumType.STRING)
    private CounterpartyTypeEnum type;        // MERCHANT (商户), INDIVIDUAL (个人), CORPORATE (企业)

    @ManyToOne
    private Category defaultCategory; // 默认分类，e.g. 餐饮美食

    private String remark;      // 备注

    private Boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany
    private List<TransactionMatcher> matchers;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
