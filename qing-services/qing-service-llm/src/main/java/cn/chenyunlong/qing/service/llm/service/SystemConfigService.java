package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.dedup.DedupConfig;
import cn.chenyunlong.qing.service.llm.dto.link.LinkConfig;
import cn.chenyunlong.qing.service.llm.entity.SystemConfig;
import cn.chenyunlong.qing.service.llm.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统通用配置管理服务。
 * <p>
 * 以 KV 形式存储去重、关联等功能模块的默认参数。
 * 各功能模块通过 {@link #getInt}, {@link #getBoolean}, {@link #getValue} 读取配置，
 * 运行时可通过管理端 {@link #setValue} 动态修改。
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigService {

    private final SystemConfigRepository systemConfigRepo;

    /**
     * 读取字符串配置值，键不存在时返回默认值。
     */
    public String getValue(String key, String defaultValue) {
        return systemConfigRepo.findByConfigKey(key)
                .map(SystemConfig::getConfigValue)
                .orElse(defaultValue);
    }

    /**
     * 读取整数配置值，键不存在或格式异常时返回默认值。
     */
    public int getInt(String key, int defaultValue) {
        try {
            String val = getValue(key, String.valueOf(defaultValue));
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            log.warn("系统配置 {} 的值不是有效整数", key);
            return defaultValue;
        }
    }

    /**
     * 读取布尔配置值，键不存在时返回默认值。
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String val = getValue(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(val);
    }

    /**
     * 设置配置值（不存在则创建，存在则更新）。
     */
    public void setValue(String key, String value, String description) {
        SystemConfig config = systemConfigRepo.findByConfigKey(key)
                .orElseGet(() -> {
                    SystemConfig c = new SystemConfig();
                    c.setConfigKey(key);
                    return c;
                });
        config.setConfigValue(value);
        if (description != null) {
            config.setDescription(description);
        }
        systemConfigRepo.save(config);
        log.info("系统配置更新: {}={}", key, value);
    }

    /**
     * 查询所有配置。
     */
    public List<SystemConfig> findAll() {
        return systemConfigRepo.findAll();
    }

    /**
     * 删除指定配置。
     */
    public void delete(Long id) {
        SystemConfig config = systemConfigRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("配置不存在"));
        systemConfigRepo.delete(config);
    }

    /**
     * 用系统配置填充 {@link DedupConfig} 中为 null 的字段。
     */
    public DedupConfig fillDefaults(DedupConfig config) {
        if (config == null) {
            config = new DedupConfig();
        }
        if (config.getTimeToleranceMinutes() == null) {
            config.setTimeToleranceMinutes(getInt("dedup.timeToleranceMinutes", 5));
        }
        if (config.getMatchMerchant() == null) {
            config.setMatchMerchant(getBoolean("dedup.matchMerchant", true));
        }
        if (config.getMatchDetail() == null) {
            config.setMatchDetail(getBoolean("dedup.matchDetail", false));
        }
        return config;
    }

    /**
     * 用系统配置填充 {@link LinkConfig} 中为 null 的字段。
     */
    public LinkConfig fillDefaults(LinkConfig config) {
        if (config == null) {
            config = new LinkConfig();
        }
        if (config.getTimeToleranceMinutes() == null) {
            config.setTimeToleranceMinutes(getInt("link.timeToleranceMinutes", 5));
        }
        if (config.getMatchMerchant() == null) {
            config.setMatchMerchant(getBoolean("link.matchMerchant", true));
        }
        return config;
    }
}
