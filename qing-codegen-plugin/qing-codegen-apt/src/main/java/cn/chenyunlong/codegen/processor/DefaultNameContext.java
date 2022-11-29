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

package cn.chenyunlong.codegen.processor;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * 默认名称名称上下文，便于其他引入类方便使用
 *
 * @author Gim
 * @date 2022/11/06
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

    public String getVoPackageName() {
        return getFinalPackage(voPackageName);
    }

    private String creatorPackageName;

    public String getQueryPackageName() {
        return getFinalPackage(queryPackageName);
    }

    private String creatorClassName;

    private String mapperPackageName;

    public String getUpdaterPackageName() {
        return getFinalPackage(updaterPackageName);
    }

    private String mapperClassName;

    private String repositoryPackageName;

    public String getCreatorPackageName() {
        return getFinalPackage(creatorPackageName);
    }

    private String repositoryClassName;

    private String servicePackageName;

    public String getMapperPackageName() {
        return getFinalPackage(mapperPackageName);
    }

    private String serviceClassName;

    public String getRepositoryPackageName() {
        return getFinalPackage(repositoryPackageName);
    }

    private String implPackageName;

    public String getServicePackageName() {
        return getFinalPackage(servicePackageName);
    }

    public String getImplPackageName() {
        return getFinalPackage(implPackageName);
    }

    private String controllerPackageName;

    public String getControllerPackageName() {
        return getFinalPackage(controllerPackageName);
    }

    /**
     * API 相关
     */
    private String createPackageName;

    public String getCreatePackageName() {
        return getFinalPackage(createPackageName);
    }

    private String createClassName;

    private String updatePackageName;

    public String getUpdatePackageName() {
        return getFinalPackage(updatePackageName);
    }

    private String updateClassName;

    private String queryRequestPackageName;

    public String getQueryRequestPackageName() {
        return getFinalPackage(queryRequestPackageName);
    }

    private String queryRequestClassName;


    private String responsePackageName;

    private String responseClassName;

    public String getResponsePackageName() {
        return getFinalPackage(responsePackageName);
    }

    private String feignPackageName;

    private String feignClassName;

    public String getFeignPackageName() {
        return getFinalPackage(feignPackageName);
    }

    private String getFinalPackage(String relativePackage) {
        return !Strings.isNullOrEmpty(basePackage) ? basePackage + "." + relativePackage : relativePackage;
    }
}
