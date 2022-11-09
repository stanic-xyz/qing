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
import cn.chenyunlong.qing.core.page.TableDataInfo;
import cn.chenyunlong.qing.domain.system.SysJob;
import cn.chenyunlong.qing.domain.system.SysJobLog;
import cn.chenyunlong.qing.service.ISysJobLogService;
import cn.chenyunlong.qing.service.ISysJobService;
import cn.chenyunlong.qing.utils.StringUtils;
import cn.chenyunlong.qing.utils.poi.ExcelUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度日志操作处理
 *
 * @author ruoyi
 */
@Tag(name = "系统日志")
@Controller
@RequestMapping("/monitor/jobLog")
@RequiredArgsConstructor
public class SysJobLogController extends BaseController {
    private final String prefix = "monitor/job";

    private final ISysJobService jobService;

    private final ISysJobLogService jobLogService;

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
