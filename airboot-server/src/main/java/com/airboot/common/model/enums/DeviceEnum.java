package com.airboot.common.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作设备枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum DeviceEnum {
    PC端(10),
    手机APP(20),
    微信小程序(30),
    ;
    
    @EnumValue
    private Integer code;
}
