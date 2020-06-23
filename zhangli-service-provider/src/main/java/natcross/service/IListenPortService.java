package natcross.service;

import natcross.entity.ListenPort;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Pluto
 * @since 2019-07-22 13:55:39
 */
@Mapper
public interface IListenPortService {


    int count(QueryWrapper<ListenPort> queryWrapper);
}
