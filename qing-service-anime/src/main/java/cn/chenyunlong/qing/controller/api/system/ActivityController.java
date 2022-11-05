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

package cn.chenyunlong.qing.controller.api.system;

import cn.chenyunlong.qing.core.ApiResult;
import cn.chenyunlong.qing.model.entities.ActivityEntity;
import cn.chenyunlong.qing.service.ActivityService;
import cn.chenyunlong.qing.service.AttachementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Stan
 */
@Tag(name = "活动信息")
@RestController
@RequestMapping("activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;
    private final AttachementService attachementService;

    /**
     * 获取所有的活动信息
     *
     * @return 活动信息列表
     */
    @GetMapping("list")
    public ApiResult<List<ActivityEntity>> listActivity(
            @RequestParam(value = "keyword", required = false) String keyword) {
        return ApiResult.success(activityService.queryActivitiesByPage(keyword));
    }

    /**
     * 添加动态信息
     *
     * @param activity 动态信息
     * @return 动态添加结果
     */
    @PostMapping
    @Operation(summary = "添加动态信息")
    public ApiResult<Object> addActivity(@RequestBody ActivityEntity activity) {
        activityService.addActivity(activity);
        return ApiResult.success();
    }


    /**
     * 获取动态的具体信息
     *
     * @param activityId 动态ID
     * @return 返回消息信息
     */
    @GetMapping("activity/{activityId:\\d+}")
    public ApiResult<ActivityEntity> getActivityDetail(@PathVariable(name = "activityId") Long activityId) {
        attachementService.getAttachementCount(activityId);
        return ApiResult.success(activityService.getActivityById(activityId));
    }
}
