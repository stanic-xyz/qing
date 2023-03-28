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

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 通用性工具集
 * </p>
 */
public final class Tools {

    /**
     * 删除重复
     * 去除list中的重复元素
     *
     * @param array 数组
     * @return {@link List}<{@link T}>
     */
    public static <T> List<T> removeRepeat(List<T> array) {
        if (array == null) {
            return null;
        }
        Set<T> set = new HashSet<>();
        List<T> newList = new ArrayList<>();
        for (T model : array) {
            if (set.add(model)) {
                newList.add(model);
            }
        }
        return newList;
    }

    /**
     * 输入输出
     * 输入流转输出流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @throws IOException ioexception
     */
    public static void inputToOutput(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] temp = new byte[4096];
        int len = -1;
        while ((len = inputStream.read(temp)) != -1) {
            outputStream.write(temp, 0, len);
        }
        outputStream.flush();
    }

    /**
     * unicode转中文
     *
     * @param str str
     * @return {@link String}
     */
    public static String unicodeToString(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char c;
        while (matcher.find()) {
            c = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), c + "");
        }
        return str;
    }

}
