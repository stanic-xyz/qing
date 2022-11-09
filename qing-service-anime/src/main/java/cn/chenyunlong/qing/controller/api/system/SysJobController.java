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

import cn.chenyunlong.qing.annotation.Log;
import cn.chenyunlong.qing.core.ApiResult;
import cn.chenyunlong.qing.core.controller.BaseController;
import cn.chenyunlong.qing.core.enums.BusinessType;
import cn.chenyunlong.qing.core.exception.job.TaskException;
import cn.chenyunlong.qing.core.page.TableDataInfo;
import cn.chenyunlong.qing.domain.system.SysJob;
import cn.chenyunlong.qing.service.ISysJobService;
import cn.chenyunlong.qing.utils.poi.ExcelUtil;
import cn.chenyunlong.qing.utils.quartz.CronUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务调度控制器
 *
 * @author Stan
 * @date 2022/11/05
 */
@Tag(name = "系统定时任务")
@Controller
@RequestMapping("/monitor/job")
@RequiredArgsConstructor
public class SysJobController extends BaseController {

    private final ISysJobService jobService;

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysJob job) {
        startPage();
        List<SysJob> list = jobService.selectJobList(job);
        return getDataTable(list);
    }

    @Log(title = "定时任务", businessType = BusinessType.EXPORT)
//    @RequiresPermissions("monitor:job:export")
    @PostMapping("/export")
    @ResponseBody
    public ApiResult<String> export(SysJob job) {
        List<SysJob> list = jobService.selectJobList(job);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        return util.exportExcel(list, "定时任务");
    }

    @Log(title = "定时任务", businessType = BusinessType.DELETE)
//    @RequiresPermissions("monitor:job:remove")
    @PostMapping("/remove")
    @ResponseBody
    public ApiResult<Void> remove(String ids) throws SchedulerException {
        jobService.deleteJobByIds(ids);
        return ApiResult.success();
    }

    //    @RequiresPermissions("monitor:job:detail")
    @GetMapping("/detail/{jobId}")
    public String detail(@PathVariable("jobId") Long jobId, ModelMap mmap) {
        mmap.put("name", "templates/monitor/job");
        mmap.put("job", jobService.selectJobById(jobId));
        String prefix = "monitor/job";
        return prefix + "/detail";
    }

    /**
     * 任务调度状态修改
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
//    @RequiresPermissions("monitor:job:changeStatus")
    @PostMapping("/changeStatus")
    @ResponseBody
    public ApiResult<Integer> changeStatus(SysJob job) throws SchedulerException {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return ApiResult.success(jobService.changeStatus(newJob));
    }

    /**
     * 任务调度立即执行一次
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
//    @RequiresPermissions("monitor:job:changeStatus")
    @PostMapping("/run")
    @ResponseBody
    public ApiResult<Void> run(SysJob job) throws SchedulerException {
        jobService.run(job);
        return ApiResult.success();
    }

    /**
     * 新增保存调度
     */
    @Log(title = "定时任务", businessType = BusinessType.INSERT)
//    @RequiresPermissions("monitor:job:add")
    @PostMapping("/add")
    @ResponseBody
    public ApiResult<Integer> addSave(@Validated SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return ApiResult.fail("cron表达式不正确");
        }
        return ApiResult.success(jobService.insertJob(job));
    }

    /**
     * 修改保存调度
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
//    @RequiresPermissions("monitor:job:edit")
    @PostMapping("/edit")
    @ResponseBody
    public ApiResult<Integer> editSave(@Validated SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return ApiResult.fail("cron表达式不正确");
        }
        return ApiResult.success(jobService.updateJob(job));
    }

    /**
     * 校验cron表达式是否有效
     */
    @PostMapping("/checkCronExpressionIsValid")
    @ResponseBody
    public boolean checkCronExpressionIsValid(SysJob job) {
        return jobService.checkCronExpressionIsValid(job.getCronExpression());
    }
}
