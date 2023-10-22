/*
 * Copyright (c) 2019-2023  YunLong Chen
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

package cn.chenyunlong.qing.config;

import cn.chenyunlong.qing.infrastructure.config.properties.WxMpProperties;
import jakarta.annotation.Resource;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置类。
 *
 * @author 陈云龙
 */
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class MpConfig implements InitializingBean {

    @Resource
    private WxMpProperties wxMpProperties;

    @Override
    public void afterPropertiesSet() {

    }

    /**
     * WxMpService多个实现类 声明一个实例
     */
    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    /**
     * 微信客户端配置存储
     */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        // 设置微信公众号appId
        configStorage.setAppId(wxMpProperties.getAppId());
        // 设置微信公众号appSecret
        configStorage.setSecret(wxMpProperties.getSecret());
        // 设置微信公众号的token
        configStorage.setToken(wxMpProperties.getToken());
        // 设置微信公众号的EncodingAESKey
        configStorage.setAesKey(wxMpProperties.getAesKey());
        return configStorage;
    }
}
