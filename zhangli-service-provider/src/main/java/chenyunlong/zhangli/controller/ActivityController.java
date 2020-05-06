package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.entities.Activity;
import chenyunlong.zhangli.entities.request.ActivityQueryInfo;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.model.response.BaseResponse;
import chenyunlong.zhangli.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("activity")
public class ActivityController {


    @Autowired
    private ActivityService activityService;

    /**
     * 获取所有的活动信息
     *
     * @return
     */
    @GetMapping("list")
    public BaseResponse listActivity(@Valid ActivityQueryInfo queryInfo) {

        return ResultUtil.success(new ArrayList<Activity>());
    }
}
