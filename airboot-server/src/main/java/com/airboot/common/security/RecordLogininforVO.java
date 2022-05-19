package com.airboot.common.security;

import com.airboot.common.model.enums.DeviceEnum;
import com.airboot.common.model.enums.LoginResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录信息记录
 *
 * @author airboot
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordLogininforVO {
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 登录账号（手机号、邮箱或用户名等）
     */
    private String account;
    
    /**
     * 登录设备
     */
    private DeviceEnum device;
    
    /**
     * 登录结果
     */
    private LoginResultEnum loginResult;
    
    /**
     * 消息
     */
    private String msg;
    
}
