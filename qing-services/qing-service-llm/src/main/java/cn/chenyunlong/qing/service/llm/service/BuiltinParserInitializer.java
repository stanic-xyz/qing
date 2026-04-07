package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.enums.ConfigStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.repository.ParserConfigRepository;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内置解析器初始化服务
 * 在应用启动时，将代码中的内置解析器注册到数据库
 */
@Slf4j
@Service
public class BuiltinParserInitializer {

    @Resource
    private List<FileParser> parserList;

    @Resource
    private ParserConfigRepository parserConfigRepository;

    @Resource
    private ChannelRepository channelRepository;

    @PostConstruct
    @Transactional
    public void initBuiltinParsers() {
        log.info("开始初始化内置解析器到数据库...");

        // 获取所有渠道
        Map<String, Channel> channelMap = channelRepository.findAll().stream()
                .collect(Collectors.toMap(Channel::getCode, channel -> channel));

        // 记录已注册的解析器
        Map<String, ParserConfig> existingParsers = parserConfigRepository.findAll().stream()
                .filter(ParserConfig::getIsBuiltIn)
                .collect(Collectors.toMap(
                        config -> config.getChannel().getCode() + ":" + config.getFileType(),
                        config -> config
                ));

        List<ParserConfig> newParserConfigList = new ArrayList<>();
        List<ParserConfig> updatedParserConfigList = new ArrayList<>();

        for (FileParser parser : parserList) {
            try {
                String channelCode = parser.channelCode();
                if (!channelMap.containsKey(channelCode)) {
                    log.warn("渠道 {} 不存在，跳过内置解析器 {} 的注册", channelCode, parser.getClass().getSimpleName());
                    continue;
                }

                List<String> supportedFileExtensions = parser.supportedFileExtensions();

                if (CollUtil.isEmpty(supportedFileExtensions)) {
                    log.warn("解析器 {} 没有定义支持的文件扩展名，跳过注册", parser.getClass().getSimpleName());
                    continue;
                }

                for (String fileType : supportedFileExtensions) {
                    String parserName = getParserDisplayName(parser.getClass().getSimpleName());
                    String uniqueKey = channelCode + ":" + fileType;

                    // 获取或创建渠道
                    Channel channel = channelMap.get(channelCode);
                    if (channel == null) {
                        log.warn("渠道 {} 不存在，跳过解析器 {}", channelCode, parserName);
                        continue;
                    }

                    ParserConfig config = existingParsers.get(uniqueKey);

                    if (!existingParsers.containsKey(uniqueKey)) {
                        // 创建新的内置解析器配置
                        config = new ParserConfig();
                        config.setName(parserName);
                        config.setChannel(channel);
                        config.setFileType(fileType);
                        config.setIsBuiltIn(true);
                        config.setStatus(ConfigStatusEnum.PUBLISHED);
                        config.setEncoding("UTF-8");
                        config.setSkipRows(0);
                        newParserConfigList.add(config);
                        log.info("✅ 创建内置解析器: {} ({}:{})", parserName, channelCode, fileType);
                    } else {
                        // 更新现有配置（确保状态为已发布）
                        if (!ConfigStatusEnum.PUBLISHED.equals(config.getStatus())) {
                            config.setStatus(ConfigStatusEnum.PUBLISHED);
                            updatedParserConfigList.add(config);
                            log.info("🔄 更新内置解析器状态: {} -> PUBLISHED", parserName);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("❌ 初始化解析器 {} 失败: {}", parser.getClass().getSimpleName(), e.getMessage(), e);
            }
        }

        if (CollUtil.isNotEmpty(newParserConfigList)) {
            log.info("创建了 {} 个内置解析器", CollUtil.size(newParserConfigList));
            parserConfigRepository.saveAll(newParserConfigList);
        }

        if (CollUtil.isNotEmpty(updatedParserConfigList)) {
            log.info("更新了 {} 个内置解析器", CollUtil.size(updatedParserConfigList));
            parserConfigRepository.saveAll(updatedParserConfigList);
        }

        log.info("内置解析器初始化完成: 创建 {} 个, 更新 {} 个", CollUtil.size(newParserConfigList), CollUtil.size(updatedParserConfigList));
    }

    /**
     * 根据类名获取解析器显示名称
     */
    private String getParserDisplayName(String className) {
        return switch (className) {
            case "AlipayParser" -> "支付宝账单解析器";
            case "WechatParser" -> "微信账单解析器";
            case "CmbParser" -> "招商银行解析器";
            case "CcbExcelParser" -> "建设银行Excel解析器";
            case "CcbPdfParser" -> "建设银行PDF解析器";
            case "QianjiParser" -> "钱迹账单解析器";
            case "JingdongParser" -> "京东账单解析器";
            case "BocCreditParser" -> "中国银行信用卡解析器";
            case "CiticCreditParser" -> "中信信用卡解析器";
            case "PingAnParser" -> "平安银行解析器";
            case "BocomCreditParser" -> "交通银行信用卡解析器";
            case "BankParser" -> "通用银行解析器";
            case "YipayParser" -> "易支付解析器";
            case "TikTokParser" -> "抖音账单解析器";
            default -> className + "解析器";
        };
    }
}
