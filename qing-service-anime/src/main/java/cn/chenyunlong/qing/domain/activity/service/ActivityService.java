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

package cn.chenyunlong.qing.domain.activity.service;


import cn.chenyunlong.qing.domain.activity.ActivityEntity;

import java.util.List;

/**
 * @author Stan
 */
public interface ActivityService {


    /**
     * 获取所有动态记录
     *
     * @param keyWorld 活动关键字
     * @return 活动列表
     */
    List<ActivityEntity> queryActivitiesByPage(String keyWorld);

    /**
     * 获取活动信息
     *
     * @param activityId 活动信息ID
     * @return 活动信息详情
     */
    ActivityEntity getActivityById(Long activityId);

    /**
     * 添加活动信息
     *
     * @param activity 需要添加的活动信息
     */
    void addActivity(ActivityEntity activity);

}
