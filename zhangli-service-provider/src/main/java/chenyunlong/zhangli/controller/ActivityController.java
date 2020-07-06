package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.entities.request.ActivityQueryInfo;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.model.response.ApiResult;
import chenyunlong.zhangli.service.ActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResult listActivity(@Valid ActivityQueryInfo queryInfo) {
        return ResultUtil.success(activityService.queryActivities());
    }


    @GetMapping("{activityId}")
    public ApiResult getActivity(@PathVariable("activityId") Long activityId) {
        return ResultUtil.success(activityService.getActivityById(activityId));
    }
}
