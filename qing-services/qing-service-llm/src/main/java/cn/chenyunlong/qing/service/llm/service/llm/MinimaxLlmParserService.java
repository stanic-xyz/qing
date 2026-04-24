package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * MiniMax LLM 解析服务实现
 *
 * <p>通过 OpenAI 兼容接口调用 MiniMax LLM。
 * 使用方式：
 * <ol>
 *   <li>在 application.yml 中启用 minimax profile 或配置 minimax.api-key</li>
 *   <li>确保 {@link cn.chenyunlong.qing.service.llm.config.MinimaxChatModelConfig} 被加载</li>
 *   <li>系统会自动注入 {@link ChatLanguageModel} Bean</li>
 * </ol>
 *
 * <p>示例 application.yml 配置：
 * <pre>
 * spring:
 *   profiles:
 *     active: minimax
 *
 * minimax:
 *   api-key: your-minimax-api-key
 *   model: MiniMax-Text-01
 *   temperature: 0.1
 *   timeout: 120
 * </pre>
 */
@Service
@Profile("minimax")
@Slf4j
public class MinimaxLlmParserService implements LlmParserService {

    /**
     * ChatLanguageModel 由 {@link cn.chenyunlong.qing.service.llm.config.MinimaxChatModelConfig} 提供
     * 仅在 minimax profile 激活时注入
     */
    @Nullable
    private final ChatLanguageModel chatLanguageModel;

    public MinimaxLlmParserService(@Nullable ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @Override
    public String parse(String prompt, CategoryStrategy strategy) {
        log.info("Calling MiniMax LLM with strategy: {}", strategy);

        if (chatLanguageModel == null) {
            throw new RuntimeException("MiniMax ChatLanguageModel 未注入，请确保启用了 minimax profile");
        }

        try {
            long startTime = System.currentTimeMillis();
            String response = chatLanguageModel.generate(prompt);
            long elapsed = System.currentTimeMillis() - startTime;

            log.info("MiniMax LLM call completed, elapsed: {}ms, response length: {}",
                    elapsed, response != null ? response.length() : 0);

            if (response == null || response.isBlank()) {
                throw new RuntimeException("MiniMax LLM 返回空响应");
            }

            return response;
        } catch (Exception e) {
            log.error("MiniMax LLM call failed: {}", e.getMessage(), e);
            throw new RuntimeException("MiniMax LLM 调用失败: " + e.getMessage(), e);
        }
    }
}
