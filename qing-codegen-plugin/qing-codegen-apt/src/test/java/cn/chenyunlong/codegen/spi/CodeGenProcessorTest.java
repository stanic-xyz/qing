/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.codegen.spi;

import junit.framework.TestCase;

import java.util.ServiceLoader;

public class CodeGenProcessorTest extends TestCase {

    public void testSpi() {
        ServiceLoader<CodeGenProcessor> genProcessors = ServiceLoader.load(CodeGenProcessor.class);
        for (CodeGenProcessor genProcessor : genProcessors) {
            System.out.println("genProcessor = " + genProcessor);
        }
    }

}
