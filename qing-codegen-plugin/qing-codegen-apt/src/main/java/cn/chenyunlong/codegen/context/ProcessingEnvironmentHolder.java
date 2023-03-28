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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * 处理环境持有人
 *
 * @author gim
 * 工具类上下文holder
 * @since 2022/10/25
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

    /**
     * 错误消息
     *
     * @param message   消息
     * @param element   错误的元素
     * @param exception 异常信息
     */
    public static void printErrorMessage(String message, Element element, Exception exception) {
        System.out.println(message);
        exception.printStackTrace();
        ENVIRONMENT.get().getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    /**
     * 输出提示信息
     *
     * @param message 消息
     */
    public static void printMessage(String message) {
        System.out.println(message);
        ENVIRONMENT.get().getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }
}
