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

package cn.chenyunlong.qing.config;

import cn.chenyunlong.qing.config.properties.WxProperties;
import com.riversoft.weixin.mp.base.AppSetting;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author stan
 */
@Configuration
@EnableConfigurationProperties(WxProperties.class)
public class MpConfig implements InitializingBean {

    @Resource
    private WxProperties wxProperties;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Bean
    public AppSetting getAppSetting() {
        return new AppSetting(wxProperties.getAppId(), wxProperties.getSecret());
    }
}
