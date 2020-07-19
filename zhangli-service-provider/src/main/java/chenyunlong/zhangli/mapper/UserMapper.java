package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {

    @Select("SELECT * FROM user WHERE userid = #{userId}")
    User findByState(@Param("userId") String userId);

    @Select("select * from user where username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO `user`(`username`,`password`,`phone`,`open_id`,`email`)VALUES(#{username},#{password}},#{phone},#{openId},#{email})")
    void addUser(User user);
}
