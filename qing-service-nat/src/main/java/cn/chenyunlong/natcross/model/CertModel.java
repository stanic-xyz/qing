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
 */

package cn.chenyunlong.natcross.model;

import lombok.*;

import java.io.File;

/**
 * <p>
 * 证书配置模型
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 11:45:28
 */
@Data
@EqualsAndHashCode
public class CertModel {

    /**
     * 证书存放基础路径
     */
    private String basePath;
    /**
     * 默认证书名（支持相对路径）
     */
    private String defaultCertName;
    /**
     * 默认证书密码（明文）
     */
    private String defaultCertPassword;

    public String formatCertPath(String certName) {
        return this.basePath + File.separator + certName;
    }

    public String formatDefaultCertPath() {
        return this.formatCertPath(this.defaultCertName);
    }

}
