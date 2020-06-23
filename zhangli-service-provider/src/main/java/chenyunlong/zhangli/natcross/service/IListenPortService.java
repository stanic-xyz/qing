package chenyunlong.zhangli.natcross.service;

import chenyunlong.zhangli.natcross.entity.ListenPort;

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


    int count(QueryWrapper<ListenPort> queryWrapper);

    boolean save(ListenPort listenPort);

    void removeById(Integer listenPort);

    List<ListenPort> list(QueryWrapper<ListenPort> queryWrapper);

    ListenPort getById(Integer listenPort);

    boolean updateById(ListenPort listenPort);
}
