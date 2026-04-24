package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * LLM 解析请求
 */
@Data
public class LlmParseRequest {
    private MultipartFile file;
    private String categoryStrategy;
    private Long forcedCategoryId;
    private boolean async = false;

    public LlmParseRequest() {}

    public LlmParseRequest(MultipartFile file, String categoryStrategy) {
        this.file = file;
        this.categoryStrategy = categoryStrategy;
    }
}
