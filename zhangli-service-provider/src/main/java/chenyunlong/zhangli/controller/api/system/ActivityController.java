package chenyunlong.zhangli.controller.api.system;

import chenyunlong.zhangli.model.entities.ActivityEntity;
import chenyunlong.zhangli.core.support.ApiResult;
import chenyunlong.zhangli.service.ActivityService;
import chenyunlong.zhangli.service.AttachementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Stan
 */
@Api(tags = "活动信息")
@RestController
@RequestMapping("activity")
public class ActivityController {

    private final ActivityService activityService;
    private final AttachementService attachementService;

    public ActivityController(ActivityService activityService, AttachementService attachementService) {
        this.activityService = activityService;
        this.attachementService = attachementService;
    }

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
    @ApiOperation("添加动态信息")
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
