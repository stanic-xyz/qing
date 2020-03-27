package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE userid = #{userId}")
    User findByState(@Param("userId") String userId);
}
