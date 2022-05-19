package com.airboot.common.security;

import com.airboot.common.model.enums.DeviceEnum;
import com.airboot.project.system.model.entity.SysUser;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 登录用户身份权限
 *
 * @author airboot
 */
@Data
@NoArgsConstructor
public class LoginUser {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 租户id
     */
    private Long tenantId;
    
    /**
     * 登录凭据
     */
    private String token;
    
    /**
     * 每次登录成功后随机生成（如果已预设则不再随机）
     */
    private String uuid;
    
    /**
     * 用户唯一标识，与userId和登录设备相关
     */
    private String userKey;
    
    /**
     * 登录时使用的登录账号（手机号、邮箱或用户名）
     */
    private String account;
    
    /**
     * 登录时间
     */
    private Long loginTime;
    
    /**
     * 过期时间
     */
    private Long expireTime;
    
    /**
     * 登录IP地址
     */
    private String ipaddr;
    
    /**
     * 登录地点
     */
    private String loginLocation;
    
    /**
     * user-agent
     */
    private UserAgent userAgent;
    
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
     * 权限列表
     */
    private Set<String> permissions;
    
    /**
     * 用户信息
     */
    private SysUser user;
    
    /**
     * 是否已被踢出
     */
    private boolean kickout;
    
    public LoginUser(SysUser user, Set<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
    
    public Long getUserId() {
        return user.getId();
    }
    
    public String getMobile() {
        return user.getMobile();
    }
    
    public String getPersonName() {
        return user.getPersonName();
    }
    
    public String getUsername() {
        return user.getUsername();
    }
    
    public boolean isAdmin() {
        return user.isAdmin();
    }
    
    public boolean isTenantAdmin() {
        return user.isTenantAdmin();
    }
    
}
