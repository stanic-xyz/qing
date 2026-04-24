package cn.chenyunlong.qing.service.llm.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * MiniMax LLM 配置
 *
 * <p>通过 OpenAI 兼容接口接入 MiniMax LLM。
 * MiniMax API Base URL: https://api.minimax.chat/v1</p>
 *
 * <p>使用方式：在 application.yml 中配置 minimax.api-key 和 minimax.model</p>
 *
 * <p>示例配置：
 * <pre>
 * minimax:
 *   api-key: your-api-key
 *   model: MiniMax-Text-01
 *   base-url: https://api.minimax.chat/v1
 *   temperature: 0.1
 *   timeout: 120
 * </pre>
 * </p>
 */
@Configuration
@Slf4j
@Profile("minimax")
public class MinimaxChatModelConfig {

    /**
     * MiniMax 配置属性
     */
    @Bean
    @ConfigurationProperties(prefix = "minimax")
    public MinimaxProperties minimaxProperties() {
        return new MinimaxProperties();
    }

    /**
     * 创建 MiniMax ChatLanguageModel（用于 LLM 解析服务）
     *
     * <p>注入到 {@link cn.chenyunlong.qing.service.llm.service.llm.MinimaxLlmParserService} 中使用。</p>
     */
    @Bean
    public ChatLanguageModel minimaxChatLanguageModel(MinimaxProperties properties) {
        log.info("Initializing MiniMax ChatLanguageModel: baseUrl={}, model={}",
                properties.getBaseUrl(), properties.getModel());

        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl(properties.getBaseUrl())
                .apiKey(properties.getApiKey())
                .modelName(properties.getModel())
                .temperature(properties.getTemperature())
                .timeout(java.time.Duration.ofSeconds(properties.getTimeout()))
                .logRequests(properties.isLogRequests())
                .logResponses(properties.isLogResponses())
                .build();

        log.info("MiniMax ChatLanguageModel initialized successfully");
        return model;
    }

    @Data
    public static class MinimaxProperties {
        /**
         * MiniMax API Key
         */
        private String apiKey;

        /**
         * MiniMax 模型名称
         * 常用：MiniMax-Text-01（通用文本）、abab6.5s-chat（对话）
         */
        private String model = "MiniMax-Text-01";

        /**
         * API Base URL（OpenAI 兼容端点）
         */
        private String baseUrl = "https://api.minimax.chat/v1";

        /**
         * 温度参数（0.0~1.0），越低越确定性
         */
        private double temperature = 0.1;

        /**
         * 请求超时时间（秒）
         */
        private int timeout = 120;

        /**
         * 是否打印请求日志
         */
        private boolean logRequests = false;

        /**
         * 是否打印响应日志
         */
        private boolean logResponses = false;
    }
}
