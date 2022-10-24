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
 * @author gim
 * 工具类上下文holder
 */
public final class ProcessingEnvironmentHolder {

    public static final ThreadLocal<ProcessingEnvironment> environment = new ThreadLocal<>();

    public static ProcessingEnvironment getEnvironment() {
        return environment.get();
    }

    public static void setEnvironment(ProcessingEnvironment pe) {
        environment.set(pe);
    }


}
