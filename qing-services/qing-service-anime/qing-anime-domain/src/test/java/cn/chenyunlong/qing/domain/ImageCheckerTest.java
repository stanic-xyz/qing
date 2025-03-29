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

package cn.chenyunlong.qing.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ImageCheckerTest {

    @Test
    @DisplayName("测试生成日期")
    void generateDate() {
        LocalDateTime localDateTime = LocalDateTime.of(2005, 12, 30, 0, 0, 0);
        for (int i = 0; i < 5; i++) {
            localDateTime = localDateTime.plusDays(1);
            String str = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("511303" + str + "5142 \t " + format);
        }
    }
}
