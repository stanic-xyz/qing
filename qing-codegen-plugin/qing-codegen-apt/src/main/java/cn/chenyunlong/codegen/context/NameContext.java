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

package cn.chenyunlong.codegen.context;

import com.google.common.base.Strings;

/**
 * 默认名称名称上下文，便于其他引入类方便使用
 *
 * @author Gim
 * @since 2022/11/06
 */
public class NameContext {


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

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getVoClassName() {
        return voClassName;
    }

    public void setVoClassName(String voClassName) {
        this.voClassName = voClassName;
    }

    public void setVoPackageName(String voPackageName) {
        this.voPackageName = voPackageName;
    }

    public String getQueryClassName() {
        return queryClassName;
    }

    public void setQueryClassName(String queryClassName) {
        this.queryClassName = queryClassName;
    }

    public void setUpdaterPackageName(String updaterPackageName) {
        this.updaterPackageName = updaterPackageName;
    }

    public void setQueryPackageName(String queryPackageName) {
        this.queryPackageName = queryPackageName;
    }

    public String getImplClassName() {
        return implClassName;
    }

    public void setImplClassName(String implClassName) {
        this.implClassName = implClassName;
    }

    public String getUpdaterClassName() {
        return updaterClassName;
    }

    public void setUpdaterClassName(String updaterClassName) {
        this.updaterClassName = updaterClassName;
    }

    public String getControllerClassName() {
        return controllerClassName;
    }

    public void setControllerClassName(String controllerClassName) {
        this.controllerClassName = controllerClassName;
    }

    public void setCreatorPackageName(String creatorPackageName) {
        this.creatorPackageName = creatorPackageName;
    }

    public String getCreatorClassName() {
        return creatorClassName;
    }

    public void setCreatorClassName(String creatorClassName) {
        this.creatorClassName = creatorClassName;
    }

    public void setMapperPackageName(String mapperPackageName) {
        this.mapperPackageName = mapperPackageName;
    }

    public String getMapperClassName() {
        return mapperClassName;
    }

    public void setMapperClassName(String mapperClassName) {
        this.mapperClassName = mapperClassName;
    }

    public void setRepositoryPackageName(String repositoryPackageName) {
        this.repositoryPackageName = repositoryPackageName;
    }

    public String getRepositoryClassName() {
        return repositoryClassName;
    }

    public void setRepositoryClassName(String repositoryClassName) {
        this.repositoryClassName = repositoryClassName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    public void setImplPackageName(String implPackageName) {
        this.implPackageName = implPackageName;
    }

    public void setControllerPackageName(String controllerPackageName) {
        this.controllerPackageName = controllerPackageName;
    }

    public void setCreatePackageName(String createPackageName) {
        this.createPackageName = createPackageName;
    }

    public String getCreateClassName() {
        return createClassName;
    }

    public void setCreateClassName(String createClassName) {
        this.createClassName = createClassName;
    }

    public void setUpdatePackageName(String updatePackageName) {
        this.updatePackageName = updatePackageName;
    }

    public String getUpdateClassName() {
        return updateClassName;
    }

    public void setUpdateClassName(String updateClassName) {
        this.updateClassName = updateClassName;
    }

    public void setQueryRequestPackageName(String queryRequestPackageName) {
        this.queryRequestPackageName = queryRequestPackageName;
    }

    public String getQueryRequestClassName() {
        return queryRequestClassName;
    }

    public void setQueryRequestClassName(String queryRequestClassName) {
        this.queryRequestClassName = queryRequestClassName;
    }

    public void setResponsePackageName(String responsePackageName) {
        this.responsePackageName = responsePackageName;
    }

    public String getResponseClassName() {
        return responseClassName;
    }

    public void setResponseClassName(String responseClassName) {
        this.responseClassName = responseClassName;
    }

    public void setFeignPackageName(String feignPackageName) {
        this.feignPackageName = feignPackageName;
    }

    public String getFeignClassName() {
        return feignClassName;
    }

    public void setFeignClassName(String feignClassName) {
        this.feignClassName = feignClassName;
    }

    public void init() {

    }
}
