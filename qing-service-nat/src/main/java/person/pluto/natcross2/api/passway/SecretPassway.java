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

package person.pluto.natcross2.api.passway;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import person.pluto.natcross2.api.IBelongControl;
import person.pluto.natcross2.api.secret.ISecret;
import person.pluto.natcross2.channel.LengthChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <p>
 * 加密型隧道，一侧加密，一侧原样输入、输出
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 15:55:29
 */
@Data
@Slf4j
public class SecretPassway implements Runnable {

    public static enum Mode {
        // 从无加密接受到加密输出
        noToSecret,
        // 从加密接受到无加密输出
        secretToNo
    }

    private boolean alive = false;

    private ISecret secret;

    private Mode mode;

    private IBelongControl belongControl;

    private int streamCacheSize = 4096;

    private Socket recvSocket;
    private Socket sendSocket;

    @Override
    public void run() {

        try {
            if (Mode.noToSecret.equals(mode)) {
                noToSecret();
            } else {
                secretToNo();
            }
        } catch (Exception e) {
            log.debug("one InputToOutputThread closed");
        }

        // 传输完成后退出
        this.cancell();
        if (belongControl != null) {
            belongControl.noticeStop();
        }
    }

    /**
     * 从加密侧输入，解密后输出到无加密侧
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 15:56:43
     */
    private void secretToNo() throws Exception {
        try (LengthChannel recvChannel = new LengthChannel(recvSocket);
             OutputStream outputStream = sendSocket.getOutputStream()) {
            while (alive) {
                byte[] read = recvChannel.read();
                if (read == null || read.length < 1) {
                    break;
                }

                byte[] decrypt = secret.decrypt(read);

                outputStream.write(decrypt);
                outputStream.flush();
            }
        }
    }

    /**
     * 从无加密侧，经过加密后输出到加密侧
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 15:57:09
     */
    private void noToSecret() throws Exception {
        try (InputStream inputStream = recvSocket.getInputStream();
             LengthChannel sendChannel = new LengthChannel(sendSocket)) {
            int len = -1;
            byte[] arrayTemp = new byte[streamCacheSize];

            while (alive && (len = inputStream.read(arrayTemp)) > 0) {
                byte[] encrypt = secret.encrypt(arrayTemp, 0, len);
                sendChannel.writeAndFlush(encrypt);
            }
        }
    }

    /**
     * 判断是否有效
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 15:57:41
     */
    public boolean isValid() {
        return alive;
    }

    /**
     * 退出
     *
     * @author Pluto
     * @since 2020-01-08 15:57:48
     */
    public void cancell() {
        this.alive = false;

        try {
            this.recvSocket.close();
            this.sendSocket.close();
        } catch (IOException e) {
            // do no thing
        }
    }

    /**
     * 启动
     *
     * @author Pluto
     * @since 2020-01-08 15:57:53
     */
    public void start() {
        if (!this.alive) {
            this.alive = true;
            PasswayControl.executePassway(this);
        }
    }

}
