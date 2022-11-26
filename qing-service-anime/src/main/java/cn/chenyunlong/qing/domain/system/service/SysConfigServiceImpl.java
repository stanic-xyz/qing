/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.system.service;

import cn.chenyunlong.qing.domain.system.SysConfig;
import cn.chenyunlong.qing.domain.system.mapper.SysConfigMapper;
import cn.chenyunlong.qing.infrastructure.constant.Constants;
import cn.chenyunlong.qing.infrastructure.constant.UserConstants;
import cn.chenyunlong.qing.infrastructure.exception.BusinessException;
import cn.chenyunlong.qing.infrastructure.utils.CacheUtils;
import cn.chenyunlong.qing.infrastructure.utils.StringUtils;
import cn.chenyunlong.qing.infrastructure.utils.text.Convert;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    private final SysConfigMapper configMapper;

    public SysConfigServiceImpl(SysConfigMapper configMapper) {
        this.configMapper = configMapper;
    }

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
//        List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        List<SysConfig> configsList = Collections.emptyList();
        for (SysConfig config : configsList) {
            CacheUtils.put(getCacheName(), getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfig selectConfigById(Long configId) {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(CacheUtils.get(getCacheName(), getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig)) {
            CacheUtils.put(getCacheName(), getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config) {
        int row = configMapper.insertConfig(config);
        if (row > 0) {
            CacheUtils.put(getCacheName(), getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config) {
        int row = configMapper.updateConfig(config);
        if (row > 0) {
            CacheUtils.put(getCacheName(), getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数配置对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteConfigByIds(String ids) {
        Long[] configIds = Convert.toLongArray(ids);
        for (Long configId : configIds) {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new BusinessException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
        }
        int count = configMapper.deleteConfigByIds(Convert.toStrArray(ids));
        if (count > 0) {

            CacheUtils.removeAll(getCacheName());
        }
        return count;
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache() {
        CacheUtils.removeAll(getCacheName());
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config) {
        long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId() != configId) {
            return UserConstants.CONFIG_KEY_NOT_UNIQUE;
        }
        return UserConstants.CONFIG_KEY_UNIQUE;
    }

    /**
     * 获取cache name
     *
     * @return 缓存名
     */
    private String getCacheName() {
        return Constants.SYS_CONFIG_CACHE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return Constants.SYS_CONFIG_KEY + configKey;
    }
}
