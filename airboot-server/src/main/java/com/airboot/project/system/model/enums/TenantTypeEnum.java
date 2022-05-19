package com.airboot.project.system.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 租户类型
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum TenantTypeEnum {
    管理平台(-1000),
    普通商家(0),
    ;
    
    @EnumValue
    private Integer code;
}
