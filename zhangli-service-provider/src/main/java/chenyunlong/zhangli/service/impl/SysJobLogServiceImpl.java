package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.service.ISysJobLogService;
import chenyunlong.zhangli.mapper.SysJobLogMapper;
import chenyunlong.zhangli.model.entities.sys.SysJobLog;
import chenyunlong.zhangli.utils.text.Convert;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务调度日志信息 服务层
 *
 * @author ruoyi
 */
@Service
public class SysJobLogServiceImpl implements ISysJobLogService {
    private final SysJobLogMapper jobLogMapper;

    public SysJobLogServiceImpl(SysJobLogMapper jobLogMapper) {
        this.jobLogMapper = jobLogMapper;
    }

    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog) {
        return jobLogMapper.selectJobLogList(jobLog);
    }

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog selectJobLogById(Long jobLogId) {
        return jobLogMapper.selectJobLogById(jobLogId);
    }

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(SysJobLog jobLog) {
        jobLogMapper.insertJobLog(jobLog);
    }

    /**
     * 批量删除调度日志信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteJobLogByIds(String ids) {
        return jobLogMapper.deleteJobLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteJobLogById(Long jobId) {
        return jobLogMapper.deleteJobLogById(jobId);
    }

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog() {
        jobLogMapper.cleanJobLog();
    }
}