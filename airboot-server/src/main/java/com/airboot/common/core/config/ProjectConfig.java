package com.airboot.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author airboot
 */
@Data
@Component
@ConfigurationProperties(prefix = "airboot")
public class ProjectConfig {
    
    /**
     * 项目名称
     */
    private String name;
    
    /**
     * 版本
     */
    private String version;
    
    /**
     * 验证码开关
     */
    private boolean captchaEnabled;
    
    /**
     * 唯一登录开关（即同一账号在同类型设备上只能同时登录一个）
     */
    private boolean uniqueLogin;
    
    /**
     * 上传路径
     */
    private static String profile;
    
    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;
    
    public static String getProfile() {
        return profile;
    }
    
    public void setProfile(String profile) {
        ProjectConfig.profile = profile;
    }
    
    public static boolean isAddressEnabled() {
        return addressEnabled;
    }
    
    public void setAddressEnabled(boolean addressEnabled) {
        ProjectConfig.addressEnabled = addressEnabled;
    }
    
    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }
    
    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }
    
    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }
}
