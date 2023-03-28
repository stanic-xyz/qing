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

package person.pluto.natcross2.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 * <p>
 * MD5散列签名
 * </p>
 *
 * @author Stan
 * @since 2020-01-08 10:13:50
 */
public final class MD5Signature {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final String RANDOM_BASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    private static final String HEX_BASE = "0123456789abcdef";


    /**
     * 隐藏public构造器
     */
    private MD5Signature() {

    }

    /**
     * 转换为16进制字符
     *
     * @param bytes 字节
     * @return {@link String}
     * @author Stan
     * @since 2019-12-05 12:34:48
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder(bytes.length << 1);
        for (byte tmp : bytes) {
            stringBuffer.append(HEX_BASE.charAt(tmp >> 4 & 0xf));
            stringBuffer.append(HEX_BASE.charAt(tmp & 0xf));
        }
        return stringBuffer.toString();
    }

    /**
     * 获取随机数
     *
     * @param count 字符长度
     * @return {@link String}
     * @author Stan
     * @since 2019-12-05 11:20:35
     */
    public static String getRandomStr(int count) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; ++i) {
            sb.append(RANDOM_BASE.charAt(RANDOM.nextInt(RANDOM_BASE.length())));
        }
        return sb.toString();
    }

    /**
     * int,字节
     * integer 转换为 byte[]
     *
     * @param source 需要转化的int类型对象
     * @return {@link byte[]}
     * @author Stan
     * @since 2019-12-05 11:20:53
     */
    public static byte[] intToBytes(int source) {
        return new byte[]{(byte) ((source >> 24) & 0xFF), (byte) ((source >> 16) & 0xFF),
                (byte) ((source >> 8) & 0xFF), (byte) (source & 0xFF)};
    }

    /**
     * bytes2int
     * byte[] 转 integer
     *
     * @param byteArr 需要转化的byte数组
     * @return int
     * @author Stan
     * @since 2019-12-05 11:21:07
     */
    public static int bytes2int(byte[] byteArr) {
        int count = 0;

        for (int i = 0; i < 4; ++i) {
            count <<= 8;
            count |= byteArr[i] & 255;
        }

        return count;
    }

    /**
     * 对参数进行签名
     *
     * @param params 参数个数
     * @return {@link String}
     */
    public static String getSignature(String... params) {
        return getSignature(CHARSET, params);
    }

    /**
     * 对参数进行MD5散列签名
     *
     * @param charset 字符集
     * @param params  参数个数
     * @return {@link String}
     * @author Stan
     * @since 2019-12-05 12:35:52
     */
    public static String getSignature(Charset charset, String... params) {
        Arrays.sort(params);
        StringBuilder stringBuffer = new StringBuilder();

        for (int i = 0; i < 4; ++i) {
            stringBuffer.append(params[i]);
        }

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        byte[] digest = md.digest(stringBuffer.toString().getBytes(charset));

        return toHexString(digest);
    }

}
