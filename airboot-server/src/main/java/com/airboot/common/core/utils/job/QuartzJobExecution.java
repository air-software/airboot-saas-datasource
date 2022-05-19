package com.airboot.common.core.utils.job;

import com.airboot.project.monitor.model.entity.SysJob;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author airboot
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
