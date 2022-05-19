package com.airboot.common.security.annotation;

import java.lang.annotation.*;

/**
 * 权限验证
 *
 * @author airboot
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuthorize {
    
    String value();
    
}
