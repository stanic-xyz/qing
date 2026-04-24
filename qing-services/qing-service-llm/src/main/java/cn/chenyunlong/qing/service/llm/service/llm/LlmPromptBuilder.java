package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * LLM Prompt 构建器 - 根据策略构建不同的 Prompt
 */
@Service
@Slf4j
public class LlmPromptBuilder {

    /**
     * 构建完整的 Prompt
     */
    public String buildPrompt(String rawBillText,
                               LlmParseContextLoader.SystemContext context,
                               CategoryStrategy strategy) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("【任务说明】\n");
        prompt.append("你是一个账单分类助手。请根据用户提供的账单文本，识别每条记录的信息（金额、时间、描述、对手方），\n");
        prompt.append("并按照指定的分类策略归类到已有分类中。如果发现需要新建的分类或账户，请单独建议。\n");
        prompt.append("\n");

        prompt.append("【分类策略】\n");
        prompt.append("策略名称: ").append(strategy.getCode()).append("\n");
        prompt.append("策略说明: ").append(strategy.getDescription()).append("\n");
        prompt.append("\n");
        prompt.append(getStrategyDetail(strategy)).append("\n");
        prompt.append("\n");

        prompt.append("【原始账单文本】\n");
        prompt.append(rawBillText).append("\n");
        prompt.append("\n");

        prompt.append("【系统上下文】\n");
        prompt.append(context.toCategoryContext()).append("\n");
        prompt.append(context.toAccountContext()).append("\n");
        prompt.append(context.toCounterpartyContext()).append("\n");

        prompt.append("\n【输出要求】\n");
        prompt.append("请以 JSON 格式输出，结构如下：\n");
        prompt.append("{\n");
        prompt.append("  \"success\": true,\n");
        prompt.append("  \"taskId\": \"uuid\",\n");
        prompt.append("  \"records\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"amount\": 123.45,\n");
        prompt.append("      \"transactionType\": \"EXPENSE\",\n");
        prompt.append("      \"transactionTime\": \"2024-01-01T12:00:00\",\n");
        prompt.append("      \"counterparty\": \"商家名称\",\n");
        prompt.append("      \"description\": \"交易描述\",\n");
        prompt.append("      \"categoryId\": 1,\n");
        prompt.append("      \"categoryName\": \"分类名称\",\n");
        prompt.append("      \"confidence\": 0.85,\n");
        prompt.append("      \"platformSource\": \"京东\",\n");
        prompt.append("      \"consumptionType\": \"数码电子\"\n");
        prompt.append("    }\n");
        prompt.append("  ],\n");
        prompt.append("  \"summary\": {\n");
        prompt.append("    \"totalRecords\": 10,\n");
        prompt.append("    \"successCount\": 9,\n");
        prompt.append("    \"failedCount\": 1,\n");
        prompt.append("    \"avgConfidence\": 0.82\n");
        prompt.append("  },\n");
        prompt.append("  \"suggestedNewCategories\": [],\n");
        prompt.append("  \"unmatchedRecords\": []\n");
        prompt.append("}\n");

        return prompt.toString();
    }

    /**
     * 获取策略详细说明
     */
    private String getStrategyDetail(CategoryStrategy strategy) {
        return switch (strategy) {
            case BY_CONSUMPTION_TYPE -> """
                分类逻辑：
                - 餐饮美食（如餐厅、快餐、外卖）
                - 购物消费（如超市、便利店、网购）
                - 交通出行（如打车、公交、地铁）
                - 数码电子（如电脑、手机、电子产品）
                - 娱乐休闲（如电影、游戏、旅游）
                - 居住支出（如房租、水电煤）
                - 医疗健康（如医院、药店）
                - 教育培训（如课程、培训）
                """;
            case BY_PLATFORM -> """
                分类逻辑：
                - 京东（京东商城购物）
                - 美团（美团外卖、酒店、团购）
                - 抖音（抖音直播、短视频打赏）
                - 淘宝/天猫（淘宝天猫购物）
                - 拼多多（拼多多购物）
                - 微信支付（微信转账、红包）
                - 支付宝（支付宝转账、收款）
                """;
            case HYBRID -> """
                分类逻辑：综合考虑消费类型和平台来源
                - 优先匹配消费类型
                - 同类型下按平台细分
                - 例如：京东购买手机 -> 分类：数码电子，平台：京东
                """;
        };
    }

    /**
     * 构建简短的测试 Prompt（用于测试）
     */
    public String buildTestPrompt() {
        return """
            【任务说明】
            这是一个测试任务。

            【分类策略】
            策略名称: BY_CONSUMPTION_TYPE
            策略说明: 按消费类型分类

            【原始账单文本】
            2024-01-01 京东 购买商品 123.45元
            2024-01-02 美团外卖 餐饮 45.00元
            2024-01-03 抖音直播充值 50.00元

            【系统上下文】
            分类列表：
            - 餐饮美食 (ID:1)
            - 数码电子 (ID:3)
            - 娱乐 (ID:5)
            - 交通出行 (ID:6)
            - 购物 (ID:7)

            【输出要求】
            请以 JSON 格式输出账单列表。
            """;
    }
}
