package cn.chenyunlong.qing.service.llm.dto.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseResult {
    private FileMetadata metadata;
    private List<TransactionRecord> records;
}