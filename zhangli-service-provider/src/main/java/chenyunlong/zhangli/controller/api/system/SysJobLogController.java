package chenyunlong.zhangli.controller.api.system;

import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.core.controller.BaseController;
import chenyunlong.zhangli.core.page.TableDataInfo;
import chenyunlong.zhangli.core.support.ApiResult;
import chenyunlong.zhangli.enums.BusinessType;
import chenyunlong.zhangli.model.entities.sys.SysJob;
import chenyunlong.zhangli.model.entities.sys.SysJobLog;
import chenyunlong.zhangli.service.ISysJobLogService;
import chenyunlong.zhangli.service.ISysJobService;
import chenyunlong.zhangli.utils.StringUtils;
import chenyunlong.zhangli.utils.poi.ExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度日志操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/jobLog")
public class SysJobLogController extends BaseController {
    private final String prefix = "monitor/job";

    private final ISysJobService jobService;

    private final ISysJobLogService jobLogService;

    public SysJobLogController(ISysJobService jobService, ISysJobLogService jobLogService) {
        this.jobService = jobService;
        this.jobLogService = jobLogService;
    }

    //    @RequiresPermissions("monitor:job:view")
    @GetMapping()
    public String jobLog(@RequestParam(value = "jobId", required = false) Long jobId, ModelMap mmap) {
        if (StringUtils.isNotNull(jobId)) {
            SysJob job = jobService.selectJobById(jobId);
            mmap.put("job", job);
        }
        return prefix + "/jobLog";
    }

    //    @RequiresPermissions("monitor:job:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysJobLog jobLog) {
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);
        return getDataTable(list);
    }

    @Log(title = "调度日志", businessType = BusinessType.EXPORT)
//    @RequiresPermissions("monitor:job:export")
    @PostMapping("/export")
    @ResponseBody
    public ApiResult<String> export(SysJobLog jobLog) {
        List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);
        ExcelUtil<SysJobLog> util = new ExcelUtil<>(SysJobLog.class);
        return util.exportExcel(list, "调度日志");
    }

    @Log(title = "调度日志", businessType = BusinessType.DELETE)
//    @RequiresPermissions("monitor:job:remove")
    @PostMapping("/remove")
    @ResponseBody
    public ApiResult<Integer> remove(String ids) {
        return ApiResult.success(jobLogService.deleteJobLogByIds(ids));
    }

    //    @RequiresPermissions("monitor:job:detail")
    @GetMapping("/detail/{jobLogId}")
    public String detail(@PathVariable("jobLogId") Long jobLogId, ModelMap mmap) {
        mmap.put("name", "jobLog");
        mmap.put("jobLog", jobLogService.selectJobLogById(jobLogId));
        return prefix + "/detail";
    }

    @Log(title = "调度日志", businessType = BusinessType.CLEAN)
//    @RequiresPermissions("monitor:job:remove")
    @PostMapping("/clean")
    @ResponseBody
    public ApiResult<Void> clean() {
        jobLogService.cleanJobLog();
        return ApiResult.success();
    }
}
