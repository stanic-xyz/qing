package cn.chenyunlong.zhangli.mapper;


import cn.chenyunlong.zhangli.model.entities.Sign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author Stan
 */
@Mapper
@Component
public interface SignMapper extends BaseMapper<Sign> {

    /**
     * 获取签到信息
     *
     * @param userId 用户ID
     * @return 签到数量
     */
    @Select("SELECT COUNT(*) FROM t_sign_record WHERE user_id=#{userId}")
    public int getSignState(Integer userId);

    /**
     * 添加签到信息
     *
     * @param sign 签到信息
     */
    @Insert("INSERT INTO `zhangli`.`t_sign_record` (`user_id`,`date_month`,`mask`,`continue_sign_month`) VALUES(#{userId}, #{dateMonth}, #{mask}, #{continueSignMonth})")
    void add2(Sign sign);

    /**
     * 添加签到信息
     *
     * @param sign 签到信息
     */
    void add(Sign sign);
}
