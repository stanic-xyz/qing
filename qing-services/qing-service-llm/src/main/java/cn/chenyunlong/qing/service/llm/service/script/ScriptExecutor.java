package cn.chenyunlong.qing.service.llm.service.script;

import java.util.Map;

public interface ScriptExecutor extends AutoCloseable {

    String getLanguage();

    Object execute(String script, Map<String, Object> context);

    default long getTimeout() {
        return 5000;
    }

    @Override
    default void close() throws Exception {
        // no-op
    }
}
