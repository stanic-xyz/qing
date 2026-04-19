package cn.chenyunlong.qing.service.llm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "finance_counterparty_account")
@Data
public class CounterpartyAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "counterparty_id")
    @JsonBackReference
    private Counterparty counterparty;

    private String accountNo; // 账号尾号或全号
    private String bankName; // 开户行
    private String accountName; // 户名
}