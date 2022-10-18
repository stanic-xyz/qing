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

package person.pluto.natcross2.common;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 公用的格式化类
 * </p>
 *
 * @author Pluto
 * @since 2019-07-05 13:35:04
 */
public class CommonFormat {

    /**
     * 获取socket匹配对key
     *
     * @param listenPort
     * @return
     * @author Pluto
     * @since 2019-07-17 09:35:10
     */
    public static String getSocketPartKey(Integer listenPort) {
        DecimalFormat fiveLenFormat = new DecimalFormat("00000");
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String randomNum = RandomStringUtils.randomNumeric(4);
        return "SK-" + fiveLenFormat.format(listenPort) + "-" + dateTime + "-" + randomNum;
    }

    /**
     * 根据socketPartKey获取端口号
     *
     * @param socketPartKey
     * @return
     * @author Pluto
     * @since 2019-07-17 11:39:50
     */
    public static Integer getSocketPortByPartKey(String socketPartKey) {
        String[] split = socketPartKey.split("-");
        return Integer.valueOf(split[1]);
    }

    /**
     * 获取交互流水号
     *
     * @return
     * @author Pluto
     * @since 2019-07-17 09:35:29
     */
    public static String getInteractiveSeq() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String randomNum = RandomStringUtils.randomNumeric(4);
        return "IS-" + dateTime + "-" + randomNum;
    }

}
