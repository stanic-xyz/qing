package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.entities.Activity;
import chenyunlong.zhangli.entities.request.ActivityQueryInfo;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.model.response.ApiResult;
import chenyunlong.zhangli.service.ActivityService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("activity")
public class ActivityController {


    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * 获取所有的活动信息
     *
     * @return
     */
    @GetMapping("list")
    public ApiResult listActivity(@Valid ActivityQueryInfo queryInfo, Pageable pageable) {
        return ResultUtil.success(activityService.queryActivities());
    }

    /**
     * 添加动态信息
     *
     * @param activity 动态信息
     * @return
     */
    @PostMapping
    public ApiResult addActivity(@RequestBody Activity activity) {
        activityService.addActivity(activity);
        return ResultUtil.success();
    }


    /**
     * 获取动态的具体信息
     *
     * @param activityId 动态ID
     * @return
     */
    @GetMapping("activity/{activityId}")
    public ApiResult getActivityDetail(@PathVariable(name = "activityId") Long activityId) {
        return ResultUtil.success(activityService.getActivityById(activityId));
    }
}
