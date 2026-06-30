package cn.chenyunlong.qing.service.llm.dto.transactions;

import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import lombok.Data;

import java.util.List;

/**
 * 交易流水分页查询参数封装。
 */
@Data
public class TransactionQueryDTO {

    private int page = 0;
    private int size = 20;

    /**
     * 按渠道过滤（通过 Account.channel 关联）
     */
    private List<Long> channelIds;

    /**
     * 按账户 ID 过滤
     */
    private List<Long> accountIds;

    /**
     * 交易类型（对应 TransactionRecord 的 type 字段）
     */
    private String type;

    /**
     * 关键字匹配商户或备注
     */
    private String keyword;

    /**
     * 交易时间范围起始 (yyyy-MM-dd)
     */
    private String startDate;

    /**
     * 交易时间范围截止 (yyyy-MM-dd)
     */
    private String endDate;

    /**
     * 匹配状态
     */
    private MatchStatusEnum matchStatus;

    /**
     * 流水角色，默认 PRIMARY（实际资金流水）
     */
    private RecordRoleEnum recordRole = RecordRoleEnum.PRIMARY;

    /**
     * 排序字段，默认 transactionTime
     */
    private String sortField = "transactionTime";

    /**
     * 排序方向，默认 DESC
     */
    private String sortDirection = "DESC";
}
