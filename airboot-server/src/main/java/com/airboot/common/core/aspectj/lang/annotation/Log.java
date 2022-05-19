package com.airboot.common.core.aspectj.lang.annotation;

import com.airboot.project.monitor.model.enums.OperationTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 * @author airboot
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    
    /**
     * 模块
     */
    String title() default "";
    
    /**
     * 功能
     */
    OperationTypeEnum operationType() default OperationTypeEnum.其它;
    
    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;
    
}
