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

package cn.chenyunlong.qing.domain.activity.mapper;

import cn.chenyunlong.qing.domain.activity.ActivityEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 活动信息Mapper
 *
 * @author Stan
 */
@Mapper
@Component
public interface ActivityMapper extends BaseMapper<ActivityEntity> {

    /**
     * 获取所有活动信息
     *
     * @return 活动信息列表
     */
    List<ActivityEntity> getActivityList();

    /**
     * 获取具体的活动信息
     *
     * @param activityId 活动ID
     * @return 活动信息
     */
    ActivityEntity getActivityById(Long activityId);
}
