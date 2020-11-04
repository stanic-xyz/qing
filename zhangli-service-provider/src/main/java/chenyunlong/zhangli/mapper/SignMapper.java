package chenyunlong.zhangli.mapper;


import chenyunlong.zhangli.entities.Sign;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SignMapper {

    @Select("SELECT COUNT(*) FROM t_sign_record WHERE user_id=#{userId}")
    public int getSignState(Integer userId);

    @Insert("INSERT INTO `zhangli`.`t_sign_record` (`user_id`,`date_month`,`mask`,`continue_sign_month`) VALUES(#{userId}, #{dateMonth}, #{mask}, #{continueSignMonth})")
    void Add2(Sign sign);

    void Add(Sign sign);
}
