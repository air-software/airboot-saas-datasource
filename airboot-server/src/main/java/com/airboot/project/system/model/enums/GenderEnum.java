package com.airboot.project.system.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {
    男(1),
    女(2),
    保密(0),
    ;
    
    @EnumValue
    private Integer code;
}
