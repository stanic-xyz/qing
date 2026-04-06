package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.ParserItemDTO;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.enums.ConfigStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.repository.ParserConfigRepository;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParserConfigService {

    @Resource
    private ParserConfigRepository repository;

    @Resource
    private List<FileParser> parserList;

    @Resource
    private ChannelRepository channelRepository;

    /**
     * 获取所有可用的解析器列表（内置 + 自定义）
     */
    public List<ParserItemDTO> getAllAvailableParsers() {
        List<ParserItemDTO> result = new ArrayList<>();

        Map<String, Channel> channelMap = channelRepository.findAll().stream().collect(Collectors.toMap(Channel::getCode, channel -> channel));
        parserList.forEach(parser -> {
            ParserItemDTO dto = new ParserItemDTO();
            dto.setId("builtin:" + parser.getMetaData().getChannelCode().toUpperCase());
            dto.setFileType(parser.getMetaData().getSupportedFileExtension());
            dto.setName(parser.getMetaData().getParserName());
            dto.setIsBuiltIn(true);
            result.add(dto);
        });

        // 2. 获取自定义解析器 (仅已发布的)
        List<ParserConfig> customConfigs = repository.findAll().stream()
                .filter(c -> ConfigStatusEnum.PUBLISHED.equals(c.getStatus()))
                .toList();

        for (ParserConfig config : customConfigs) {
            ParserItemDTO dto = new ParserItemDTO();
            dto.setId("custom:" + config.getId());
            dto.setName(config.getName());
            dto.setFileType(config.getFileType());
            dto.setIsBuiltIn(config.getIsBuiltIn() != null ? config.getIsBuiltIn() : false);
            result.add(dto);
        }

        return result;
    }

    private String getBuiltInName(String key) {
        return switch (key) {
            case "ALIPAY" -> "支付宝账单解析器";
            case "WECHAT" -> "微信账单解析器";
            case "CMB" -> "招商银行解析器";
            case "CCB" -> "建设银行解析器";
            case "QIANJI" -> "钱迹账单解析器";
            case "JINGDONG" -> "京东账单解析器";
            case "BOC_CREDIT" -> "中国银行信用卡解析器";
            case "CITIC_CREDIT" -> "中信信用卡解析器";
            case "PINGAN" -> "平安银行解析器";
            case "BOCOM_CREDIT" -> "交通银行信用卡解析器";
            case "BANK" -> "通用银行解析器";
            case "YIPAY" -> "易支付解析器";
            case "TIKTOK" -> "抖音账单解析器";
            default -> key + " 解析器";
        };
    }

    public List<ParserConfig> findAllConfigs() {
        return repository.findAll();
    }

    public ParserConfig getConfig(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("配置不存在"));
    }

    public ParserConfig saveConfig(ParserConfig config) {
        if (config.getIsBuiltIn() == null) {
            config.setIsBuiltIn(false);
        }
        return repository.save(config);
    }

    public void deleteConfig(Long id) {
        repository.deleteById(id);
    }
}
