package com.airboot.project.monitor.model.entity;

import com.airboot.common.core.aspectj.lang.annotation.Excel;
import com.airboot.common.core.utils.DateUtils;
import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.DeviceEnum;
import com.airboot.common.model.enums.SuccessEnum;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 操作日志记录表 sys_oper_log
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_oper_log")
public class SysOperLog extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 租户ID暂存
     */
    @TableField(exist = false)
    private Long tenantId;
    
    /**
     * 操作模块
     */
    @Excel(name = "操作模块")
    private String title;
    
    /**
     * 操作类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据）
     */
    @Excel(name = "操作类型")
    private OperationTypeEnum operationType;
    
    /**
     * 请求方法
     */
    @Excel(name = "请求方法")
    private String method;
    
    /**
     * 请求方式
     */
    @Excel(name = "请求方式")
    private String requestMethod;
    
    /**
     * 操作设备
     */
    @Excel(name = "操作设备")
    private DeviceEnum device;
    
    /**
     * 浏览器类型
     */
    @Excel(name = "浏览器类型")
    private String browser;
    
    /**
     * 操作系统
     */
    @Excel(name = "操作系统")
    private String os;
    
    /**
     * 操作人员账号
     */
    @Excel(name = "操作人员账号")
    private String operAccount;
    
    /**
     * 操作人员姓名
     */
    @Excel(name = "操作人员姓名")
    private String operName;
    
    /**
     * 操作人员ID
     */
    private Long operUserId;
    
    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String deptName;
    
    /**
     * 请求url
     */
    @Excel(name = "请求地址")
    private String operUrl;
    
    /**
     * 操作地址
     */
    @Excel(name = "操作地址")
    private String operIp;
    
    /**
     * 操作地点
     */
    @Excel(name = "操作地点")
    private String operLocation;
    
    /**
     * 请求参数
     */
    @Excel(name = "请求参数")
    private String operParam;
    
    /**
     * 返回参数
     */
    @Excel(name = "返回参数")
    private String jsonResult;
    
    /**
     * 操作状态（0失败 1成功）
     */
    @Excel(name = "状态")
    private SuccessEnum status;
    
    /**
     * 错误消息
     */
    @Excel(name = "错误消息")
    private String errorMsg;
    
    /**
     * 操作时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT, locale = "zh", timezone = "GMT+8")
    @Excel(name = "操作时间", width = 30, dateFormat = DateUtils.DATETIME_FORMAT)
    private Date operTime;
    
}
