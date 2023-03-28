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

package cn.chenyunlong.qing.conf;

import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ant path matcher test.
 *
 * @author Stan
 */
class AntPathMatcherTest {

    final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Test
    void matchTest() {
        assertFalse(pathMatcher.match("/admin/?*/**", "/admin"));
        assertFalse(pathMatcher.match("/admin/?*/**", "/admin/"));

        assertTrue(pathMatcher.match("/admin/?*/**", "/admin/index.html"));
        assertTrue(pathMatcher.match("/admin/?*/**", "/admin/index.html/more"));
    }
}
