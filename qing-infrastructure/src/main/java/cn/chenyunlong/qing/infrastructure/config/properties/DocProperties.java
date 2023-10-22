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

package cn.chenyunlong.qing.infrastructure.config.properties;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 陈云龙
 */
@Data
@Component
@ConfigurationProperties(prefix = "qing.api")
public class DocProperties {

    /**
     * 是否启用openDoc
     */
    private boolean docDisabled;

    private String title;
    private String description;
    private String version;
    private String author;
    private String url;
    private String email;
    private String license;


    private String licenseUrl;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 具体的服务的URL
     */
    private List<ServerInfo> infoList;

    @Data
    public static class ServerInfo {

        private String url;

        private String description;
    }
}
