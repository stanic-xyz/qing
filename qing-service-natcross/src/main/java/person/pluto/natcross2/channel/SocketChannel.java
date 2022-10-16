package person.pluto.natcross2.channel;

import java.io.IOException;
import java.net.Socket;

/**
 * <p>
 * socket通道
 * </p>
 *
 * @param <R> 读取返回的类型
 * @param <W> 写入的类型
 * @author Pluto
 * @since 2020-01-08 16:19:51
 */
public abstract class SocketChannel<R, W> implements Channel<R, W> {

    /**
     * 获取socket
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 16:20:28
     */
    abstract public Socket getSocket();

    /**
     * 设置socket
     *
     * @param socket
     * @throws IOException
     * @author Pluto
     * @since 2020-01-08 16:20:37
     */
    abstract public void setSocket(Socket socket) throws IOException;

    /**
     * 关闭socket
     *
     * @throws IOException
     * @author Pluto
     * @since 2020-01-08 16:20:45
     */
    abstract public void closeSocket() throws IOException;

    @Override
    public void close() throws IOException {
        this.closeSocket();
    }

}
