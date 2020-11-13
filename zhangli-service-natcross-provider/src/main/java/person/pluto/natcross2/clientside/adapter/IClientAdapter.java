package person.pluto.natcross2.clientside.adapter;

/**
 * <p>
 * 客户端适配器
 * </p>
 *
 * @param <R> 处理的对象
 * @param <W> 可写的对象
 * @author Pluto
 * @since 2020-01-08 16:22:43
 */
public interface IClientAdapter<R, W> {

    /**
     * 请求建立控制器
     *
     * @return
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:22:53
     */
    boolean createControl() throws Exception;

    /**
     * 处理方法
     *
     * @param read
     * @author Pluto
     * @since 2020-01-08 16:23:09
     */
    void procMethod(R read);

    /**
     * 等待消息处理
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:23:33
     */
    void waitMessage() throws Exception;

    /**
     * 关闭
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:23:42
     */
    void close() throws Exception;

    /**
     * 向控制器发送心跳
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:23:49
     */
    void sendUrgentData() throws Exception;

}
