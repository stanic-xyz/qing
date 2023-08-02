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
import javax.lang.model.element.AnnotationMirror;
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
     * 处理器。
     */
    public static final ThreadLocal<ProcessingEnvironment> PROCESSING_ENVIRONMENT;

    static {
        PROCESSING_ENVIRONMENT = new ThreadLocal<>();
    }

    public ProcessingEnvironmentHolder() {
    }

    /**
     * 获取工具类上下文。
     *
     * @return {@link ProcessingEnvironment}
     */
    public static ProcessingEnvironment getProcessingEnvironment() {
        return PROCESSING_ENVIRONMENT.get();
    }

    /**
     * 设置工具类上下文
     *
     * @param processingEnvironment 上下文
     */
    public static void setProcessingEnvironment(ProcessingEnvironment processingEnvironment) {
        PROCESSING_ENVIRONMENT.set(processingEnvironment);
    }


    /**
     * 输出提示信息
     *
     * @param message 消息
     */
    public static void printMessage(String message) {
        ProcessingEnvironment processingEnvironment = PROCESSING_ENVIRONMENT.get();
        if (processingEnvironment != null) {
            processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
        }
    }

    /**
     * 打印普通信息。
     *
     * @param msg 需要打印的消息
     */
    public static void log(String msg) {
        if (PROCESSING_ENVIRONMENT.get().getOptions().containsKey("debug")) {
            PROCESSING_ENVIRONMENT.get().getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
        }
    }

    /**
     * 打印警告信息。
     *
     * @param msg 警告信息
     */
    public static void warning(String msg, Element element, AnnotationMirror annotation) {
        PROCESSING_ENVIRONMENT.get().getMessager()
            .printMessage(Diagnostic.Kind.WARNING, msg, element, annotation);
    }

    /**
     * 打印错误信息。
     *
     * @param msg 错误信息
     */
    public static void error(String msg, Element element, AnnotationMirror annotation) {
        PROCESSING_ENVIRONMENT.get().getMessager()
            .printMessage(Diagnostic.Kind.ERROR, msg, element, annotation);
    }

    /**
     * 处理错误信息。
     *
     * @param msg 错误信息
     */
    public static void fatalError(String msg) {
        PROCESSING_ENVIRONMENT.get().getMessager()
            .printMessage(Diagnostic.Kind.ERROR, "FATAL ERROR: " + msg);
    }
}
