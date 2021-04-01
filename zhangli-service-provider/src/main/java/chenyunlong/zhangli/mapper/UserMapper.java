package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * 用户信息
 *
 * @author Stan
 */
@Mapper
@Component
public interface UserMapper {

    /**
     * 根据ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户的权限信息
     */
    @Select("SELECT * FROM user WHERE userid = #{userId}")
    User selectById(@Param("userId") Integer userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("select * from user where username = #{username}")
    User findByUsername(@Param("username") String username);

    /**
     * 添加用户信息
     *
     * @param user 待添加的用户信息
     */
    @Insert("INSERT INTO `user`(`username`,`password`,`phone`,`open_id`,`email`)VALUES(#{username},#{password}},#{phone},#{openId},#{email})")
    void addUser(User user);

    /**
     * 根据用户的email获取用户信息
     *
     * @param email 用户的email
     * @return 用户信息
     */
    @Select("select * from user where email = #{email}")
    User findByEmail(@Param("email") String email);

    /**
     * 更新用户的密码
     *
     * @param user 更新后的用户信息
     */
    @Update("update user set password=#{user.password} where userid=#{user.userId}")
    void updatePassword(@Param("user") User user);
}
