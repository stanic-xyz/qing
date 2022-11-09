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
     * vo包名称
     */
    private String voPackageName;

    private String voClassName;

    private String queryPackageName;

    private String queryClassName;

    private String updaterPackageName;

    private String updaterClassName;

    private String creatorPackageName;

    private String creatorClassName;

    private String mapperPackageName;

    private String mapperClassName;

    private String repositoryPackageName;

    private String repositoryClassName;

    private String servicePackageName;

    private String serviceClassName;

    private String implPackageName;

    private String implClassName;

    private String controllerPackageName;

    private String controllerClassName;

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

}
