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

package cn.chenyunlong.codegen.processor;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * 默认名称名称上下文，便于其他引入类方便使用
 *
 * @author Gim
 * @since 2022/11/06
 */
@Data
public class DefaultNameContext {


    /**
     * 根包
     */
    private String basePackage = "";

    //vo包名称
    private String voClassName;
    private String voPackageName;
    private String queryClassName;
    private String updaterPackageName;
    private String queryPackageName;
    private String implClassName;

    private String updaterClassName;
    private String controllerClassName;
    private String creatorPackageName;
    private String creatorClassName;
    private String mapperPackageName;
    private String mapperClassName;
    private String repositoryPackageName;
    private String repositoryClassName;
    private String servicePackageName;
    private String serviceClassName;
    private String implPackageName;
    private String controllerPackageName;
    /**
     * API 相关
     */
    private String createPackageName;
    private String createClassName;
    private String updatePackageName;
    private String updateClassName;
    private String queryRequestPackageName;
    private String queryRequestClassName;
    private String responsePackageName;
    private String responseClassName;
    private String feignPackageName;
    private String feignClassName;

    public String getVoPackageName() {
        return getFinalPackage(voPackageName);
    }

    public String getQueryPackageName() {
        return getFinalPackage(queryPackageName);
    }

    public String getUpdaterPackageName() {
        return getFinalPackage(updaterPackageName);
    }

    public String getCreatorPackageName() {
        return getFinalPackage(creatorPackageName);
    }

    public String getMapperPackageName() {
        return getFinalPackage(mapperPackageName);
    }

    public String getRepositoryPackageName() {
        return getFinalPackage(repositoryPackageName);
    }

    public String getServicePackageName() {
        return getFinalPackage(servicePackageName);
    }

    public String getImplPackageName() {
        return getFinalPackage(implPackageName);
    }

    public String getControllerPackageName() {
        return getFinalPackage(controllerPackageName);
    }

    public String getCreatePackageName() {
        return getFinalPackage(createPackageName);
    }

    public String getUpdatePackageName() {
        return getFinalPackage(updatePackageName);
    }

    public String getQueryRequestPackageName() {
        return getFinalPackage(queryRequestPackageName);
    }

    public String getResponsePackageName() {
        return getFinalPackage(responsePackageName);
    }

    public String getFeignPackageName() {
        return getFinalPackage(feignPackageName);
    }

    private String getFinalPackage(String relativePackage) {
        return !Strings.isNullOrEmpty(basePackage) ? basePackage + "." + relativePackage : relativePackage;
    }
}
