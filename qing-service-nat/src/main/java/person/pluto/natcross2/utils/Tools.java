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
 */

package person.pluto.natcross2.utils;

/**
 * <p>
 * 无归类的工具集
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:56:35
 */
public class Tools {

    /**
     * integer 转换为 byte[]
     *
     * @param source
     * @return
     * @author Pluto
     * @since 2019-12-05 11:20:53
     */
    public static byte[] intToBytes(int source) {
        return new byte[]{(byte) ((source >> 24) & 0xFF), (byte) ((source >> 16) & 0xFF),
                (byte) ((source >> 8) & 0xFF), (byte) (source & 0xFF)};
    }

    /**
     * byte[] 转 integer
     *
     * @param byteArr
     * @return
     * @author Pluto
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

}
