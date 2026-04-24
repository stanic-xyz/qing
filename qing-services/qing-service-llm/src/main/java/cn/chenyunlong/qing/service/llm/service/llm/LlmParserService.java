package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;

/**
 * LLM 解析服务接口
 * 定义 LLM 解析的契约，具体实现可以是：
 * - {@link RealLlmParserService}: 调用真实 LLM API
 * - Mock 方式: 测试时使用 Mockito Mock
 */
public interface LlmParserService {

    /**
     * 解析账单
     *
     * @param prompt   构建好的 Prompt（包含原始文本+系统上下文）
     * @param strategy 分类策略
     * @return LLM 返回的 JSON 字符串
     */
    String parse(String prompt, CategoryStrategy strategy);
}
