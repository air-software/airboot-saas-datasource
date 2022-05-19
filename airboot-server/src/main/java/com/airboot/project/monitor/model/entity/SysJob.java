package com.airboot.project.monitor.model.entity;

import com.airboot.common.core.aspectj.lang.annotation.Excel;
import com.airboot.common.core.utils.DateUtils;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.core.utils.job.CronUtils;
import com.airboot.common.model.entity.BaseEntity;
import com.airboot.project.monitor.model.enums.JobStatusEnum;
import com.airboot.project.monitor.model.enums.MisfirePolicyEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 定时任务调度表 sys_job
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_job")
public class SysJob extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 任务名称
     */
    @Excel(name = "任务名称")
    @NotBlank(message = "任务名称不能为空")
    @Size(min = 0, max = 64, message = "任务名称不能超过64个字符")
    private String jobName;
    
    /**
     * 任务组名
     */
    @Excel(name = "任务组名")
    private String jobGroup;
    
    /**
     * 调用目标字符串
     */
    @Excel(name = "调用目标字符串")
    @NotBlank(message = "调用目标字符串不能为空")
    @Size(min = 0, max = 500, message = "调用目标字符串长度不能超过500个字符")
    private String invokeTarget;
    
    /**
     * cron执行表达式
     */
    @Excel(name = "执行表达式 ")
    @NotBlank(message = "Cron执行表达式不能为空")
    @Size(min = 0, max = 255, message = "Cron执行表达式不能超过255个字符")
    private String cronExpression;
    
    /**
     * 执行发生错误的处理策略
     */
    @Excel(name = "执行发生错误的处理策略")
    @Builder.Default
    private MisfirePolicyEnum misfirePolicy = MisfirePolicyEnum.默认;
    
    /**
     * 是否并发执行（0禁止 1允许）
     */
    @Excel(name = "并发执行", readConverterExp = "false=禁止,true=允许")
    private Boolean concurrent;
    
    /**
     * 任务状态（0暂停 1正常）
     */
    @Excel(name = "任务状态")
    private JobStatusEnum status;
    
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT, locale = "zh", timezone = "GMT+8")
    public Date getNextValidTime() {
        if (StringUtils.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }
    
}
