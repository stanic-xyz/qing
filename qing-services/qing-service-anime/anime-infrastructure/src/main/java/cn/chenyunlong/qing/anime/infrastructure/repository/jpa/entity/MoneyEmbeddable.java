package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * 值对象的数据库映射
 */
@Getter
@Embeddable
public class MoneyEmbeddable {
    private BigDecimal amount;
    private String currency;

    public MoneyEmbeddable(BigDecimal amount, Currency currency) {

    }

    public MoneyEmbeddable() {

    }
}
