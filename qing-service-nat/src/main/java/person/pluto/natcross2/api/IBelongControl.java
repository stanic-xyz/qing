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

package person.pluto.natcross2.api;

/**
 * <p>
 * 通知上次停止的统一类，为适应不同的类型进行不同的函数封装
 * </p>
 *
 * @author Pluto
 * @since 2019-07-12 08:39:25
 */
public interface IBelongControl {

    /**
     * 无标记通知
     *
     * @author Pluto
     * @since 2020-01-08 16:08:46
     */
    default void noticeStop() {
        // do no thing
    }

    /**
     * 有标记通知
     *
     * @param socketPartKey
     * @return
     * @author Pluto
     * @since 2020-01-08 16:08:55
     */
    default boolean stopSocketPart(String socketPartKey) {
        return true;
    }

}
