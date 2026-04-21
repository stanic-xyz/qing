package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LLM 增强分类服务
 * 使用大语言模型对交易进行语义分类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LlmClassificationService {

    private final ChatModel chatModel;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    // 预定义分类集合
    private static final Map<String, String> CATEGORY_KEYWORDS;

    static {

        CATEGORY_KEYWORDS = new HashMap<>();
        CATEGORY_KEYWORDS.put("餐饮饮食", "餐饮,美食,外卖,餐厅,饭店,小吃,水果,零食,超市");
        CATEGORY_KEYWORDS.put("交通出行", "打车,加油,停车,地铁,公交,高速,ETC,骑行,机票,火车");
        CATEGORY_KEYWORDS.put("购物消费", "京东,淘宝,拼多多,网购,电器,电脑,手机,衣服,鞋");
        CATEGORY_KEYWORDS.put("居住生活", "房租,水电,物业,燃气,家具,家电,维修,装修");
        CATEGORY_KEYWORDS.put("医疗健康", "医院,挂号,药品,体检,保险,牙科,眼科");
        CATEGORY_KEYWORDS.put("娱乐休闲", "电影,游戏,音乐,直播,视频会员,旅游,健身");
        CATEGORY_KEYWORDS.put("教育学习", "培训,课程,书籍,学费,考试,文具");
        CATEGORY_KEYWORDS.put("通讯网络", "话费,流量,宽带,网费");
        CATEGORY_KEYWORDS.put("金融保险", "转账,还款,利息,手续费,保险,投资,理财");
        CATEGORY_KEYWORDS.put("工作收入", "工资,奖金,报销,津贴,兼职,副业");
        CATEGORY_KEYWORDS.put("人情往来", "红包,礼金,礼物,请客,聚会");
        CATEGORY_KEYWORDS.put("其他", "");

    }

    /**
     * 对单条交易进行 LLM 分类
     */
    public Optional<String> classifyTransaction(TransactionRecord record) {
        if (record == null) return Optional.empty();

        String description = buildDescription(record);
        if (description == null || description.isBlank()) {
            return Optional.empty();
        }

        String prompt = buildClassificationPrompt(description, record.getAmount());

        try {
            String response = chatModel.chat(prompt);
            String category = parseCategoryResponse(response);

            if (category != null && !category.isBlank()) {
                log.debug("LLM classified '{}' as '{}'", description, category);
                return Optional.of(category);
            }
        } catch (Exception e) {
            log.warn("LLM classification failed for record {}: {}", record.getId(), e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * 批量分类（节省 API 调用）
     * 每次最多 20 条
     */
    public Map<Long, String> classifyBatch(List<TransactionRecord> records) {
        Map<Long, String> results = new HashMap<>();

        // 过滤掉已有分类或描述为空且对手方为空的记录
        List<TransactionRecord> toClassify = records.stream()
                .filter(r -> r.getCategory() == null &&
                        (r.getMerchant() != null || r.getRemark() != null || r.getCounterparty() != null))
            .toList();

        if (toClassify.isEmpty()) {
            return results;
        }

        // 分批处理
        int batchSize = 20;
        for (int i = 0; i < toClassify.size(); i += batchSize) {
            int end = Math.min(i + batchSize, toClassify.size());
            List<TransactionRecord> batch = toClassify.subList(i, end);

            try {
                Map<Long, String> batchResults = classifyBatchOnce(batch);
                results.putAll(batchResults);
                Thread.sleep(500); // 避免 API 限流
            } catch (Exception e) {
                log.warn("Batch classification failed at offset {}: {}", i, e.getMessage());
                // 单条降级处理
                for (TransactionRecord record : batch) {
                    classifyTransaction(record).ifPresent(cat -> results.put(record.getId(), cat));
                }
            }
        }

        return results;
    }

    private Map<Long, String> classifyBatchOnce(List<TransactionRecord> batch) {
        Map<Long, String> results = new HashMap<>();

        String prompt = buildBatchClassificationPrompt(batch);
        try {
            String response = chatModel.chat(prompt);
            results = parseBatchResponse(response, batch);
        } catch (Exception e) {
            log.warn("Batch LLM call failed: {}", e.getMessage());
        }

        return results;
    }

    private String buildDescription(TransactionRecord record) {
        StringBuilder sb = new StringBuilder();

        if (record.getMerchant() != null && !record.getMerchant().isBlank()) {
            sb.append("商家: ").append(record.getMerchant()).append("; ");
        }
        if (record.getRemark() != null && !record.getRemark().isBlank()) {
            sb.append("备注: ").append(record.getRemark()).append("; ");
        }
        if (record.getCounterparty() != null) {
            sb.append("对手: ").append(record.getCounterparty().getName()).append("; ");
        }
        if (record.getAccountName() != null) {
            sb.append("账户: ").append(record.getAccountName()).append("; ");
        }

        return sb.toString().trim();
    }

    private String buildClassificationPrompt(String description, BigDecimal amount) {
        String categories = CATEGORY_KEYWORDS.entrySet().stream()
            .map(e -> "- " + e.getKey() + (e.getValue().isEmpty() ? "" : ": " + e.getValue()))
            .reduce((a, b) -> a + "\n" + b)
            .orElse("");

        return String.format("""
            你是一个财务分类助手。根据以下交易信息，判断它属于哪个分类。

            交易信息: %s
            交易金额: %s

            可选分类:
            %s

            请只输出分类名称，不要其他解释。如果无法判断，输出"其他"。
            """, description, amount != null ? amount.toPlainString() : "未知", categories);
    }

    private String buildBatchClassificationPrompt(List<TransactionRecord> batch) {
        StringBuilder sb = new StringBuilder("你是一个财务分类助手。请判断以下每笔交易的分类。\n\n");
        sb.append("可选分类: ").append(String.join(", ", CATEGORY_KEYWORDS.keySet())).append("\n\n");

        int idx = 1;
        for (TransactionRecord r : batch) {
            sb.append(String.format("[%d] %s (金额: %s)\n",
                idx++,
                buildDescription(r),
                r.getAmount() != null ? r.getAmount().toPlainString() : "未知"
            ));
        }

        sb.append("\n请按以下格式输出每行的分类（只输出分类名，用换行分隔）：\n");
        for (int i = 1; i <= batch.size(); i++) {
            sb.append(i).append(". ");
        }

        return sb.toString();
    }

    private String parseCategoryResponse(String response) {
        if (response == null || response.isBlank()) return null;

        String cleaned = response.trim();
        // 移除引号、编号等
        cleaned = cleaned.replaceAll("^\"|\"$", "").trim();
        cleaned = cleaned.replaceAll("^[一二三四五六七八九十]、?", "").trim();

        // 匹配已知的分类名
        for (String cat : CATEGORY_KEYWORDS.keySet()) {
            if (cleaned.contains(cat)) {
                return cat;
            }
        }

        // 模糊匹配
        if (cleaned.contains("餐饮") || cleaned.contains("美食") || cleaned.contains("外卖")) return "餐饮饮食";
        if (cleaned.contains("交通") || cleaned.contains("出行") || cleaned.contains("加油")) return "交通出行";
        if (cleaned.contains("购物") || cleaned.contains("网购") || cleaned.contains("消费")) return "购物消费";
        if (cleaned.contains("娱乐") || cleaned.contains("游戏") || cleaned.contains("电影")) return "娱乐休闲";
        if (cleaned.contains("医疗") || cleaned.contains("健康") || cleaned.contains("医院")) return "医疗健康";
        if (cleaned.contains("居住") || cleaned.contains("房租") || cleaned.contains("水电")) return "居住生活";
        if (cleaned.contains("教育") || cleaned.contains("学习") || cleaned.contains("培训")) return "教育学习";
        if (cleaned.contains("通讯") || cleaned.contains("话费")) return "通讯网络";
        if (cleaned.contains("金融") || cleaned.contains("保险") || cleaned.contains("理财")) return "金融保险";
        if (cleaned.contains("工资") || cleaned.contains("奖金") || cleaned.contains("收入")) return "工作收入";
        if (cleaned.contains("人情") || cleaned.contains("红包") || cleaned.contains("礼金")) return "人情往来";

        return null;
    }

    private Map<Long, String> parseBatchResponse(String response, List<TransactionRecord> batch) {
        Map<Long, String> results = new HashMap<>();
        if (response == null || response.isBlank()) return results;

        String[] lines = response.split("\n");
        int idx = 0;
        for (String line : lines) {
            String category = parseCategoryResponse(line);
            if (category != null && idx < batch.size()) {
                results.put(batch.get(idx).getId(), category);
                idx++;
            }
        }

        return results;
    }

    /**
     * 获取分类建议（用于前端预览）
     */
    public String suggestCategory(String description, BigDecimal amount) {
        try {
            String prompt = buildClassificationPrompt(
                "商家/描述: " + description, amount
            );
            String response = chatModel.chat(prompt);
            return parseCategoryResponse(response) != null ? parseCategoryResponse(response) : "其他";
        } catch (Exception e) {
            log.warn("Category suggestion failed: {}", e.getMessage());
            return "其他";
        }
    }
}
