package cn.chenyunlong.qing.service.llm.config;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChain4jConfig {

    @Bean
    public OllamaChatModel embeddingModel() {
        // 使用本地模型，如 all-MiniLM-L6-v2
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("deepseek-r1:1.5b")
                .temperature(0.0)
                .build();
    }

    @Bean
    public EmbeddingStore vectorStore(OllamaChatModel embeddingModel) {
        // 使用 Chroma 或内存存储
        return new InMemoryEmbeddingStore<>();
    }
}
