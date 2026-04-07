package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.ParserItemDTO;
import cn.chenyunlong.qing.service.llm.dto.channel.ChannelDto;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.enums.ConfigStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.repository.ParserConfigRepository;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
     * 内置解析器现在也从数据库读取，由BuiltinParserInitializer初始化
     */
    public List<ParserItemDTO> getAllAvailableParsers() {
        return repository.findAllByStatusEquals(ConfigStatusEnum.PUBLISHED)
                .stream().map(
                        parserConfig ->
                        {
                            ParserItemDTO dto = new ParserItemDTO();
                            dto.setId(parserConfig.getId());
                            dto.setName(parserConfig.getName());
                            dto.setFileType(parserConfig.getFileType());
                            if (parserConfig.getChannel() != null) {
                                dto.setChannel(ChannelDto.of(parserConfig.getChannel()));
                            }
                            dto.setIsBuiltIn(parserConfig.getIsBuiltIn() != null ? parserConfig.getIsBuiltIn() : false);
                            return dto;
                        }).toList();
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
