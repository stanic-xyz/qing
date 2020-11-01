package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.entities.Activity;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.model.vo.ApiResult;
import chenyunlong.zhangli.service.ActivityService;
import chenyunlong.zhangli.service.AttachementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("activity")
public class ActivityController {


    private final ActivityService activityService;
    @Autowired
    private AttachementService attachementService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * 获取所有的活动信息
     *
     * @return
     */
    @GetMapping("list")
    public ApiResult listActivity(
            @RequestParam(value = "keyword", required = false) String keyword) {
        return ResultUtil.success(activityService.queryActivitiesByPage(keyword));
    }

    /**
     * 添加动态信息
     *
     * @param activity 动态信息
     * @return
     */
    @PostMapping
    @ApiOperation("添加动态信息")
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
    @GetMapping("activity/{activityId:\\d+}")
    public ApiResult getActivityDetail(@PathVariable(name = "activityId") Long activityId) {
        int attachementCount = attachementService.getAttachementCount(activityId);
        return ResultUtil.success(activityService.getActivityById(activityId));
    }
}
