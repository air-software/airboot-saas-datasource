package com.airboot.common.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录结果枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum LoginResultEnum {
    登录成功(1),
    登录失败(-1),
    退出成功(11)
    ;
    
    @EnumValue
    private Integer code;
}
