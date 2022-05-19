package com.airboot.common.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    正常(1),
    停用(0),
    ;
    
    @EnumValue
    private Integer code;
}
