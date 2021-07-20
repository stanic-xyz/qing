package chenyunlong.zhangli.controller.api.system;

import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.common.core.controller.BaseController;
import chenyunlong.zhangli.common.core.page.TableDataInfo;
import chenyunlong.zhangli.common.core.support.ApiResult;
import chenyunlong.zhangli.common.enums.BusinessType;
import chenyunlong.zhangli.common.exception.job.TaskException;
import chenyunlong.zhangli.model.entities.sys.SysJob;
import chenyunlong.zhangli.common.service.ISysJobService;
import chenyunlong.zhangli.common.utils.poi.ExcelUtil;
import chenyunlong.zhangli.common.utils.quartz.CronUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/job")
public class SysJobController extends BaseController {
    private final String prefix = "monitor/job";

    @Autowired
    private ISysJobService jobService;

    @GetMapping()
    public String job() {
        return prefix + "/job";
    }

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
     * 新增调度
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
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
     * 修改调度
     */
    @GetMapping("/edit/{jobId}")
    public String edit(@PathVariable("jobId") Long jobId, ModelMap mmap) {
        mmap.put("job", jobService.selectJobById(jobId));
        return prefix + "/edit";
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
