package stan.zhangli.natcross.service;

import stan.zhangli.natcross.entity.ListenPort;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Pluto
 * @since 2019-07-22 13:55:39
 */
public interface IListenPortService {


    int count();

    boolean save(ListenPort listenPort);

    void removeById(Integer listenPort);

    List<ListenPort> list();

    ListenPort getByListenPort(Integer listenPort);

    boolean updateById(ListenPort listenPort);
}