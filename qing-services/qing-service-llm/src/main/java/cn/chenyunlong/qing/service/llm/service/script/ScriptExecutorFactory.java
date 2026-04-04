package cn.chenyunlong.qing.service.llm.service.script;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ScriptExecutorFactory {

    private final Map<String, ScriptExecutor> executors = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        register(new GroovyScriptExecutor());
    }

    @PreDestroy
    public void shutdownExecutors() {
        for (ScriptExecutor executor : executors.values()) {
            try {
                executor.close();
            } catch (Exception e) {
                log.warn("Failed to close script executor {}: {}", executor.getLanguage(), e.getMessage());
            }
        }
    }

    public void register(ScriptExecutor executor) {
        executors.put(executor.getLanguage().toLowerCase(), executor);
        log.info("Registered script executor: {}", executor.getLanguage());
    }

    public ScriptExecutor getExecutor(String language) {
        if (language == null || language.isEmpty()) {
            language = "groovy";
        }
        ScriptExecutor executor = executors.get(language.toLowerCase());
        if (executor == null) {
            throw new IllegalArgumentException("Unsupported script language: " + language);
        }
        return executor;
    }

    public Object execute(String language, String script, Map<String, Object> context) {
        ScriptExecutor executor = getExecutor(language);
        return executor.execute(script, context);
    }

    public boolean isSupported(String language) {
        return language != null && executors.containsKey(language.toLowerCase());
    }
}
