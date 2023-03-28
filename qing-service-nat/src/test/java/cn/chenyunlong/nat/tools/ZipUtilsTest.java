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

package cn.chenyunlong.nat.tools;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

class ZipUtilsTest {

    @AfterAll
    static void afterAll() throws IOException {
        File file = new File("test.7z");
        Files.deleteIfExists(Path.of(file.getPath()));
    }

    @BeforeEach
    void setUp() throws IOException {
        File file = new File("test.7z");
        Files.deleteIfExists(Path.of(file.getPath()));
    }

    @Test
    void toZip() {
        try (FileOutputStream outputStream = new FileOutputStream("test.7z")) {
            ZipUtils.toZip(Collections.emptyList(), outputStream);
        } catch (FileNotFoundException ignore) {
        } catch (IOException ioException) {
            System.out.println("发生了io异常");
            ioException.printStackTrace();
        }
    }
}
