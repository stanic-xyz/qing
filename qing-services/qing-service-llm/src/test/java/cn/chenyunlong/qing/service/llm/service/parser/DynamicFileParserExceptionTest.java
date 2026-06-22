package cn.chenyunlong.qing.service.llm.service.parser;
import cn.chenyunlong.qing.service.llm.dto.parser.FieldMappingRule;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.service.script.ScriptExecutorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DynamicFileParserExceptionTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 验证不支持的脚本语言会被归类为参数异常。
     */
    @Test
    @DisplayName("parse 在严格模式下把不支持的脚本语言抛为 IllegalArgumentException")
    void shouldThrowIllegalArgumentExceptionForUnsupportedScriptLanguage() throws Exception {
        ParserConfig config = createCsvConfig(List.of(createRule("amount", "python", true)));
        DynamicFileParser parser = new DynamicFileParser(config, new ScriptExecutorFactory(), true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse(
                        new ByteArrayInputStream("123.45".getBytes(StandardCharsets.UTF_8)),
                        "demo.csv"
                )
        );

        assertEquals("不支持的脚本语言: python", exception.getMessage());
    }

    /**
     * 验证未知目标字段仍然保持参数异常语义。
     */
    @Test
    @DisplayName("parse 在严格模式下对未知目标字段保留 IllegalArgumentException")
    void shouldKeepIllegalArgumentExceptionForUnknownTargetField() throws Exception {
        ParserConfig config = createCsvConfig(List.of(createRule("unknownField", null, false)));
        DynamicFileParser parser = new DynamicFileParser(config, new ScriptExecutorFactory(), true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse(
                        new ByteArrayInputStream("123.45".getBytes(StandardCharsets.UTF_8)),
                        "demo.csv"
                )
        );

        assertEquals("Unknown target field: unknownField", exception.getMessage());
    }

    /**
     * 构造最小可运行的 CSV 解析配置。
     *
     * @param rules 字段映射规则
     * @return 解析配置
     * @throws Exception JSON 序列化异常
     */
    private ParserConfig createCsvConfig(List<FieldMappingRule> rules) throws Exception {
        ParserConfig config = new ParserConfig();
        config.setName("dynamic-test");
        config.setFileType("CSV");
        config.setEncoding("UTF-8");
        config.setSkipRows(0);
        config.setFieldMappingRules(objectMapper.writeValueAsString(rules));
        return config;
    }

    /**
     * 构造字段映射规则。
     *
     * @param targetField 目标字段
     * @param scriptLanguage 脚本语言
     * @param scriptEnabled 是否启用脚本
     * @return 字段映射规则
     */
    private FieldMappingRule createRule(String targetField, String scriptLanguage, boolean scriptEnabled) {
        FieldMappingRule rule = new FieldMappingRule();
        rule.setTargetField(targetField);
        rule.setSourceIndex(0);
        rule.setScriptLanguage(scriptLanguage);
        rule.setScriptEnabled(scriptEnabled);
        if (scriptEnabled) {
            rule.setScriptRule("return value");
        }
        return rule;
    }
}
