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

package person.pluto.natcross2.serverside.listen;

import lombok.extern.slf4j.Slf4j;
import person.pluto.natcross2.serverside.listen.config.IListenServerConfig;

import java.util.*;

/**
 * <p>
 * 转发监听服务控制类
 * </p>
 *
 * @author Pluto
 * @since 2019-07-05 11:25:44
 */
@Slf4j
public class ListenServerControl {

    private static final Map<Integer, ServerListenThread> SERVER_LISTEN_MAP = new HashMap<>();

    /**
     * * 加入新的监听服务进程
     *
     * @param serverListen 监听转发服务进程（句柄类似的东西）
     * @return 是否创建成功
     * @author Pluto
     * @since 2019-07-18 18:36:25
     */
    public static boolean add(ServerListenThread serverListen) {
        if (serverListen == null) {
            return false;
        }

        Integer listenPort = serverListen.getListenPort();
        ServerListenThread serverListenThread = SERVER_LISTEN_MAP.get(listenPort);
        if (serverListenThread != null) {
            // 必须要先remove掉才能add，讲道理如果原先的存在应该直接报错才对，也就是参数为null，所以这里不自动remove
            return false;
        }

        SERVER_LISTEN_MAP.put(listenPort, serverListen);
        return true;
    }

    /**
     * * 去除指定端口的监听服务端口
     *
     * @param listenPort 需要监听的端口
     * @return 是否移除成功了
     * @author Pluto
     * @since 2019-07-18 18:36:35
     */
    public static boolean remove(Integer listenPort) {
        ServerListenThread serverListenThread = SERVER_LISTEN_MAP.get(listenPort);
        if (serverListenThread == null) {
            return true;
        }

        serverListenThread.cancel();
        SERVER_LISTEN_MAP.remove(listenPort);

        return true;
    }

    /**
     * 根据端口获取监听服务端口
     *
     * @param listenPort 监听端口
     * @author Pluto
     * @since 2019-07-18 18:36:52
     */
    public static ServerListenThread get(Integer listenPort) {
        return SERVER_LISTEN_MAP.get(listenPort);
    }

    /**
     * * 获取全部监听服务
     *
     * @return 所有监听进程
     * @author Pluto
     * @since 2019-07-19 15:31:55
     */
    public static List<ServerListenThread> getAll() {
        List<ServerListenThread> list = new LinkedList<>();
        SERVER_LISTEN_MAP.forEach((key, value) -> list.add(value));
        return list;
    }

    /**
     * * 关闭所有监听服务
     *
     * @author Pluto
     * @since 2019-07-18 19:00:54
     */
    public static void closeAll() {
        Set<Integer> keySet = SERVER_LISTEN_MAP.keySet();
        Integer[] array = keySet.toArray(new Integer[0]);
        for (Integer key : array) {
            remove(key);
        }
    }

    /**
     * 创建新的监听服务
     *
     * @param config 配置信息
     * @return 返回监听信息
     */
    public static ServerListenThread createNewListenServer(IListenServerConfig config) {
        ServerListenThread serverListenThread;
        try {
            serverListenThread = new ServerListenThread(config);
        } catch (Exception e) {
            log.error("create listen server faild", e);
            return null;
        }
        // 若没有报错则说明没有监听该端口的线程，即不可正常使用原有端口，所以先进行强行remove，再进行add
        ListenServerControl.remove(config.getListenPort());
        ListenServerControl.add(serverListenThread);
        return serverListenThread;
    }

}
