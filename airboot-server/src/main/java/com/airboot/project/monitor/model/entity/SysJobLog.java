package com.airboot.project.monitor.model.entity;

import com.airboot.common.core.aspectj.lang.annotation.Excel;
import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.SuccessEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 定时任务调度日志表 sys_job_log
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_job_log")
public class SysJobLog extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 任务名称
     */
    @Excel(name = "任务名称")
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
    private String invokeTarget;
    
    /**
     * 日志信息
     */
    @Excel(name = "日志信息")
    private String jobMessage;
    
    /**
     * 执行状态（0失败 1成功）
     */
    @Excel(name = "执行状态")
    private SuccessEnum status;
    
    /**
     * 异常信息
     */
    @Excel(name = "异常信息")
    private String exceptionInfo;
    
    /**
     * 任务开始时间
     */
    private Date startTime;
    
    /**
     * 任务结束时间
     */
    private Date stopTime;
    
}
