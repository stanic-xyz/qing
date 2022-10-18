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

package person.pluto.natcross2.channel;

import com.alibaba.fastjson.JSONObject;
import lombok.*;
import lombok.EqualsAndHashCode.Exclude;
import person.pluto.natcross2.model.InteractiveModel;
import person.pluto.natcross2.model.SecretInteractiveModel;
import person.pluto.natcross2.utils.AESUtil;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.Key;

/**
 * <p>
 * InteractiveModel 加密型通道，AES加密
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:16:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SecretInteractiveChannel extends SocketChannel<InteractiveModel, InteractiveModel> {

    @Exclude
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private JsonChannel channle;

    /**
     * 签名混淆key
     */
    private String tokenKey;
    /**
     * aes密钥
     */
    private Key aesKey;
    /**
     * 超时时间，毫秒
     */
    private Long overtimeMills = 5000L;

    public SecretInteractiveChannel() {
        this.channle = new JsonChannel();
    }

    public SecretInteractiveChannel(Socket socket) throws IOException {
        this.channle = new JsonChannel(socket);
    }

    @Override
    public InteractiveModel read() throws Exception {
        JSONObject read = channle.read();
        SecretInteractiveModel secretInteractiveModel = read.toJavaObject(SecretInteractiveModel.class);
        if (Math.abs(System.currentTimeMillis() - secretInteractiveModel.getTimestamp()) > overtimeMills) {
            throw new IllegalStateException("超时");
        }
        boolean checkAutograph = secretInteractiveModel.checkAutograph(tokenKey);
        if (!checkAutograph) {
            throw new IllegalStateException("签名错误");
        }
        secretInteractiveModel.decryptMsg(aesKey);
        return secretInteractiveModel;
    }

    @Override
    public void write(InteractiveModel value) throws Exception {
        SecretInteractiveModel secretInteractiveModel = new SecretInteractiveModel(value);
        secretInteractiveModel.setCharset(this.getCharset().name());
        secretInteractiveModel.fullMessage(aesKey, tokenKey);
        channle.write(secretInteractiveModel);
    }

    @Override
    public void flush() throws Exception {
        channle.flush();
    }

    @Override
    public void writeAndFlush(InteractiveModel value) throws Exception {
        this.write(value);
        this.flush();
    }

    /**
     * 获取charset
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 16:18:58
     */
    public Charset getCharset() {
        return channle.getCharset();
    }

    @Override
    public void setCharset(Charset charset) {
        channle.setCharset(charset);
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

    /**
     * 使用base64格式设置aes密钥
     *
     * @param aesKey
     * @author Pluto
     * @since 2020-01-08 16:19:07
     */
    public void setBaseAesKey(String aesKey) {
        this.aesKey = AESUtil.createKeyByBase64(aesKey);
    }

}
