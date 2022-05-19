package com.airboot.project.system.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 证件类型枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum CardTypeEnum {
    身份证(1),
    其他(0),
    ;
    
    @EnumValue
    private Integer code;
}
