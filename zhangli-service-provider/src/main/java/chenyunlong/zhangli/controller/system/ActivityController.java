package chenyunlong.zhangli.controller.system;

import chenyunlong.zhangli.entities.Activity;
import chenyunlong.zhangli.model.vo.ApiResult;
import chenyunlong.zhangli.common.service.ActivityService;
import chenyunlong.zhangli.common.service.AttachementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Stan
 */
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
     * @return 活动信息列表
     */
    @GetMapping("list")
    public ApiResult<List<Activity>> listActivity(
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
    @ApiOperation("添加动态信息")
    public ApiResult<Object> addActivity(@RequestBody Activity activity) {
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
    public ApiResult getActivityDetail(@PathVariable(name = "activityId") Long activityId) {
        int attachementCount = attachementService.getAttachementCount(activityId);
        return ApiResult.success(activityService.getActivityById(activityId));
    }
}
