package cn.chenyunlong.qing.service.llm.service.script;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScriptExecutorFactoryTest {

    /**
     * 验证未注册的脚本语言会抛出参数异常。
     */
    @Test
    @DisplayName("getExecutor 对未注册脚本语言抛出 IllegalArgumentException")
    void shouldThrowIllegalArgumentExceptionWhenLanguageIsUnsupported() {
        ScriptExecutorFactory factory = new ScriptExecutorFactory();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.getExecutor("python")
        );

        assertEquals("不支持的脚本语言: python", exception.getMessage());
    }

    /**
     * 验证已注册的执行器可以被工厂正常获取。
     */
    @Test
    @DisplayName("getExecutor 返回已注册的脚本执行器")
    void shouldReturnRegisteredExecutor() {
        ScriptExecutorFactory factory = new ScriptExecutorFactory();
        ScriptExecutor executor = new StubScriptExecutor();
        factory.register(executor);

        ScriptExecutor actual = factory.getExecutor("stub");

        assertSame(executor, actual);
    }

    /**
     * 测试用脚本执行器。
     */
    private static final class StubScriptExecutor implements ScriptExecutor {

        /**
         * 返回测试语言标识。
         *
         * @return 语言名称
         */
        @Override
        public String getLanguage() {
            return "stub";
        }

        /**
         * 执行占位脚本。
         *
         * @param script 脚本内容
         * @param context 上下文
         * @return 透传脚本内容
         */
        @Override
        public Object execute(String script, Map<String, Object> context) {
            return script;
        }
    }
}
