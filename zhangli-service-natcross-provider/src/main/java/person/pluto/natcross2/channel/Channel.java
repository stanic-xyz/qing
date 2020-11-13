package person.pluto.natcross2.channel;

import java.io.Closeable;
import java.nio.charset.Charset;

/**
 * <p>
 * 读写通道
 * </p>
 *
 * @param <R> 读取返回的类型
 * @param <W> 写入的类型
 * @author Pluto
 * @since 2020-01-03 15:40:28
 */
public interface Channel<R, W> extends Closeable {

    /**
     * 简单的读取方式
     *
     * @return
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:09:22
     */
    R read() throws Exception;

    /**
     * 简单的写入
     *
     * @param value
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:09:32
     */
    void write(W value) throws Exception;

    /**
     * 刷新
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:09:46
     */
    void flush() throws Exception;

    /**
     * 简单的写入并刷新
     *
     * @param value
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:09:57
     */
    void writeAndFlush(W value) throws Exception;

    /**
     * 设置交互编码
     *
     * @param charset
     * @author Pluto
     * @since 2020-01-08 16:10:06
     */
    default public void setCharset(Charset charset) {
        throw new IllegalStateException("不支持的操作");
    }
}
