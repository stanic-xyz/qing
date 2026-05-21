package cn.chenyunlong.qing.service.llm.dto.parser;

import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseResult {
    private FileMetadata metadata;
    private List<UnifiedDraftRecord> records;
    private Map<String, List<String>> categories = new HashMap<>();
    private List<String> accountList = new ArrayList<>();

    public ParseResult(FileMetadata metadata, List<UnifiedDraftRecord> draftRecordList) {
        setMetadata(metadata);
        records = draftRecordList;
    }
}
