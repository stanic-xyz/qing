package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 真实 LLM 解析服务实现
 * 调用实际的 LLM API（如 Minimax / OpenAI / Ollama）
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RealLlmParserService implements LlmParserService {

    private final ChatLanguageModel chatLanguageModel;

    @Override
    public String parse(String prompt, CategoryStrategy strategy) {
        log.info("Calling real LLM with strategy: {}", strategy);
        try {
            String response = chatLanguageModel.generate(prompt);
            log.debug("LLM response length: {}", response.length());
            return response;
        } catch (Exception e) {
            log.error("Real LLM call failed", e);
            throw new RuntimeException("LLM 调用失败: " + e.getMessage(), e);
        }
    }
}
