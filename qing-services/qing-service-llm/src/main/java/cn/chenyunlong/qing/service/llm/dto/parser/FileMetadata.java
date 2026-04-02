package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer recordCount;
    private LocalDateTime exportTime;
}