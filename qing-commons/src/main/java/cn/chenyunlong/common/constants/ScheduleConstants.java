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

package cn.chenyunlong.common.constants;

import lombok.Getter;

/**
 * 任务调度通用常量。
 *
 * @author ruoyi
 */
@SuppressWarnings("unused")
public class ScheduleConstants {

    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /**
     * 执行目标key。
     */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /**
     * 默认。
     */
    public static final String MISFIRE_DEFAULT = "0";

    /**
     * 立即触发执行。
     */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /**
     * 触发一次执行。
     */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /**
     * 不触发立即执行。
     */
    public static final String MISFIRE_DO_NOTHING = "3";

    @Getter
    public enum Status {

        /**
         * 正常。
         */
        NORMAL("0"),
        /**
         * 暂停。
         */
        PAUSE("1");

        private final String value;

        Status(String value) {
            this.value = value;
        }

    }
}
