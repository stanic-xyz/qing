package cn.chenyunlong.qing.service.llm.dto.dedup;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 去重执行结果报告。
 */
@Data
public class DedupReport {

    /** 账户 ID */
    private Long accountId;

    /** 参与扫描的总记录数（未软删除且未标记的） */
    private int totalRecords;

    /** 发现的重复组数 */
    private int duplicateGroups;

    /** 被标记为重复的记录数 */
    private int markedCount;

    /** 被保留的记录数（= duplicateGroups） */
    private int keptCount;

    /** 各组明细 */
    private List<DedupGroupDetail> details = new ArrayList<>();

    @Data
    public static class DedupGroupDetail {

        /** 被保留的那条记录 ID */
        private Long keptRecordId;

        /** 被标记为重复的记录 ID 列表 */
        private List<Long> duplicateRecordIds = new ArrayList<>();

        /** 实际命中的匹配字段 */
        private List<String> matchedBy = new ArrayList<>();

        /** 是否有更多重复记录未在列表展示（超过 99 条时） */
        private boolean hasMore;
    }
}
