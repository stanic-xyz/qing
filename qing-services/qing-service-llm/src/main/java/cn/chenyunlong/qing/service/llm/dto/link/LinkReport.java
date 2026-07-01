package cn.chenyunlong.qing.service.llm.dto.link;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 关联执行结果报告。
 */
@Data
public class LinkReport {

    /** 账户 ID */
    private Long accountId;

    /** 参与扫描的 TRACE 记录总数 */
    private int totalTraceRecords;

    /** 成功关联到 PRIMARY 的记录数 */
    private int linkedCount;

    /** 未找到匹配的 TRACE 记录数 */
    private int unlinkedCount;

    /** 各组明细 */
    private List<LinkDetail> details = new ArrayList<>();

    @Data
    public static class LinkDetail {
        /** TRACE 记录 ID */
        private Long traceRecordId;

        /** 关联到的 PRIMARY 记录 ID */
        private Long linkedPrimaryRecordId;

        /** 实际命中的匹配字段 */
        private List<String> matchedBy = new ArrayList<>();
    }
}
