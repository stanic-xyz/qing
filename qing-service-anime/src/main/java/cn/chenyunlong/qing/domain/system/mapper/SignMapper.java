/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.system.mapper;


import cn.chenyunlong.qing.domain.sign.Sign;
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
