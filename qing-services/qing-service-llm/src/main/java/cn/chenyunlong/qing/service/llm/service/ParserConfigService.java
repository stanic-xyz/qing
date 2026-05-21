package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.service.llm.dto.ParserItemDTO;
import cn.chenyunlong.qing.service.llm.dto.channel.ChannelDto;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.enums.ConfigStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.repository.ParserConfigRepository;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import cn.hutool.core.lang.Assert;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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


    /**
     * 更新解析器配置（仅草稿状态可更新）
     *
     * @param id     解析器ID
     * @param config 前端传入的新配置（仅更新传入的非空字段）
     * @throws BusinessException 内置或已发布状态下不允许编辑
     */
    public void updateConfig(Long id, ParserConfig config) {
        // 1. 获取现有配置
        ParserConfig existing = repository.findById(id)
                .orElseThrow(() -> new BusinessException("解析器配置不存在"));

        // 2. 内置校验
        if (Boolean.TRUE.equals(existing.getIsBuiltIn())) {
            throw new BusinessException("内置解析器不支持编辑");
        }

        // 3. 状态校验：只有草稿（DRAFT）状态才允许修改
        if (existing.getStatus() != ConfigStatusEnum.DRAFT) {
            throw new BusinessException("已发布的解析器不可直接修改，请先取消发布");
        }

        // 4. 更新可修改字段（使用防呆策略：仅更新传入的非空字段）
        if (StringUtils.hasText(config.getName())) {
            existing.setName(config.getName());
        }
        if (StringUtils.hasText(config.getFileType())) {
            existing.setFileType(config.getFileType());
        }
        if (StringUtils.hasText(config.getEncoding())) {
            existing.setEncoding(config.getEncoding());
        }
        if (config.getSkipRows() != null) {
            existing.setSkipRows(config.getSkipRows());
        }
        if (StringUtils.hasText(config.getMetadataRules())) {
            existing.setMetadataRules(config.getMetadataRules());
        }
        if (StringUtils.hasText(config.getFieldMappingRules())) {
            existing.setFieldMappingRules(config.getFieldMappingRules());
        }
        if (StringUtils.hasText(config.getScript())) {
            existing.setScript(config.getScript());
        }
        if (StringUtils.hasText(config.getPostScript())) {
            existing.setPostScript(config.getPostScript());
        }
        if (StringUtils.hasText(config.getPostScriptLanguage())) {
            existing.setPostScriptLanguage(config.getPostScriptLanguage());
        }
        if (config.getPostScriptEnabled() != null) {
            existing.setPostScriptEnabled(config.getPostScriptEnabled());
        }
        // 注意：channel、status、isBuiltIn 不允许通过编辑接口变更
        // 需要变更渠道需走专门接口，状态变更需走发布/取消发布接口

        // 5. 保存更新
        repository.save(existing);
    }

    /**
     * 取消发布（将已发布配置降为草稿，以便编辑）
     */
    @Transactional
    public void unpublish(Long id) {
        ParserConfig config = repository.findById(id)
                .orElseThrow(() -> new BusinessException("解析器配置不存在"));
        if (Boolean.TRUE.equals(config.getIsBuiltIn())) {
            throw new BusinessException("内置解析器不允许取消发布");
        }
        if (config.getStatus() != ConfigStatusEnum.PUBLISHED) {
            throw new BusinessException("只有已发布的解析器才能取消发布");
        }
        config.setStatus(ConfigStatusEnum.DRAFT);
        repository.save(config);
    }

    /**
     * 发布草稿（使其生效）
     */
    @Transactional
    public void publish(Long id) {
        ParserConfig config = repository.findById(id)
                .orElseThrow(() -> new BusinessException("解析器配置不存在"));
        if (config.getStatus() != ConfigStatusEnum.DRAFT) {
            throw new BusinessException("只有草稿状态的解析器才能发布");
        }
        config.setStatus(ConfigStatusEnum.PUBLISHED);
        repository.save(config);
    }
}
