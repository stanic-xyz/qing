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
 */

package cn.chenyunlong.zhangli.service.impl;

import cn.chenyunlong.zhangli.mapper.ActivityMapper;
import cn.chenyunlong.zhangli.model.entities.ActivityEntity;
import cn.chenyunlong.zhangli.service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }


    @Override
    public List<ActivityEntity> queryActivitiesByPage(String keyWorld) {
        return activityMapper.getActivityList();
    }

    @Override
    public ActivityEntity getActivityById(Long activityId) {
        return activityMapper.getActivityById(activityId);
    }

    @Override
    public void addActivity(ActivityEntity activity) {
        activityMapper.insert(activity);
    }
}
