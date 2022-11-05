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

package cn.chenyunlong.codegen.context;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * 处理环境持有人
 *
 * @author gim
 * 工具类上下文holder
 * @date 2022/10/25
 */
public final class ProcessingEnvironmentHolder {

    /**
     * 环境
     */
    public static final ThreadLocal<ProcessingEnvironment> ENVIRONMENT = new ThreadLocal<>();

    /**
     * 获取工具类上下文
     *
     * @return {@link ProcessingEnvironment}
     */
    public static ProcessingEnvironment getEnvironment() {
        return ENVIRONMENT.get();
    }

    /**
     * 设置工具类上下文
     *
     * @param processingEnvironment 上下文
     */
    public static void setEnvironment(ProcessingEnvironment processingEnvironment) {
        ENVIRONMENT.set(processingEnvironment);
    }
}
