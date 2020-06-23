package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.entities.request.ActivityQueryInfo;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.model.response.ApiResult;
import chenyunlong.zhangli.service.ActivityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.printf(authentication.getName());

        return ResultUtil.success(activityService.queryActivities());
    }
}
