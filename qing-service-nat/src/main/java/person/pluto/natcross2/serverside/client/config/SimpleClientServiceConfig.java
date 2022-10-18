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

package person.pluto.natcross2.serverside.client.config;

import lombok.NoArgsConstructor;
import person.pluto.natcross2.channel.InteractiveChannel;
import person.pluto.natcross2.channel.SocketChannel;
import person.pluto.natcross2.model.InteractiveModel;
import person.pluto.natcross2.serverside.client.adapter.DefaultReadAheadPassValueAdapter;
import person.pluto.natcross2.serverside.client.adapter.IClientServiceAdapter;

import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 简单交互的客户端服务配置
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:45:47
 */
@NoArgsConstructor
public class SimpleClientServiceConfig implements IClientServiceConfig<InteractiveModel, InteractiveModel> {

    private Integer listenPort;
    private IClientServiceAdapter clientServiceAdapter = new DefaultReadAheadPassValueAdapter(this);
    private Charset charset = StandardCharsets.UTF_8;

    public SimpleClientServiceConfig(Integer listenPort) {
        this.listenPort = listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    @Override
    public Integer getListenPort() {
        return listenPort;
    }

    @Override
    public ExecutorService newProcExecutorService() {
        return Executors.newCachedThreadPool();
    }

    /**
     * 设置适配器
     *
     * @param clientServiceAdapter
     * @author Pluto
     * @since 2020-01-09 16:19:16
     */
    public void setClientServiceAdapter(IClientServiceAdapter clientServiceAdapter) {
        this.clientServiceAdapter = clientServiceAdapter;
    }

    @Override
    public IClientServiceAdapter getClientServiceAdapter() {
        return clientServiceAdapter;
    }

    @Override
    public SocketChannel<? extends InteractiveModel, ? super InteractiveModel> newSocketChannel(Socket listenSocket)
            throws Exception {
        InteractiveChannel channel = new InteractiveChannel();
        channel.setSocket(listenSocket);
        channel.setCharset(charset);
        return channel;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    /**
     * 设置字符编码
     *
     * @param charset
     * @author Pluto
     * @since 2020-01-08 16:46:06
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

}
