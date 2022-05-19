package com.airboot.project.monitor.model.entity;

import com.airboot.common.model.enums.DeviceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 当前在线会话
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserOnline {
    
    /**
     * 会话编号
     */
    private String uuid;
    
    /**
     * 用户唯一标识
     */
    private String userKey;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 登录账号
     */
    private String account;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 登录IP地址
     */
    private String ipaddr;
    
    /**
     * 登录地址
     */
    private String loginLocation;
    
    /**
     * 登录设备
     */
    private DeviceEnum device;
    
    /**
     * 浏览器类型
     */
    private String browser;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 登录时间
     */
    private Long loginTime;
    
}
