package cn.chenyunlong.qing.service.llm.enums;

import cn.chenyunlong.common.enums.CommonEnum;
import lombok.Getter;

public enum TransactionType implements CommonEnum {
    // 收入
    INCOME(1, "收入", TransactionDirectionTypeEnum.INCOME),
    // 支出
    EXPENSE(2, "支出", TransactionDirectionTypeEnum.EXPENSE),
    // 转账
    TRANSFER(3, "转账", TransactionDirectionTypeEnum.INCOME, TransactionDirectionTypeEnum.EXPENSE),
    ;

    private final int code;
    private final String description;

    @Getter
    private final TransactionDirectionTypeEnum[] allowDirections;

    TransactionType(int code, String name, TransactionDirectionTypeEnum... transactionDirectionTypeEnum) {
        this.code = code;
        this.description = name;
        this.allowDirections = transactionDirectionTypeEnum;

    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
