package stan.zhangli.natcross.mapper;

import stan.zhangli.natcross.entity.ListenPort;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Pluto
 * @since 2019-07-22 13:55:39
 */
@Mapper
public interface ListenPortMapper {

    int count();

    List<ListenPort> list();
}
