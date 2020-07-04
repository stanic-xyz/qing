package stan.zhangli.natcross.service.impl;

import stan.zhangli.natcross.entity.ListenPort;
import stan.zhangli.natcross.mapper.ListenPortMapper;
import stan.zhangli.natcross.service.IListenPortService;
import stan.zhangli.natcross.service.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        return listenPortMapper.list();
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
