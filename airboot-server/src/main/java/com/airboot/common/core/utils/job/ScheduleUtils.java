package com.airboot.common.core.utils.job;

import com.airboot.common.core.constant.ScheduleConstants;
import com.airboot.common.core.exception.job.TaskException;
import com.airboot.common.core.exception.job.TaskException.Code;
import com.airboot.project.monitor.model.entity.SysJob;
import com.airboot.project.monitor.model.enums.JobStatusEnum;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author airboot
 */
public class ScheduleUtils {
    
    /**
     * 得到quartz任务类
     *
     * @param sysJob 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(SysJob sysJob) {
        return sysJob.getConcurrent() ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }
    
    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }
    
    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(Long jobId, String jobGroup) {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }
    
    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, SysJob job) throws SchedulerException, TaskException {
        Class<? extends Job> jobClass = getQuartzJobClass(job);
        // 构建job信息
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, jobGroup)).build();
        
        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);
        
        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup))
                .withSchedule(cronScheduleBuilder).build();
        
        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, job);
        
        // 判断是否存在
        if (scheduler.checkExists(getJobKey(jobId, jobGroup))) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(getJobKey(jobId, jobGroup));
        }
        
        scheduler.scheduleJob(jobDetail, trigger);
        
        // 暂停任务
        if (job.getStatus().equals(JobStatusEnum.暂停)) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }
    
    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob job, CronScheduleBuilder cb)
            throws TaskException {
        switch (job.getMisfirePolicy()) {
            case 默认:
                return cb;
            case 立即执行:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case 执行一次:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case 放弃执行:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new TaskException("The task misfire policy '" + job.getMisfirePolicy().name()
                        + "' cannot be used in cron schedule tasks", Code.CONFIG_ERROR);
        }
    }
}
