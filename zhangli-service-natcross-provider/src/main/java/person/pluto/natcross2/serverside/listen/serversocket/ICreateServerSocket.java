package person.pluto.natcross2.serverside.listen.serversocket;

import java.net.ServerSocket;

/**
 * <p>
 * 创建服务端口接口
 * </p>
 *
 * @author Pluto
 * @since 2020-01-09 13:28:12
 */
public interface ICreateServerSocket {

    /**
     * 创建监听服务
     *
     * @param listenPort 监听端口
     * @return
     * @author Pluto
     * @since 2020-01-09 13:34:16
     */
    ServerSocket createServerSocket(int listenPort) throws Exception;

}