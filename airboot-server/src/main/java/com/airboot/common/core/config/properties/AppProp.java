package com.airboot.common.core.config.properties;

import com.airboot.common.core.constant.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 获取自定义属性，由于@Value注解需要动态注入后才能使用，无法直接在静态属性上注入
 * 因此想要直接设置为静态属性使用的话就需要用@PostConstruct注解来初始化init()，将获取到的动态属性注入到静态属性中
 *
 * @author airboot
 */

@Component
public class AppProp {
    
    /**
     * 当前使用的profile
     */
    public static String ACTIVE_PROFILE;
    
    /**
     * 是生产环境
     */
    public static boolean IS_PROD_ENV;
    
    /**
     * 不是生产环境
     */
    public static boolean NOT_PROD_ENV;
    
    /**
     * 判断是否为生产环境
     */
    private static boolean isProdEnv() {
        for (String profile : ACTIVE_PROFILE.split(",")) {
            if (profile.contains(Constants.PROFILE_PROD)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 是测试环境
     */
    public static boolean IS_TEST_ENV;
    
    /**
     * 不是测试环境
     */
    public static boolean NOT_TEST_ENV;
    
    /**
     * 判断是否为测试环境
     */
    private static boolean isTestEnv() {
        for (String profile : ACTIVE_PROFILE.split(",")) {
            if (profile.contains(Constants.PROFILE_TEST)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 新增租户的SQL脚本名称
     */
    public static String TENANT_SQL_SCRIPT_NAME;
    
    // 在Spring动态代理后注入到静态属性中
    @PostConstruct
    public void init() {
        ACTIVE_PROFILE = activeProfile;
        IS_PROD_ENV = isProdEnv();
        NOT_PROD_ENV = !IS_PROD_ENV;
        IS_TEST_ENV = isTestEnv();
        NOT_TEST_ENV = !IS_TEST_ENV;
    
        TENANT_SQL_SCRIPT_NAME = tenantSqlScriptName;
    }
    
    /**
     * 当前使用的profile
     */
    @Value("${spring.profiles.active}")
    private String activeProfile;
    
    @Value("${tenant.sql-script-name}")
    private String tenantSqlScriptName;
}
