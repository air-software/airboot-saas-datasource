package com.airboot.common.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum CommonEnum {
    
    唯一(0),
    非唯一(1),
    ;
    
    @EnumValue
    private Integer code;
}
