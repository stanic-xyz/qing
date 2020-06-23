package chenyunlong.zhangli.natcross.service.impl;

import chenyunlong.zhangli.natcross.entity.ListenPort;
import chenyunlong.zhangli.natcross.mapper.ListenPortMapper;
import chenyunlong.zhangli.natcross.service.IListenPortService;
import chenyunlong.zhangli.natcross.service.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

@Resource
public class IListenPortServiceImpl implements IListenPortService {

    @Autowired
    private ListenPortMapper listenPortMapper;

    @Override
    public int count(QueryWrapper<ListenPort> queryWrapper) {
        return listenPortMapper.count();
    }

    @Override
    public boolean save(ListenPort listenPort) {
        return false;
    }

    @Override
    public void removeById(Integer listenPort) {

    }

    @Override
    public List<ListenPort> list(QueryWrapper<ListenPort> queryWrapper) {
        return null;
    }

    @Override
    public ListenPort getById(Integer listenPort) {
        return null;
    }

    @Override
    public boolean updateById(ListenPort listenPort) {
        return false;
    }
}
