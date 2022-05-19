package com.airboot.project.monitor.model.entity;

import com.airboot.common.core.aspectj.lang.annotation.Excel;
import com.airboot.common.core.utils.DateUtils;
import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.DeviceEnum;
import com.airboot.common.model.enums.LoginResultEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 系统访问记录表 sys_logininfor
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_logininfor")
public class SysLogininfor extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户登录账号
     */
    @Excel(name = "登录账号")
    private String account;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 登录结果
     */
    @Excel(name = "登录结果")
    private LoginResultEnum loginResult;
    
    /**
     * 登录IP地址
     */
    @Excel(name = "登录地址")
    private String ipaddr;
    
    /**
     * 登录地点
     */
    @Excel(name = "登录地点")
    private String loginLocation;
    
    /**
     * 登录设备
     */
    @Excel(name = "登录设备")
    private DeviceEnum device;
    
    /**
     * 浏览器类型
     */
    @Excel(name = "浏览器")
    private String browser;
    
    /**
     * 操作系统
     */
    @Excel(name = "操作系统")
    private String os;
    
    /**
     * 提示消息
     */
    @Excel(name = "提示消息")
    private String msg;
    
    /**
     * 登录时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT, locale = "zh", timezone = "GMT+8")
    @Excel(name = "登录时间", width = 30, dateFormat = DateUtils.DATETIME_FORMAT)
    private Date loginTime;
    
}
