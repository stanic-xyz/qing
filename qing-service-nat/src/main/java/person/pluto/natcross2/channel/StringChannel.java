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

package person.pluto.natcross2.channel;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 字符型通道
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:21:31
 */
public class StringChannel extends SocketChannel<String, String> {

    private LengthChannel channle;

    private Charset charset = StandardCharsets.UTF_8;

    public StringChannel() {
        channle = new LengthChannel();
    }

    public StringChannel(Socket socket) throws IOException {
        this.channle = new LengthChannel(socket);
    }

    @Override
    public String read() throws Exception {
        byte[] read = channle.read();
        return new String(read, charset);
    }

    @Override
    public void write(String value) throws Exception {
        channle.write(value.getBytes(charset));
    }

    @Override
    public void flush() throws Exception {
        channle.flush();
    }

    @Override
    public void writeAndFlush(String value) throws Exception {
        this.write(value);
        this.flush();
    }

    /**
     * 获取charset
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 16:22:06
     */
    public Charset getCharset() {
        return charset;
    }

    @Override
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public Socket getSocket() {
        return channle.getSocket();
    }

    @Override
    public void setSocket(Socket socket) throws IOException {
        channle.setSocket(socket);
    }

    @Override
    public void closeSocket() throws IOException {
        channle.closeSocket();
    }

}
