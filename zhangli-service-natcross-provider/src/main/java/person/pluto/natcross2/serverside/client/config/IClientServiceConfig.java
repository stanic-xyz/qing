package person.pluto.natcross2.serverside.client.config;

import person.pluto.natcross2.channel.SocketChannel;
import person.pluto.natcross2.serverside.client.adapter.IClientServiceAdapter;

import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * 客户端服务配置
 * </p>
 *
 * @param <R> 交互通道读取的类
 * @param <W> 交互通道可写的类
 * @author Pluto
 * @since 2020-01-08 16:43:17
 */
public interface IClientServiceConfig<R, W> {

    /**
     * 监听端口
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 16:43:51
     */
    Integer getListenPort();

    /**
     * 执行使用的线程池
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 16:43:59
     */
    ExecutorService newProcExecutorService();

    /**
     * 客户端适配器
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 16:44:14
     */
    IClientServiceAdapter getClientServiceAdapter();

    /**
     * 交互通道
     *
     * @param listenSocket
     * @return
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:44:23
     */
    SocketChannel<? extends R, ? super W> newSocketChannel(Socket listenSocket) throws Exception;

    /**
     * 字符集
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 16:44:31
     */
    Charset getCharset();

}
